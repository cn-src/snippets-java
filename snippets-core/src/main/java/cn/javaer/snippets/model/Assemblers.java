package cn.javaer.snippets.model;

import java.util.Collections;

/**
 * @author cn-src
 */
public interface Assemblers {

    /**
     * 序列化 Json 的组合对象.
     *
     * @param <T1> the type parameter
     * @param <T2> the type parameter
     * @param unwrapped1 解包属性的对象1
     * @param unwrapped2 解包属性的对象2
     *
     * @return Assembler
     */
    static <T1, T2> Assembler<T1, T2> of(final T1 unwrapped1,
                                         final T2 unwrapped2) {
        return new Assembler<>(unwrapped1, unwrapped2);
    }

    /**
     * 序列化 Json 的组合对象.
     *
     * @param <T1> the type parameter
     * @param <T2> the type parameter
     * @param unwrapped1 解包属性的对象1
     * @param propName 添加的属性名
     * @param propValue 添加的属性值
     *
     * @return DynamicAssembler
     */
    static <T1, T2> DynamicAssembler<T1, T2> ofDynamic(final T1 unwrapped1,
                                                       final String propName, final T2 propValue) {
        return new DynamicAssembler<>(unwrapped1, Collections.singletonMap(propName, propValue));
    }
}
