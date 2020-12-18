package cn.javaer.snippets.jooq;

import lombok.Value;
import org.jooq.Field;

import java.util.function.Function;

/**
 * @author cn-src
 */
@Value
public class ColumnMeta {
    String fieldName;
    Function<Object, Object> readMethod;
    Field<Object> column;
}