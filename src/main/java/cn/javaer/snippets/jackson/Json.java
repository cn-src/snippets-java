package cn.javaer.snippets.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.jooq.JSONB;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author cn-src
 */
public class Json {
    public static final Json INSTANCE;

    static {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(SnippetsJacksonIntrospector.INSTANCE);
        final SimpleModule module = new SimpleModule();

        final DateTimeFormatter dataTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dataTimeFormat));
        module.addSerializer(LocalDate.class, new LocalDateSerializer(dataFormat));
        module.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormat));
        module.addSerializer(JSONB.class, JooqJsonbSerializer.INSTANCE);

        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dataTimeFormat));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(dataFormat));
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormat));

        mapper.registerModule(module);
        INSTANCE = new Json(mapper);
    }

    private final ObjectMapper objectMapper;

    public Json(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String write(final Object obj) {
        try {
            return this.objectMapper.writeValueAsString(obj);
        }
        catch (final JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public <T> T read(final String json, final Class<T> clazz) {
        try {
            return this.objectMapper.readValue(json, clazz);
        }
        catch (final JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
