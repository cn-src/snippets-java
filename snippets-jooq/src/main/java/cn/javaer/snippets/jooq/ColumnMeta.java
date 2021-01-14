package cn.javaer.snippets.jooq;

import lombok.Value;
import org.jooq.Field;

import java.util.function.Function;

/**
 * @author cn-src
 */
@Value
public class ColumnMeta<T, F> {
    Function<T, F> readMethod;
    Field<F> column;
}