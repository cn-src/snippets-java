package cn.javaer.snippets.spring.format;

import cn.javaer.snippets.format.DateMaxTime;
import cn.javaer.snippets.format.DateTimeFormat;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cn-src
 */
public class DateMaxTimeTest {

    @Test
    void name() throws Exception {

        final Field dateTime = Demo.class.getDeclaredField("dateTime");
        final DateTimeFormat annotation = AnnotatedElementUtils.findMergedAnnotation(dateTime,
            DateTimeFormat.class);
        assertThat(Objects.requireNonNull(annotation).time())
            .isEqualTo(DateTimeFormat.Time.MAX);
        assertThat(annotation.datePattern()).isEqualTo("yyyy/MM/dd");
    }

    static class Demo {
        @DateMaxTime(datePattern = "yyyy/MM/dd")
        LocalDateTime dateTime;
    }
}
