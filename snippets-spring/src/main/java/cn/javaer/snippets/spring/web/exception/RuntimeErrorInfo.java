package cn.javaer.snippets.spring.web.exception;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author cn-src
 */
@Data
public class RuntimeErrorInfo {
    @NotNull
    private final String error;
    private final Integer status;
    private final String message;

    private String path;
    private String exception;
    private String trace;
    private String traceMessage;
    private LocalDateTime timestamp;

    public RuntimeErrorInfo(final DefinedErrorInfo definedErrorInfo) {
        this.error = definedErrorInfo.getError();
        this.status = definedErrorInfo.getStatus();
        this.message = definedErrorInfo.getMessage();
    }
}
