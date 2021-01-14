package cn.javaer.snippets.jooq.condition.annotation;

import cn.javaer.snippets.jooq.PGDSL;
import org.jooq.Field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
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
        CONTAINS(PGDSL::contains),

        CONTAINED_IN(PGDSL::containedIn),

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
