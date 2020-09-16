package cn.javaer.snippets.spring;

import cn.javaer.snippets.spring.format.DateFillFormat;
import cn.javaer.snippets.spring.format.DateMinTime;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(format).get().extracting(DateFillFormat::datePattern)
            .isEqualTo("yyyy/MM/dd");
    }

    static class Demo {
        @DateMinTime(datePattern = "yyyy/MM/dd")
        LocalDateTime dateTime;
    }
}