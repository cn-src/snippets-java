package cn.javaer.snippets.jackson;

import cn.javaer.snippets.spring.format.DateFillFormat;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Value;
import org.junit.jupiter.api.Test;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cn-src
 */
class SnippetsJacksonIntrospectorTest {
    @Test
    void jacksonIntrospector() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final SnippetsJacksonIntrospector jacksonIntrospector = new SnippetsJacksonIntrospector();
        objectMapper.setAnnotationIntrospector(jacksonIntrospector);
        //language=JSON
        final Demo demo = objectMapper.readValue("{\"dateTime\": \"2020-05-05\"}", Demo.class);
        assertThat(demo.dateTime).isEqualTo(LocalDate.parse("2020-04-05").atTime(LocalTime.MIN));
    }

    @Test
    void jacksonIntrospectorValue() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final SnippetsJacksonIntrospector jacksonIntrospector = new SnippetsJacksonIntrospector();
        objectMapper.setAnnotationIntrospector(jacksonIntrospector);
        //language=JSON
        final Demo1 demo = objectMapper.readValue("{\"dateTime\": \"2020-05-05\"}", Demo1.class);
        assertThat(demo.dateTime).isEqualTo(LocalDate.parse("2020-03-05").atTime(LocalTime.MIN));
    }

    @Data
    static class Demo {
        @DateFillFormat(fillTime = DateFillFormat.FillTime.MIN, months = -1)
        LocalDateTime dateTime;
    }

    @Value
    static class Demo1 {
        LocalDateTime dateTime;

        @JsonCreator
        @ConstructorProperties("dateTime")
        public Demo1(@DateFillFormat(fillTime = DateFillFormat.FillTime.MIN, months = -2) final LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }
    }
}