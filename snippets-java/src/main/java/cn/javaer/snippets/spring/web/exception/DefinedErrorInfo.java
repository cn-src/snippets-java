package cn.javaer.snippets.spring.web.exception;

import lombok.Value;
import lombok.With;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    public static DefinedErrorInfo of(final Error error) {
        return new DefinedErrorInfo(error.error(), error.httpCode(), error.message());
    }

    public static DefinedErrorInfo of(final Error error, final String message) {
        return new DefinedErrorInfo(error.error(), error.httpCode(), message);
    }

    public static DefinedErrorInfo of(final ResponseStatus responseStatus) {
        final HttpStatus httpStatus = responseStatus.code() == HttpStatus.INTERNAL_SERVER_ERROR ?
            responseStatus.value() : responseStatus.code();

        if (responseStatus.reason().isEmpty()) {
            return DefinedErrorInfo.of(httpStatus);
        }
        return DefinedErrorInfo.of(httpStatus, responseStatus.reason());
    }

    public static DefinedErrorInfo of(final ResponseStatus responseStatus, final String message) {
        final HttpStatus httpStatus = responseStatus.code() == HttpStatus.INTERNAL_SERVER_ERROR ?
            responseStatus.value() : responseStatus.code();
        
        return DefinedErrorInfo.of(httpStatus, message);
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
