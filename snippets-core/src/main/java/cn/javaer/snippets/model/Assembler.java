package cn.javaer.snippets.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Value;

/**
 * @author cn-src
 */
@Value
public class Assembler<T1, T2> {
    @JsonUnwrapped
    T1 unwrapped1;

    @JsonUnwrapped
    T2 unwrapped2;

    Assembler(final T1 unwrapped1, final T2 unwrapped2) {
        this.unwrapped1 = unwrapped1;
        this.unwrapped2 = unwrapped2;
    }
}
