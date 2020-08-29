package cn.javaer.snippets.spring.format;

import cn.javaer.snippets.jackson.SnippetsJacksonIntrospector;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于输入的时间只有日期没有时间，但需要填补时间部分的场景，填充最小时间。
 *
 * @author cn-src
 * @see SnippetsJacksonIntrospector
 * @see DateFillFormatter
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@DateFillFormat(fillTime = DateFillFormat.FillTime.MIN)
public @interface DateMinTime {

    /**
     * 日期的格式，输入。
     */
    String datePattern() default "yyyy-MM-dd";

    /**
     * 日期时间的格式，输出。
     */
    String dateTimePattern() default "yyyy-MM-dd HH:mm:ss";
}
