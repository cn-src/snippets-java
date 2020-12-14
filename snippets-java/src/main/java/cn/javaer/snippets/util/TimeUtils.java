package cn.javaer.snippets.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * @author cn-src
 */
public interface TimeUtils {

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * 当月的开始时间，例如：2020-06-01 00:00:00.
     *
     * @return LocalDateTime
     */
    static LocalDateTime monthStart() {
        return YearMonth.now().atDay(1).atStartOfDay();
    }

    /**
     * 当月的结束时间，例如：2020-06-01 23:59:59.999999999.
     *
     * @return LocalDateTime
     */
    static LocalDateTime monthEnd() {
        return YearMonth.now().atEndOfMonth().atTime(LocalTime.MAX);
    }
}
