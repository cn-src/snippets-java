package cn.javaer.snippets.model;

import java.util.Collections;

/**
 * @author cn-src
 */
public interface Assemblers {

    static <T1, T2> Assembler<T1, T2> of(final T1 unwrapped1,
                                         final T2 unwrapped2) {
        return new Assembler<>(unwrapped1, unwrapped2);
    }

    static <T1, T2> DynamicAssembler<T1, T2> ofDynamic(final T1 unwrapped1,
                                                       final String propName, final T2 propValue) {
        return new DynamicAssembler<>(unwrapped1, Collections.singletonMap(propName, propValue));
    }
}
