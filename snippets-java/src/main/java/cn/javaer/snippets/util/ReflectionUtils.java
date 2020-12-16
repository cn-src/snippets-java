package cn.javaer.snippets.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;

/**
 * 反射相关的工具类.
 *
 * @author cn -src
 */
public interface ReflectionUtils {

    /**
     * 判断 class 是否存在.
     *
     * @param className the class name
     *
     * @return 返回 true 如果存在
     */
    static boolean isPresent(final String className) {
        try {
            Class.forName(className);
            return true;
        }
        catch (final ClassNotFoundException ex) {
            return false;
        }
    }

    /**
     * 获取 class.
     *
     * @param className the class name
     *
     * @return the class
     */
    static Optional<Class<?>> getClass(final String className) {
        try {
            return Optional.of(Class.forName(className));
        }
        catch (final ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    /**
     * 获取继承自父类的泛型类型.
     *
     * @param clazz 需要继承带泛型的父类，并且此类需要泛型实参
     *
     * @return 泛型类型
     */
    static Class<?>[] getSuperclassGenerics(final Class<?> clazz) {
        final Type type = clazz.getGenericSuperclass();
        final ParameterizedType pType = (ParameterizedType) type;
        return Arrays.stream(pType.getActualTypeArguments())
            .map(Class.class::cast).toArray(Class[]::new);
    }
}
