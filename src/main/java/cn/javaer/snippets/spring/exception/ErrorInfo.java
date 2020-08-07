package cn.javaer.snippets.spring.exception;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author cn-src
 */
@Data
public class ErrorInfo {

    private Integer status;
    @NotNull
    private String error;
    private String message;
    private String path;
    private String exception;
    private String trace;
    private String traceMessage;
    private LocalDateTime timestamp;
}