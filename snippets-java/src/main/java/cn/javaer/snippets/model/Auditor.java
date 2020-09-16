package cn.javaer.snippets.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Value;
import org.springframework.data.annotation.Immutable;

/**
 * @author cn-src
 */
@Value
@Immutable
public class Auditor<T, U> {
    @JsonUnwrapped
    T unwrapped;
    U updatedBy;
    U createdBy;

    Auditor(final T unwrapped, final U updatedBy, final U createdBy) {
        this.unwrapped = unwrapped;
        this.updatedBy = updatedBy;
        this.createdBy = createdBy;
    }

    public static <T, U> Auditor<T, U>
    of(final T unwrapped, final U updatedBy, final U createdBy) {
        return new Auditor<>(unwrapped, updatedBy, createdBy);
    }
}
