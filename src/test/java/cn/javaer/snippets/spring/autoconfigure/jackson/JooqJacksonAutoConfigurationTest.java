package cn.javaer.snippets.spring.autoconfigure.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.jooq.JSONB;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author cn-src
 */
class JooqJacksonAutoConfigurationTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    JooqJacksonAutoConfiguration.class, JacksonAutoConfiguration.class));

    @Test
    void auto() throws Exception {
        this.contextRunner.run(context -> {
            final ObjectMapper mapper = context.getBean(ObjectMapper.class);
            final Demo demo = new Demo();
            demo.setStr("val");
            //language=JSON
            demo.setJsonb(JSONB.valueOf("{\"demo\":123}"));
            demo.setLocalDateTime(LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.MIN));
            demo.setLocalDate(LocalDate.of(2020, 1, 1));
            demo.setLocalTime(LocalTime.MIN);
            final String value = mapper.writeValueAsString(demo);
            JSONAssert.assertEquals("{\"str\":\"val\",\"jsonb\":{\"demo\":123}," +
                    "\"localDateTime\":\"2020-01-01 00:00:00\",\"localDate\":\"2020-01-01\"," +
                    "\"localTime\":\"00:00:00\"}", value, true);
        });
    }

    @Data
    static class Demo {
        String str;
        JSONB jsonb;
        LocalDateTime localDateTime;
        LocalDate localDate;
        LocalTime localTime;
    }
}