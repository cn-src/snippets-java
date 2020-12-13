package cn.javaer.snippets.util;

import java.util.Optional;

/**
 * @author cn-src
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
}
