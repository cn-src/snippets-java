package cn.javaer.snippets.jooq.field;

import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.TableImpl;

import java.lang.reflect.InvocationTargetException;

/**
 * @author cn-src
 */
interface CustomFieldUtils {
    static void addToFields(final Table<?> table, final Field<?> field) {
        if (table instanceof TableImpl) {
            try {
                final Object fields = TableImpl.class.getDeclaredMethod("fields0").invoke(table);
                fields.getClass().getDeclaredMethod("add", Field.class).invoke(fields, field);
            }
            catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                // TODO
                throw new RuntimeException(e);
            }
        }
    }
}
