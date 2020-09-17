package cn.javaer.snippets.spring.web.exception;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
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
public class ErrorInfoExtractor {

    private final Map<String, DefinedErrorInfo> configuredErrorMapping;
    private final ResourceBundleMessageSource messageSource;
    private final MessageSourceAccessor messageSourceAccessor;

    public ErrorInfoExtractor(
        final Map<String, DefinedErrorInfo> errorMapping,
        final ResourceBundleMessageSource messageSource) {

        this.messageSource = messageSource;
        this.messageSourceAccessor = new MessageSourceAccessor(messageSource, Locale.CHINESE);
        this.configuredErrorMapping = getInternalErrorMapping();
        if (!CollectionUtils.isEmpty(errorMapping)) {
            this.configuredErrorMapping.putAll(errorMapping);
        }
    }

    Set<String> getMessageSourceErrors() {
        final Set<String> errors = new HashSet<>();

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
                errors.addAll(resourceBundle.keySet());
            }
        }
        return errors;
    }

    public Map<String, DefinedErrorInfo> getControllersErrorMapping(final Collection<Object> controllers) {
        final Map<String, DefinedErrorInfo> result = new HashMap<>();
        for (final Object ctr : controllers) {
            final Method[] methods = ctr.getClass().getDeclaredMethods();
            for (final Method method : methods) {
                if (null != AnnotationUtils.findAnnotation(method, RequestMapping.class)) {
                    final Class<?>[] exceptionTypes = method.getExceptionTypes();
                    if (exceptionTypes != null && exceptionTypes.length > 0) {
                        for (final Class<?> type : exceptionTypes) {
                            @SuppressWarnings("unchecked")
                            final Class<? extends Throwable> t = (Class<? extends Throwable>) type;
                            final DefinedErrorInfo extract = this.extract(t, true);
                            if (null != extract) {
                                result.put(t.getName(), extract);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Nullable
    public DefinedErrorInfo extract(final Class<? extends Throwable> clazz,
                                    final boolean isIncludeMessage) {
        return this.extract(clazz, isIncludeMessage, this.configuredErrorMapping);
    }

    @Nullable
    public DefinedErrorInfo extract(final Class<? extends Throwable> clazz,
                                    final boolean isIncludeMessage,
                                    final Map<String, DefinedErrorInfo> configured) {

        if (configured.containsKey(clazz.getName())) {
            final DefinedErrorInfo errorInfo = configured.get(clazz.getName());
            if (!isIncludeMessage) {
                return errorInfo;
            }
            final String message = this.messageSourceAccessor.getMessage(errorInfo.getError());
            if (message.isEmpty()) {
                return errorInfo;
            }
            return errorInfo.withMessage(message);
        }

        final Error error = AnnotatedElementUtils.findMergedAnnotation(clazz,
            Error.class);
        if (null != error) {
            if (!isIncludeMessage) {
                return DefinedErrorInfo.of(error);
            }
            final String message = this.messageSourceAccessor.getMessage(
                error.error(), error.message());
            return DefinedErrorInfo.of(error, message);
        }

        final ResponseStatus responseStatus = AnnotationUtils.findAnnotation(
            clazz, ResponseStatus.class);
        if (null == responseStatus) {
            return null;
        }
        if (!isIncludeMessage) {
            return DefinedErrorInfo.of(responseStatus);
        }
        final HttpStatus httpStatus = responseStatus.code() == HttpStatus.INTERNAL_SERVER_ERROR ?
            responseStatus.value() : responseStatus.code();
        final String message = this.messageSourceAccessor.getMessage(httpStatus.name());
        if (message.isEmpty()) {
            return DefinedErrorInfo.of(responseStatus);
        }
        return DefinedErrorInfo.of(httpStatus, message);
    }

    public Map<String, DefinedErrorInfo> getConfiguredErrorInfos() {
        return Collections.unmodifiableMap(this.configuredErrorMapping);
    }

    static Map<String, DefinedErrorInfo> getInternalErrorMapping() {
        final Map<String, DefinedErrorInfo> internalErrorMapping = new HashMap<>();
        internalErrorMapping.put(
            "org.springframework.web.HttpRequestMethodNotSupportedException",
            DefinedErrorInfo.of(HttpStatus.METHOD_NOT_ALLOWED)
        );

        internalErrorMapping.put(
            "org.springframework.web.HttpMediaTypeNotSupportedException",
            DefinedErrorInfo.of(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
        );

        internalErrorMapping.put(
            "org.springframework.web.HttpMediaTypeNotAcceptableException",
            DefinedErrorInfo.of(HttpStatus.NOT_ACCEPTABLE)
        );

        internalErrorMapping.put(
            "org.springframework.web.bind.MissingPathVariableException",
            DefinedErrorInfo.of(HttpStatus.BAD_REQUEST));

        internalErrorMapping.put(
            "org.springframework.web.bind.MissingServletRequestParameterException",
            DefinedErrorInfo.of(HttpStatus.BAD_REQUEST));

        internalErrorMapping.put(
            "org.springframework.web.bind.ServletRequestBindingException",
            DefinedErrorInfo.of(HttpStatus.BAD_REQUEST));

        internalErrorMapping.put(
            "org.springframework.beans.TypeMismatchException",
            DefinedErrorInfo.of(HttpStatus.BAD_REQUEST));

        internalErrorMapping.put(
            "org.springframework.http.converter.HttpMessageNotReadableException",
            DefinedErrorInfo.of(HttpStatus.BAD_REQUEST));

        internalErrorMapping.put(
            "org.springframework.web.bind.MethodArgumentNotValidException",
            DefinedErrorInfo.of(HttpStatus.BAD_REQUEST));

        internalErrorMapping.put(
            "org.springframework.web.multipart.support.MissingServletRequestPartException",
            DefinedErrorInfo.of(HttpStatus.BAD_REQUEST));

        internalErrorMapping.put(
            "org.springframework.validation.BindException",
            DefinedErrorInfo.of(HttpStatus.BAD_REQUEST));

        internalErrorMapping.put(
            "org.springframework.web.servlet.NoHandlerFoundException",
            DefinedErrorInfo.of(HttpStatus.NOT_FOUND));

        internalErrorMapping.put(
            "org.springframework.web.context.request.async.AsyncRequestTimeoutException",
            DefinedErrorInfo.of(HttpStatus.SERVICE_UNAVAILABLE));

        internalErrorMapping.put("javax.validation.ConstraintViolationException",
            DefinedErrorInfo.of(HttpStatus.BAD_REQUEST));

        return internalErrorMapping;
    }
}

