package cn.javaer.snippets.jackson;

import cn.javaer.snippets.spring.format.DateFillFormat;
import cn.javaer.snippets.spring.format.DateMaxTime;
import cn.javaer.snippets.spring.format.DateMinTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cn-src
 */
class DateFillDeserializerTest {

    @Test
    void deserialize() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        //language=JSON
        final Demo demo = objectMapper.readValue("{\"dateTime\": \"2020-05-05\",\"dateMinTime\": " +
            "\"2020/02/05\",\"dateMaxTime\": \"2020/03/05\"}", Demo.class);
        assertThat(demo.dateTime).isEqualTo(LocalDate.parse("2020-05-04").atTime(LocalTime.MIN));
        assertThat(demo.dateMinTime).isEqualTo(LocalDate.parse("2020-02-05").atTime(LocalTime.MIN));
        assertThat(demo.dateMaxTime).isEqualTo(LocalDate.parse("2020-03-05").atTime(LocalTime.MAX));
    }

    @Data
    static class Demo {
        @DateFillFormat(fillTime = DateFillFormat.FillTime.MIN, days = -1)
        @JsonDeserialize(using = DateFillDeserializer.class)
        LocalDateTime dateTime;

        @DateMinTime(datePattern = "yyyy/MM/dd")
        @JsonDeserialize(using = DateFillDeserializer.class)
        LocalDateTime dateMinTime;

        @DateMaxTime(datePattern = "yyyy/MM/dd")
        @JsonDeserialize(using = DateFillDeserializer.class)
        LocalDateTime dateMaxTime;
    }
}