package cn.javaer.snippets.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Value;

/**
 * @author cn-src
 */
@Value
public class Auditor<T, U, C> {
    @JsonUnwrapped
    T unwrapped;
    U updatedBy;
    C createdBy;

    Auditor(final T unwrapped, final U updatedBy, final C createdBy) {
        this.unwrapped = unwrapped;
        this.updatedBy = updatedBy;
        this.createdBy = createdBy;
    }

    public static <T, U, C> Auditor<T, U, C>
    of(final T unwrapped, final U updatedBy, final C createdBy) {
        return new Auditor<>(unwrapped, updatedBy, createdBy);
    }
}
