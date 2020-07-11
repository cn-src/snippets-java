package cn.javaer.snippets.spring.format;

import cn.javaer.snippets.jackson.SnippetsJacksonIntrospector;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 适用于输入的时间只有日期没有时间，但需要填补时间部分的场景，同时也可以按天数，周数和月数偏移日期。
 *
 * @author cn-src
 * @see SnippetsJacksonIntrospector
 * @see DateFillFormatter
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
public @interface DateFillFormat {

    String style() default "SS";

    /**
     * 输入/反序列化的格式
     */
    String pattern() default "yyyy-MM-dd";

    /**
     * 输出/序列化的格式
     */
    String printPattern() default "yyyy-MM-dd HH:mm:ss";

    /**
     * 在已有的日期上偏移天数
     */
    int days() default 0;

    /**
     * 在已有的日期上偏移周数
     */
    int weeks() default 0;

    /**
     * 在已有的日期上偏移月数
     */
    int months() default 0;

    /**
     * 填充时间的方式
     */
    FillTime fillTime();

    enum FillTime {

        /**
         * 填充当天最小时间
         *
         * {@link java.time.LocalTime#MIN}
         */
        MIN,

        /**
         * 填充当天最大时间
         *
         * {@link java.time.LocalTime#MAX}
         */
        MAX
    }

    interface Conversion {

        /**
         * 转换日期
         */
        static LocalDateTime conversion(final LocalDate localDate, final DateFillFormat format) {
            LocalDate date = localDate;
            final int days = format.days();
            if (days < 0) {
                date = date.minusDays(-days);
            }
            else if (days > 0) {
                date = date.plusDays(days);
            }
            final int months = format.months();
            if (months < 0) {
                date = date.minusMonths(-months);
            }
            else if (months > 0) {
                date = date.plusMonths(months);
            }
            final int weeks = format.weeks();
            if (weeks < 0) {
                date = date.minusWeeks(-weeks);
            }
            else if (weeks > 0) {
                date = date.plusWeeks(weeks);
            }
            switch (format.fillTime()) {
                case MIN:
                    return date.atTime(LocalTime.MIN);
                case MAX:
                    return date.atTime(LocalTime.MAX);
                default:
                    throw new IllegalStateException();
            }
        }
    }
}
