package cn.javaer.snippets.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author cn-src
 */
public class Json {
    public static final Json INSTANCE = new Json(new ObjectMapper());
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
