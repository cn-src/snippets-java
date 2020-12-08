package cn.javaer.snippets.util;

import java.util.Optional;

/**
 * @author cn-src
 */
public interface ReflectionUtils {

    static boolean isPresent(final String className) {
        try {
            Class.forName(className);
            return true;
        }
        catch (final ClassNotFoundException ex) {
            return false;
        }
    }

    static Optional<Class<?>> getClass(final String className) {
        try {
            return Optional.of(Class.forName(className));
        }
        catch (final ClassNotFoundException e) {
            return Optional.empty();
        }
    }
}
