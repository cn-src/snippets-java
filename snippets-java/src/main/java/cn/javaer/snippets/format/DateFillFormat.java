package cn.javaer.snippets.format;

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
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
public @interface DateFillFormat {

    /**
     * 日期的格式
     */
    String datePattern() default "yyyy-MM-dd";

    /**
     * 日期时间的格式
     */
    String dateTimePattern() default "yyyy-MM-dd HH:mm:ss";

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
     * 在只有日期的时候, 填充时间的方式
     */
    FillTime fillTime();

    enum FillTime {

        /**
         * 填充当天最小时间
         *
         * {@link LocalTime#MIN}
         */
        MIN,

        /**
         * 填充当天最大时间
         *
         * {@link LocalTime#MAX}
         */
        MAX
    }

    interface Conversion {

        /**
         * 计算日期。
         *
         * @param localDate LocalDate
         * @param format DateFillFormat
         */
        static LocalDate computeDate(final LocalDate localDate, final DateFillFormat format) {
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

            return date;
        }

        /**
         * 日期转换日期时间
         *
         * @param localDate LocalDate
         * @param format DateFillFormat
         */
        static LocalDateTime conversion(final LocalDate localDate,
                                        final DateFillFormat format) {
            final LocalDate date = Conversion.computeDate(localDate, format);

            switch (format.fillTime()) {
                case MIN:
                    return date.atTime(LocalTime.MIN);
                case MAX:
                    return date.atTime(LocalTime.MAX);
                default:
                    throw new IllegalStateException();
            }
        }

        /**
         * 日期时间转换日期时间
         *
         * @param localDateTime LocalDateTime
         * @param format DateFillFormat
         */
        static LocalDateTime conversion(final LocalDateTime localDateTime,
                                        final DateFillFormat format) {
            final LocalDate date = Conversion.computeDate(localDateTime.toLocalDate(), format);
            return date.atTime(localDateTime.toLocalTime());
        }
    }
}
