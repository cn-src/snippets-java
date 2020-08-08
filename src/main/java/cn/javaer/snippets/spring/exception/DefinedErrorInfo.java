package cn.javaer.snippets.spring.exception;

import lombok.Value;
import lombok.With;
import org.springframework.http.HttpStatus;

/**
 * @author cn-src
 */
@Value
public class DefinedErrorInfo {
    String error;
    Integer status;
    @With String message;

    public DefinedErrorInfo(final String error, final Integer status) {
        this.error = error;
        this.status = status;
        this.message = "";
    }

    public DefinedErrorInfo(final HttpStatus httpStatus) {
        this.error = httpStatus.name();
        this.status = httpStatus.value();
        this.message = "";
    }
}
