package cn.javaer.snippets.jooq.codegen.withentity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于额外生成 jOOQ 字段信息，这些字段可不再实体类中用字段声明。
 *
 * @author cn-src
 */
@Documented
@Repeatable(value = GenColumns.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface GenColumn {

    /**
     * 字段名
     */
    String field();

    /**
     * 字段类型
     */
    Class<?> fieldType();

    /**
     * 列名，默认是 field 字段名转下划线格式
     */
    String column() default "";
}
