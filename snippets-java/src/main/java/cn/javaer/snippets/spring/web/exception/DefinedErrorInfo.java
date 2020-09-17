package cn.javaer.snippets.spring.web.exception;

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

    public DefinedErrorInfo(final String error, final Integer status, final String message) {
        this.error = error;
        this.status = status;
        this.message = message;
    }

    public DefinedErrorInfo(final String error, final Integer status) {
        this.error = error;
        this.status = status;
        this.message = "";
    }

    public DefinedErrorInfo(final HttpStatus httpStatus) {
        this(httpStatus.name(), httpStatus.value(), httpStatus.getReasonPhrase());
    }

    public DefinedErrorInfo(final HttpStatus httpStatus, final String message) {
        this.error = httpStatus.name();
        this.status = httpStatus.value();
        this.message = message;
    }
}
