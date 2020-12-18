package cn.javaer.snippets.jooq;

import lombok.Value;
import org.jooq.Field;

/**
 * @author cn-src
 */
@Value
public class ColumnMeta {
    String fieldName;
    String getterName;
    Field<Object> column;
}