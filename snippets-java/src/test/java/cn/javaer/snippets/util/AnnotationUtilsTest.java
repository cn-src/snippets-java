package cn.javaer.snippets.util;

import cn.javaer.snippets.format.DateFillFormat;
import cn.javaer.snippets.format.DateMinTime;
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
        final Optional<DateFillFormat> format =
            AnnotationUtils.mergeAnnotations(DateFillFormat.class, annotation);
        Assertions.assertThat(format).get().extracting(DateFillFormat::datePattern)
            .isEqualTo("yyyy/MM/dd");
    }

    static class Demo {
        @DateMinTime(datePattern = "yyyy/MM/dd")
        LocalDateTime dateTime;
    }
}