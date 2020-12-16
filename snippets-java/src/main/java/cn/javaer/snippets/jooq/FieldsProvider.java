package cn.javaer.snippets.jooq;

import org.jooq.Field;

/**
 * @author cn-src
 */
public interface FieldsProvider {
    Field<?>[] declaredFields();
}
