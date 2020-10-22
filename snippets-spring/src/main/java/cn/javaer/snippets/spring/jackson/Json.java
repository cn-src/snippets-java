package cn.javaer.snippets.spring.jackson;

import cn.javaer.snippets.jackson.JooqJsonbDeserializer;
import cn.javaer.snippets.jackson.JooqJsonbSerializer;
import cn.javaer.snippets.jackson.JooqRecordSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.jooq.JSONB;
import org.jooq.Record;

import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author cn-src
 */
public class Json {
    public static final Json DEFAULT;
    public static final Json NON_EMPTY;

    static {
        final ObjectMapper defaultX = new ObjectMapper();
        defaultX.setAnnotationIntrospector(SnippetsJacksonIntrospector.INSTANCE);
        final SimpleModule module = new SimpleModule();

        final DateTimeFormatter dataTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dataTimeFormat));
        module.addSerializer(LocalDate.class, new LocalDateSerializer(dataFormat));
        module.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormat));
        // TODO 判断是否 JSONB 存在
        module.addSerializer(JSONB.class, JooqJsonbSerializer.INSTANCE);
        module.addSerializer(Record.class, JooqRecordSerializer.INSTANCE);

        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dataTimeFormat));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(dataFormat));
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormat));
        // TODO
        module.addDeserializer(JSONB.class, JooqJsonbDeserializer.INSTANCE);

        defaultX.registerModule(module);
        DEFAULT = new Json(defaultX);

        final ObjectMapper nonEmpty = new ObjectMapper();
        nonEmpty.setAnnotationIntrospector(SnippetsJacksonIntrospector.INSTANCE);
        nonEmpty.registerModule(module);
        nonEmpty.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        NON_EMPTY = new Json(nonEmpty);
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
            throw new UncheckedIOException(e);
        }
    }

    public <T> T read(final String json, final Class<T> clazz) {
        try {
            return this.objectMapper.readValue(json, clazz);
        }
        catch (final JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public <T> T read(final String json, final TypeReference<T> valueTypeRef) {
        try {
            return this.objectMapper.readValue(json, valueTypeRef);
        }
        catch (final JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public JsonNode read(final String json) {
        try {
            return this.objectMapper.readTree(json);
        }
        catch (final JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }
}
