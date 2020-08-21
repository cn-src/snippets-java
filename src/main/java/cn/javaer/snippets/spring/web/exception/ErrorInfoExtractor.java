package cn.javaer.snippets.spring.web.exception;

import org.springframework.beans.BeansException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author cn-src
 */
public class ErrorInfoExtractor implements ApplicationContextAware {

    private final Map<String, DefinedErrorInfo> errorInfos = new HashMap<>();
    private final Map<String, DefinedErrorInfo> internalErrorMapping = new HashMap<>();
    private final Map<String, DefinedErrorInfo> errorMapping;
    private final Set<String> errors = new HashSet<>();
    private final ResourceBundleMessageSource messageSource;
    private final MessageSourceAccessor messageSourceAccessor;
    private final boolean isIncludeMessage;
    private ApplicationContext applicationContext;

    public ErrorInfoExtractor(
            final Map<String, DefinedErrorInfo> errorMapping,
            final ResourceBundleMessageSource messageSource,
            final boolean isIncludeMessage) {

        this.messageSource = messageSource;
        this.messageSourceAccessor = new MessageSourceAccessor(messageSource, Locale.CHINESE);
        this.isIncludeMessage = isIncludeMessage;
        this.errorMapping = errorMapping;
    }

    @PostConstruct
    public void init() {
        this.initErrors();
        this.initErrorMapping();
        for (final String error : this.errors) {
            Arrays.stream(HttpStatus.values())
                    .filter(it -> it.name().equals(error))
                    .findFirst()
                    .ifPresent(it -> {
                        if (this.isIncludeMessage) {
                            final String message = this.messageSourceAccessor.getMessage(
                                    error, "No message available");
                            this.errorInfos.put(error, new DefinedErrorInfo(it, message));
                        }
                        else {
                            this.errorInfos.put(error, new DefinedErrorInfo(it));
                        }
                    });
        }
        final Collection<Object> controllers =
                this.applicationContext.getBeansWithAnnotation(Controller.class).values();

        final Set<Class<? extends Throwable>> throwables = this.findThrowables(controllers);
        for (final Class<? extends Throwable> throwable : throwables) {
            final DefinedErrorInfo extracted = this.extract(throwable);
            if (null != extracted) {
                final String error = extracted.getError();
                if (this.errors.contains(error)) {
                    if (this.errorInfos.containsKey(error) &&
                            !this.errorInfos.get(error).getStatus().equals(extracted.getStatus())) {
                        final String msg = String.format("Error:'%s', Has multiple status: '%d' " +
                                        "and '%d'",
                                error, this.errorInfos.get(error).getStatus(),
                                extracted.getStatus());

                        throw new IllegalStateException(msg);
                    }
                    if (this.isIncludeMessage) {
                        final String message = this.messageSourceAccessor.getMessage(
                                error, "No message available");
                        this.errorInfos.put(error, extracted.withMessage(message));
                    }
                    else {
                        this.errorInfos.put(error, extracted);
                    }
                }
            }
        }
    }

    void initErrorMapping() {
        this.internalErrorMapping.put(HttpRequestMethodNotSupportedException.class.getName(),
                new DefinedErrorInfo(HttpStatus.METHOD_NOT_ALLOWED));

        this.internalErrorMapping.put(HttpMediaTypeNotSupportedException.class.getName(),
                new DefinedErrorInfo(HttpStatus.UNSUPPORTED_MEDIA_TYPE));

        this.internalErrorMapping.put(HttpMediaTypeNotAcceptableException.class.getName(),
                new DefinedErrorInfo(HttpStatus.NOT_ACCEPTABLE));

        this.internalErrorMapping.put(MissingPathVariableException.class.getName(),
                new DefinedErrorInfo(HttpStatus.BAD_REQUEST));

        this.internalErrorMapping.put(MissingServletRequestParameterException.class.getName(),
                new DefinedErrorInfo(HttpStatus.BAD_REQUEST));

        this.internalErrorMapping.put(ServletRequestBindingException.class.getName(),
                new DefinedErrorInfo(HttpStatus.BAD_REQUEST));

        this.internalErrorMapping.put(ConversionNotSupportedException.class.getName(),
                new DefinedErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR));

