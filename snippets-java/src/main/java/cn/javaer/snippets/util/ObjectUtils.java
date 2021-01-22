package cn.javaer.snippets.util;

import java.util.Collection;

/**
 * @author cn-src
 */
public interface ObjectUtils {
    
    static boolean isEmpty(final Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    static boolean isNotEmpty(final Collection<?> coll) {
        return !isEmpty(coll);
    }

    static <T extends CharSequence> T notEmpty(final T chars) {
        if (chars == null || chars.length() == 0) {
            throw new IllegalArgumentException("The validated character sequence is empty");
        }
        return chars;
    }
}
