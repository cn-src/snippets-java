package cn.javaer.snippets.spring.exception;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author cn-src
 */
@Data
public class RuntimeErrorInfo {
    private Integer status;
    @NotNull
    private String error;
    private String message;
    private String path;
    private String exception;
    private String trace;
    private String traceMessage;
    private LocalDateTime timestamp;

    public RuntimeErrorInfo() {
    }

    public RuntimeErrorInfo(final DefinedErrorInfo definedErrorInfo) {
        this.error = definedErrorInfo.getError();
        this.status = definedErrorInfo.getStatus();
        this.message = definedErrorInfo.getMessage();
    }
}
