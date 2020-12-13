package cn.javaer.snippets.spring.web.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
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

    private final Map<String, DefinedErrorInfo> configuredErrorMapping;
    private final MessageSourceAccessor messageSourceAccessor;

    public ErrorInfoExtractor(
        final Map<String, DefinedErrorInfo> errorMapping,
        final ResourceBundleMessageSource messageSource) {

        this.messageSourceAccessor = new MessageSourceAccessor(messageSource, Locale.CHINESE);
        this.configuredErrorMapping = getInternalErrorMapping();
        if (!CollectionUtils.isEmpty(errorMapping)) {
            this.configuredErrorMapping.putAll(errorMapping);
        }
    }

    public Map<String, DefinedErrorInfo> getControllersErrorMapping(final Collection<Object> controllers, final boolean isIncludeMessage) {
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
                            final DefinedErrorInfo extract = this.extract(t, isIncludeMessage);
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

    public DefinedErrorInfo extract(final Exception e, final boolean isIncludeMessage) {
        if (!isIncludeMessage) {
            return this.extract(e.getClass(), false);
        }
        final DefinedErrorInfo info = this.extract(e.getClass(), false);
        if (null == info) {
            return null;
        }
        if (e.getCause() instanceof InvalidFormatException) {
            final InvalidFormatException cause = (InvalidFormatException) e.getCause();
            final Object value = cause.getValue();
            final String message = this.messageSourceAccessor.getMessage(
                "PARAM_INVALID_FORMAT", new Object[]{value});
            return DefinedErrorInfo.of(info, message);
        }
        if (e instanceof MethodArgumentTypeMismatchException) {
            final MethodArgumentTypeMismatchException ec = (MethodArgumentTypeMismatchException) e;
            final String message = this.messageSourceAccessor.getMessage(
                "PARAM_INVALID_TYPE", new Object[]{ec.getName(), ec.getValue()});
            return DefinedErrorInfo.of(info, message);
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
            return DefinedErrorInfo.of(info, sb.toString());
        }
        return info;
    }

    public Map<String, DefinedErrorInfo> getConfiguredErrorInfos() {
        return Collections.unmodifiableMap(this.configuredErrorMapping);
    }

    public MessageSourceAccessor getMessageSourceAccessor() {
        return this.messageSourceAccessor;
    }

    static Map<String, DefinedErrorInfo> getInternalErrorMapping() {
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

        return internalErrorMapping;
    }
}

