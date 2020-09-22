package cn.javaer.snippets.jooq.condition.annotation;

import cn.javaer.snippets.jooq.PGDSL;
import org.assertj.core.util.Arrays;
import org.jooq.Field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.util.function.BiFunction;

/**
 * @author cn-src
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Condition {

    Operator value() default Operator.EQUAL;

    enum Operator {

        /**
         *
         */
        EQUAL(Field::equal),
        CONTAINS(Field::contains),

        CONTAINED_IN(PGDSL::containedIn),

        BETWEEN((field, obj) -> {
            if (!Arrays.isArray(obj) && Array.getLength(obj) != 2) {
                throw new IllegalStateException("'between' must has two params");
            }
            final Object[] objs = (Object[]) obj;
            return field.between(objs[0], objs[1]);
        }),

        GREATER_THAN(Field::greaterThan),
        GREATER_OR_EQUAL(Field::greaterOrEqual),

        LESS_THAN(Field::lessThan),
        LESS_OR_EQUAL(Field::lessOrEqual);

        private final BiFunction<Field<Object>, Object, org.jooq.Condition> function;

        Operator(final BiFunction<Field<Object>, Object, org.jooq.Condition> function) {
            this.function = function;
        }

        public BiFunction<Field<Object>, Object, org.jooq.Condition> getFunction() {

            return this.function;
        }
    }
}
