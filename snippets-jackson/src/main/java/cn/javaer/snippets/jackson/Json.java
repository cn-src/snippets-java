package cn.javaer.snippets.jackson;

import cn.javaer.snippets.util.ReflectionUtils;
import cn.javaer.snippets.util.TimeUtils;
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

import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * 基于 jackson 的工具类，与常规的纯静态方法的工具类设计的不同之处在于，可传入不同的 ObjectMapper
 * 来实例化新的工具对象，方便定制扩展，同时提供静态常量实例来方便直接使用。
 *
 * <p>
 * 此工具目前主要是对受检查异常进行转换抛出。
 * </p>
 *
 * @author cn-src
 */
@SuppressWarnings("ALL")
public class Json {

    /**
     * 默认实例
     */
    public static final Json DEFAULT;

    /**
     * 序列化时忽略空对象的实例
     */
    public static final Json NON_EMPTY;

    static {
        // @formatter:off
        final SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(TimeUtils.DATE_TIME_FORMATTER));
        module.addSerializer(LocalDate.class, new LocalDateSerializer(TimeUtils.DATE_FORMATTER));
        module.addSerializer(LocalTime.class, new LocalTimeSerializer(TimeUtils.TIME_FORMATTER));

        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(TimeUtils.DATE_TIME_FORMATTER));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(TimeUtils.DATE_FORMATTER));
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(TimeUtils.TIME_FORMATTER));
        // @formatter:on

        ReflectionUtils.getClass("org.jooq.JSONB").ifPresent(it -> {
            module.addSerializer((Class) it, JooqJsonbSerializer.INSTANCE);
            module.addDeserializer((Class) it, JooqJsonbDeserializer.INSTANCE);
        });
        ReflectionUtils.getClass("org.jooq.Record").ifPresent(it -> {
            module.addSerializer((Class) it, JooqRecordSerializer.INSTANCE);
        });

        final ObjectMapper aDefault = new ObjectMapper();
        aDefault.setAnnotationIntrospector(SnippetsJacksonIntrospector.INSTANCE);
        aDefault.registerModule(module);
        DEFAULT = new Json(aDefault);

        final ObjectMapper nonEmpty = new ObjectMapper();
        nonEmpty.setAnnotationIntrospector(SnippetsJacksonIntrospector.INSTANCE);
        nonEmpty.registerModule(module);
        nonEmpty.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        NON_EMPTY = new Json(nonEmpty);
    }

    private final ObjectMapper objectMapper;

    /**
     * 根据 ObjectMapper 创建新实例。
     *
     * @param objectMapper ObjectMapper
     */
    public Json(final ObjectMapper objectMapper) {
        Objects.requireNonNull(objectMapper);
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
