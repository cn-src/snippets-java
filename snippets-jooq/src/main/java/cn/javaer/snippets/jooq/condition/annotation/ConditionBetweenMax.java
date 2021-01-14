package cn.javaer.snippets.jooq.condition.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author cn-src
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@BiCondition(valueType = BiCondition.ValueType.MAX)
public @interface ConditionBetweenMax {

    /**
     * Between 的表列名，驼峰式自动转换成下划线。
     *
     * @return Between 的表列名
     */
    @AliasFor(attribute = "column", annotation = BiCondition.class)
    String value() default "";

    /**
     * @see #value()
     */
    @AliasFor(attribute = "column", annotation = BiCondition.class)
    String column() default "";
}