        this.internalErrorMapping.put(TypeMismatchException.class.getName(),
                new DefinedErrorInfo(HttpStatus.BAD_REQUEST));

        this.internalErrorMapping.put(HttpMessageNotReadableException.class.getName(),
                new DefinedErrorInfo(HttpStatus.BAD_REQUEST));

        this.internalErrorMapping.put(HttpMessageNotWritableException.class.getName(),
                new DefinedErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR));

        this.internalErrorMapping.put(MethodArgumentNotValidException.class.getName(),
                new DefinedErrorInfo(HttpStatus.BAD_REQUEST));

        this.internalErrorMapping.put(MissingServletRequestPartException.class.getName(),
                new DefinedErrorInfo(HttpStatus.BAD_REQUEST));

        this.internalErrorMapping.put(BindException.class.getName(),
                new DefinedErrorInfo(HttpStatus.BAD_REQUEST));

        this.internalErrorMapping.put(NoHandlerFoundException.class.getName(),
                new DefinedErrorInfo(HttpStatus.NOT_FOUND));

        this.internalErrorMapping.put(AsyncRequestTimeoutException.class.getName(),
                new DefinedErrorInfo(HttpStatus.SERVICE_UNAVAILABLE));

        this.internalErrorMapping.put("javax.validation.ConstraintViolationException",
                new DefinedErrorInfo(HttpStatus.BAD_REQUEST));

        if (!CollectionUtils.isEmpty(this.errorMapping)) {
            this.internalErrorMapping.putAll(this.errorMapping);
        }
    }

    void initErrors() {
        final Method getResourceBundle =
                Objects.requireNonNull(ReflectionUtils.findMethod(this.messageSource.getClass(),
                        "getResourceBundle", String.class, Locale.class));
        getResourceBundle.setAccessible(true);
        final Method getDefaultLocale =
                Objects.requireNonNull(ReflectionUtils.findMethod(this.messageSourceAccessor.getClass(),
                        "getDefaultLocale"));
        getDefaultLocale.setAccessible(true);
        final Locale locale = (Locale) ReflectionUtils.invokeMethod(getDefaultLocale,
                this.messageSourceAccessor);

        for (final String basename : this.messageSource.getBasenameSet()) {

            final ResourceBundle resourceBundle =
                    (ResourceBundle) ReflectionUtils.invokeMethod(getResourceBundle,
                            this.messageSource,
                            basename, locale);
            if (null != resourceBundle && !resourceBundle.keySet().isEmpty()) {
                this.errors.addAll(resourceBundle.keySet());
            }
        }
    }

    @SuppressWarnings("unchecked")
    Set<Class<? extends Throwable>> findThrowables(final Collection<Object> controllers) {
        final Set<Class<? extends Throwable>> throwables = new HashSet<>();
        for (final Object ctr : controllers) {
            final Method[] methods = ctr.getClass().getDeclaredMethods();
            for (final Method method : methods) {
                if (null != AnnotationUtils.findAnnotation(method, RequestMapping.class)) {
                    final Class<?>[] exceptionTypes = method.getExceptionTypes();
                    if (exceptionTypes != null && exceptionTypes.length > 0) {
                        for (final Class<?> type : exceptionTypes) {
                            throwables.add((Class<? extends Throwable>) type);
                        }
                    }
                }
            }
        }
        return throwables;
    }

    @Nullable
    public DefinedErrorInfo extract(final Class<? extends Throwable> clazz) {

        if (this.internalErrorMapping.containsKey(clazz.getName())) {
            return this.internalErrorMapping.get(clazz.getName());
        }

        final ResponseStatus responseStatus = AnnotationUtils.findAnnotation(clazz,
                ResponseStatus.class);
        if (null != responseStatus) {
            final HttpStatus httpStatus = responseStatus.code();
            String error = responseStatus.reason();
            if (!StringUtils.hasLength(error)) {
                error = httpStatus.name();
            }
            return new DefinedErrorInfo(error, httpStatus.value());
        }
        return null;
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Map<String, DefinedErrorInfo> getErrorInfos() {
        return this.errorInfos;
    }
}

