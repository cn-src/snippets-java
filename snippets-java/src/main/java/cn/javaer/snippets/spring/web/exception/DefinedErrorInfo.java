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

    @Deprecated
    public DefinedErrorInfo(final String error, final Integer status, final String message) {
        this.error = error;
        this.status = status;
        this.message = message;
    }

    @Deprecated
    public DefinedErrorInfo(final String error, final Integer status) {
        this.error = error;
        this.status = status;
        this.message = "";
    }

    @Deprecated
    public DefinedErrorInfo(final HttpStatus httpStatus) {
        this(httpStatus.name(), httpStatus.value(), httpStatus.getReasonPhrase());
    }

    @Deprecated
    public DefinedErrorInfo(final HttpStatus httpStatus, final String message) {
        this.error = httpStatus.name();
        this.status = httpStatus.value();
        this.message = message;
    }

    public static DefinedErrorInfo of(final String error, final Integer status,
                                      final String message) {
        return new DefinedErrorInfo(error, status, message);
    }

    public static DefinedErrorInfo of(final String error, final Integer status) {
        return new DefinedErrorInfo(error, status, "");
    }

    public static DefinedErrorInfo of(final HttpStatus httpStatus) {
        return new DefinedErrorInfo(httpStatus.name(), httpStatus.value(),
            httpStatus.getReasonPhrase());
    }

    public static DefinedErrorInfo of(final HttpStatus httpStatus, final String message) {
        return new DefinedErrorInfo(httpStatus.name(), httpStatus.value(), message);
    }
}
