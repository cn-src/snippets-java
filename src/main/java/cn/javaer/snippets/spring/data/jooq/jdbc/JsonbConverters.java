package cn.javaer.snippets.spring.data.jooq.jdbc;

import cn.javaer.snippets.jackson.Json;
import com.fasterxml.jackson.databind.JsonNode;
import org.jooq.JSONB;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author cn-src
 */
public class JsonbConverters {
    private static final List<Converter<?, ?>> CONVERTERS = new ArrayList<>();

    static {
        JsonbConverters.CONVERTERS.add(ToJsonbConverter.INSTANCE);
        JsonbConverters.CONVERTERS.add(JsonbToConverter.INSTANCE);
        JsonbConverters.CONVERTERS.add(ToJsonNodeConverter.INSTANCE);
        JsonbConverters.CONVERTERS.add(JsonNodeToConverter.INSTANCE);
    }

    public static List<Converter<?, ?>> getConvertersToRegister() {
        return Collections.unmodifiableList(JsonbConverters.CONVERTERS);
    }

    @ReadingConverter
    public enum ToJsonbConverter implements Converter<PGobject, JSONB> {

        /**
         * 单实例.
         */
        INSTANCE;

        @Override
        public JSONB convert(final PGobject source) {
            return JSONB.valueOf(source.getValue());
        }
    }

    @WritingConverter
    public enum JsonbToConverter implements Converter<JSONB, PGobject> {

        /**
         * 单实例.
         */
        INSTANCE;

        @Override
        public PGobject convert(final JSONB source) {
            final PGobject obj = new PGobject();
            obj.setType("jsonb");
            try {
                obj.setValue(source.data());
            }
            catch (final SQLException e) {
                throw new IllegalStateException(e);
            }
            return obj;
        }
    }

    @ReadingConverter
    public enum ToJsonNodeConverter implements Converter<PGobject, JsonNode> {

        /**
         * 单实例.
         */
        INSTANCE;

        @Override
        public JsonNode convert(final PGobject source) {
            return Json.INSTANCE.read(source.getValue());
        }
    }

    @WritingConverter
    public enum JsonNodeToConverter implements Converter<JsonNode, PGobject> {

        /**
         * 单实例.
         */
        INSTANCE;

        @Override
        public PGobject convert(@NonNull final JsonNode source) {
            try {
                final String json = Json.INSTANCE.write(source);
                final PGobject obj = new PGobject();
                obj.setType("jsonb");
                obj.setValue(json);
                return obj;
            }
            catch (final SQLException e) {
                throw new TypeMismatchDataAccessException("Not json format", e);
            }
        }
    }
}
