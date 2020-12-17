package cn.javaer.snippets.spring.web.exception;

import cn.javaer.snippets.spring.web.DefaultAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

/**
 * @author cn-src
 */
@ControllerAdvice
public class GlobalExceptionAdvice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ErrorProperties errorProperties;
    private final ErrorInfoExtractor errorInfoExtractor;
    private final boolean includeMessage;

    public GlobalExceptionAdvice(final ErrorProperties errorProperties,
                                 final ErrorInfoExtractor errorInfoExtractor) {
        this.errorProperties = errorProperties;
        this.errorInfoExtractor = errorInfoExtractor;
        this.includeMessage =
            errorProperties.getIncludeMessage() == ErrorProperties.IncludeAttribute.ALWAYS;
    }

    @ResponseBody
    @ExceptionHandler({Exception.class})
    public ResponseEntity<RuntimeErrorInfo> handleBadRequestException(
        final HttpServletRequest request, final Exception e) {
        this.logger.error("", e);

        final DefinedErrorInfo definedErrorInfo = this.errorInfoExtractor.extract(e,
            this.includeMessage);

        final RuntimeErrorInfo runtimeErrorInfo;
        if (null != definedErrorInfo) {
            runtimeErrorInfo = new RuntimeErrorInfo(definedErrorInfo);
        }
        else {
            runtimeErrorInfo = new RuntimeErrorInfo(
                DefinedErrorInfo.of(HttpStatus.INTERNAL_SERVER_ERROR));
        }

        this.fillInfo(runtimeErrorInfo, request, e);
        return ResponseEntity.status(runtimeErrorInfo.getStatus()).body(runtimeErrorInfo);
    }

    public void fillInfo(final RuntimeErrorInfo runtimeErrorInfo,
                         final HttpServletRequest request,
                         final Exception e) {
        runtimeErrorInfo.setPath(request.getServletPath());
        runtimeErrorInfo.setTimestamp(LocalDateTime.now());
        runtimeErrorInfo.setRequestId(DefaultAppContext.getRequestId());
        // exception
        final String clazz = e.getClass().getName();
        if (this.errorProperties.isIncludeException()) {
            runtimeErrorInfo.setException(clazz);
        }

        if (this.errorProperties.getIncludeStacktrace() == ErrorProperties.IncludeStacktrace.ALWAYS) {
            runtimeErrorInfo.setTraceMessage(e.getMessage());
            final StringWriter stackTrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace));
            stackTrace.flush();
            runtimeErrorInfo.setTrace(stackTrace.toString());
        }
    }
}
