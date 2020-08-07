package cn.javaer.snippets.spring.exception;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cn-src
 */
@ControllerAdvice
public class GlobalExceptionAdvice {

    private final Map<String, ErrorStatus> errorMapping = new HashMap<>();
    private final ErrorProperties errorProperties;
    private final MessageSourceAccessor messageSourceAccessor;

    public GlobalExceptionAdvice(final ErrorProperties errorProperties,
                                 final Map<String, ErrorStatus> errorMapping,
                                 final ResourceBundleMessageSource messageSource) {
        this.errorProperties = errorProperties;
        this.messageSourceAccessor = new MessageSourceAccessor(messageSource);

        this.errorMapping.put("org.springframework.web.bind.MethodArgumentNotValidException",
                new ErrorStatus(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name()));
        this.errorMapping.put("org.springframework.web.bind" +
                        ".MissingServletRequestParameterException",
                new ErrorStatus(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name()));
        this.errorMapping.put("javax.validation.ConstraintViolationException",
                new ErrorStatus(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name()));

        this.errorMapping.putAll(errorMapping);
    }

    @ResponseBody
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorInfo> handleBadRequestException(
            final HttpServletRequest request,
            final Exception e) {
        final ErrorInfo errorInfo = this.createErrorInfo(request, e);
        return ResponseEntity.status(errorInfo.getStatus()).body(errorInfo);
    }

    public ErrorInfo createErrorInfo(final HttpServletRequest request, final Exception e) {
        final ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setPath(request.getServletPath());
        errorInfo.setTimestamp(LocalDateTime.now());

        // exception
        final String clazz = e.getClass().getName();
        if (this.errorProperties.isIncludeException()) {
            errorInfo.setException(clazz);
        }

        if (this.errorProperties.getIncludeStacktrace() == ErrorProperties.IncludeStacktrace.ALWAYS) {
            errorInfo.setTraceMessage(e.getMessage());
            final StringWriter stackTrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace));
            stackTrace.flush();
            errorInfo.setTrace(stackTrace.toString());
        }

        final ResponseStatus responseStatus = AnnotationUtils.findAnnotation(e.getClass(),
                ResponseStatus.class);

        Integer status;
        String error = null;
        HttpStatus httpStatus = null;
        if (this.errorMapping.containsKey(clazz)) {
            final ErrorStatus errorStatus = this.errorMapping.get(clazz);
            status = errorStatus.code;
            error = errorStatus.error;
        }
        else if (responseStatus != null) {
            httpStatus = responseStatus.code();
            status = httpStatus.value();
            final String reason = responseStatus.reason();
            if (StringUtils.hasLength(reason)) {
                error = reason;
            }
            else {
                error = httpStatus.name();
            }
        }
        else {
            status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
            try {
                httpStatus = HttpStatus.valueOf(status);
                error = httpStatus.name();
            }
            catch (final Exception ignore) {

            }
        }

        status = status == null ? 999 : status;
        error = error == null ? "None" : error;
        errorInfo.setStatus(status);
        errorInfo.setError(error);

        // message
        if (this.errorProperties.getIncludeMessage() == ErrorProperties.IncludeAttribute.ALWAYS) {
            String message = this.messageSourceAccessor.getMessage(error, "");
            if (!StringUtils.hasLength(message) && httpStatus != null) {
                message = httpStatus.getReasonPhrase();
            }
            message = StringUtils.hasLength(message) ? message : "No message available";
            errorInfo.setMessage(message);
        }

        return errorInfo;
    }

    static class ErrorStatus {
        int code;
        String error;

        public ErrorStatus(final int code, final String error) {
            this.code = code;
            this.error = error;
        }
    }
}
