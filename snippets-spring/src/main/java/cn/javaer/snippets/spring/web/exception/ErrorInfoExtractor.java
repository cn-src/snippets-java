package cn.javaer.snippets.spring.web.exception;

import cn.javaer.snippets.exception.DefinedErrorInfo;
import cn.javaer.snippets.exception.Error;
import cn.javaer.snippets.exception.RuntimeErrorInfo;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author cn-src
 */
public class ErrorInfoExtractor {

    private final Map<String, DefinedErrorInfo> configuredErrorMapping = new HashMap<>();
    private final Map<String, DefinedErrorInfo> internalErrorMapping;
    private final MessageSourceAccessor messageSourceAccessor;

    public ErrorInfoExtractor(final Map<String, DefinedErrorInfo> errorMapping,
                              final ResourceBundleMessageSource messageSource) {

        this.internalErrorMapping = this.initInternalErrorMapping();
        this.messageSourceAccessor = new MessageSourceAccessor(messageSource, Locale.CHINESE);
        if (!CollectionUtils.isEmpty(errorMapping)) {
            this.configuredErrorMapping.putAll(errorMapping);
        }
    }

    public Map<String, DefinedErrorInfo> getControllersErrorMapping(final Collection<Object> controllers) {
        final Map<String, DefinedErrorInfo> result = new HashMap<>(20);
        for (final Object ctr : controllers) {
            final Method[] methods = ctr.getClass().getDeclaredMethods();
            for (final Method method : methods) {
                if (null != AnnotationUtils.findAnnotation(method, RequestMapping.class)) {
                    final Class<?>[] exceptionTypes = method.getExceptionTypes();
                    if (exceptionTypes != null && exceptionTypes.length > 0) {
                        for (final Class<?> type : exceptionTypes) {
                            @SuppressWarnings("unchecked")
                            final Class<? extends Throwable> t = (Class<? extends Throwable>) type;
                            final DefinedErrorInfo extract = this.getErrorInfoWithMessage(t);
                            result.put(t.getName(), extract);
                        }
                    }
                }
            }
        }
        return result;
    }

    public RuntimeErrorInfo getRuntimeErrorInfo(final Throwable t, final boolean includeMessage) {
        final RuntimeErrorInfo errorInfo = new RuntimeErrorInfo(this.getErrorInfo(t));
        if (includeMessage) {
            final String msg = this.getMessage(t);
            if (StringUtils.hasLength(msg)) {
                errorInfo.setMessage(msg);
            }
            else {
                final String message = this.messageSourceAccessor.getMessage(errorInfo.getError());
                if (StringUtils.hasLength(message)) {
                    errorInfo.setMessage(message);
                }
            }
        }
        else {
            errorInfo.setMessage(null);
        }
        return errorInfo;
    }

    public DefinedErrorInfo getErrorInfoWithMessage(final Class<? extends Throwable> clazz) {
        final DefinedErrorInfo errorInfo = this.getErrorInfo(clazz);
        final String message = this.messageSourceAccessor.getMessage(errorInfo.getError());
        if (StringUtils.hasLength(message)) {
            return errorInfo.withMessage(message);
        }
        return errorInfo;
    }

    @NotNull
    public DefinedErrorInfo getErrorInfo(final Throwable t) {
        Class<? extends Throwable> clazz = t.getClass();
        if (t.getCause() instanceof InvalidFormatException) {
            clazz = InvalidFormatException.class;
        }
        return this.getErrorInfo(clazz);
    }

    @NotNull
    public DefinedErrorInfo getErrorInfo(final Class<? extends Throwable> clazz) {
        if (this.configuredErrorMapping.containsKey(clazz.getName())) {
            return this.configuredErrorMapping.get(clazz.getName());
        }
        final Error error = AnnotatedElementUtils.findMergedAnnotation(clazz, Error.class);
        if (error != null) {
            return DefinedErrorInfo.of(error);
        }
        final ResponseStatus responseStatus = AnnotationUtils.findAnnotation(
            clazz, ResponseStatus.class);
        if (null != responseStatus) {
            return DefinedErrorInfo.of(responseStatus);
        }
        if (this.internalErrorMapping.containsKey(clazz.getName())) {
            return this.internalErrorMapping.get(clazz.getName());
        }
        return DefinedErrorInfo.of(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Nullable
    private String getMessage(final Throwable e) {
        if (e.getCause() instanceof InvalidFormatException) {
            final InvalidFormatException cause = (InvalidFormatException) e.getCause();
            return this.messageSourceAccessor.getMessage(
                "PARAM_INVALID_FORMAT", new Object[]{cause.getValue()});
        }
        if (e instanceof MethodArgumentTypeMismatchException) {
            final MethodArgumentTypeMismatchException ec = (MethodArgumentTypeMismatchException) e;
            return this.messageSourceAccessor.getMessage(
                "PARAM_INVALID_TYPE", new Object[]{ec.getName(), ec.getValue()});
        }
        if (e instanceof MethodArgumentNotValidException) {
            final MethodArgumentNotValidException ec = (MethodArgumentNotValidException) e;
            final List<FieldError> fieldErrors = ec.getBindingResult().getFieldErrors();
            final StringJoiner sb = new StringJoiner("; ");
            for (final FieldError fieldError : fieldErrors) {
                final String message = this.messageSourceAccessor.getMessage(
                    "PARAM_INVALID", new Object[]{fieldError.getField(),
                        fieldError.getRejectedValue(),
                        fieldError.getDefaultMessage()});
                sb.add(message);
            }
            return sb.toString();
        }
        return null;
    }

    public MessageSourceAccessor getMessageSourceAccessor() {
        return this.messageSourceAccessor;
    }

    @UnmodifiableView
    public Map<String, DefinedErrorInfo> getInternalErrorMapping() {
        return this.internalErrorMapping;
    }

    @UnmodifiableView
    public Map<String, DefinedErrorInfo> getConfiguredErrorMapping() {
        return Collections.unmodifiableMap(this.configuredErrorMapping);
    }

    @UnmodifiableView
    Map<String, DefinedErrorInfo> initInternalErrorMapping() {
        final Map<String, DefinedErrorInfo> internalErrorMapping = new HashMap<>(15);
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

        internalErrorMapping.put(
            "org.springframework.web.method.annotation.MethodArgumentTypeMismatchException",
            DefinedErrorInfo.of(HttpStatus.BAD_REQUEST));

        return Collections.unmodifiableMap(internalErrorMapping);
    }
}