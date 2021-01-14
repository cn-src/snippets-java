package cn.javaer.snippets.jooq.condition.annotation;

import cn.javaer.snippets.util.function.Function3;
import org.jooq.Condition;
import org.jooq.Field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author cn-src
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BiCondition {

    Operator operator() default Operator.BETWEEN;

    String column() default "";

    ValueType valueType();

    enum ValueType {
        /**
         *
         */
        MIN, MAX
    }

    enum Operator {

        /**
         *
         */

        BETWEEN(Field::between);

        private final Function3<Field<Object>, Object, Object, Condition> function;

        Operator(final Function3<Field<Object>, Object, Object, Condition> function) {
            this.function = function;
        }

        public Function3<Field<Object>, Object, Object, Condition> getFunction() {
            return this.function;
        }
    }
}
