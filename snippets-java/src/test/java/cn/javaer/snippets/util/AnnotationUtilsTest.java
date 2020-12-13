package cn.javaer.snippets.util;

import cn.javaer.snippets.format.DateMinTime;
import cn.javaer.snippets.format.DateTimeFormat;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author cn-src
 */
class AnnotationUtilsTest {
    @Test
    void name() throws Exception {
        final DateMinTime annotation =
            Demo.class.getDeclaredField("dateTime").getAnnotation(DateMinTime.class);
        final Optional<DateTimeFormat> format =
            AnnotationUtils.mergeAnnotations(DateTimeFormat.class, annotation);
        Assertions.assertThat(format).get().extracting(DateTimeFormat::datePattern)
            .isEqualTo("yyyy/MM/dd");
    }

    static class Demo {
        @DateMinTime(datePattern = "yyyy/MM/dd")
        LocalDateTime dateTime;
    }
}