package cn.javaer.snippets.jooq.condition.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被包含.
 *
 * @author cn-src
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Condition
public @interface ConditionContained {
}
