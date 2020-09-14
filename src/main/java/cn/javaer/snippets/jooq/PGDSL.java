package cn.javaer.snippets.jooq;

import cn.javaer.snippets.jooq.field.JsonbField;
import cn.javaer.snippets.type.Geometry;
import org.jooq.Condition;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Support;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.tools.json.JSONValue;
import org.jooq.util.postgres.PostgresDSL;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author cn-src
 */
public class PGDSL extends PostgresDSL {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static DataType<Geometry> GEOMETRY_TYPE =
        SQLDataType.OTHER.asConvertedDataType((Converter) PostGISGeometryConverter.INSTANCE);

    protected PGDSL() {
    }

    /**
     * 被包含，操作符: <@
     */
    @Support(SQLDialect.POSTGRES)
    public static <T> Condition containedIn(final Field<T> field, final T value) {
        return DSL.condition("{0} <@ {1}", field,
            DSL.val(value, field.getDataType()));
    }

    @Support(SQLDialect.POSTGRES)
    public static Condition jsonbContains(final Field<JSONB> jsonField, final String jsonKey,
                                          final Object jsonValue) {
        final String json = JSONValue.toJSONString(Collections.singletonMap(jsonKey, jsonValue));
        return DSL.condition("{0}::jsonb @> {1}::jsonb", jsonField,
            DSL.val(json, jsonField.getDataType()));
    }

    @Support(SQLDialect.POSTGRES)
    public static Condition jsonbContains(final Field<JSONB> jsonField, final JSONB jsonb) {
        return DSL.condition("{0}::jsonb @> {1}::jsonb", jsonField,
            DSL.val(jsonb, jsonField.getDataType()));
    }

    @Support(SQLDialect.POSTGRES)
    public static JsonbField<Record, JSONB> jsonbObjectAgg(final Field<?> keyField,
                                                           final Field<?> valueField) {
        return new JsonbField<>("jsonb_object_agg", SQLDataType.JSONB, keyField, valueField);
    }

    @Support(SQLDialect.POSTGRES)
    public static JsonbField<Record, JSONB> jsonbObjectAgg(final Field<?>[] keyFields,
                                                           final char keySeparator,
                                                           final Field<?> valueField) {

        final Field<?> field =
            Arrays.stream(keyFields).reduce((f1, f2) -> f1.concat(DSL.inline(keySeparator),
                f2)).orElse(null);
        return new JsonbField<>("jsonb_object_agg", SQLDataType.JSONB, field, valueField);
    }

    @Support(SQLDialect.POSTGRES)
    public static Field<String> toChar(final Field<LocalDateTime> timestamp,
                                       final String pattern) {
        return DSL.function("to_char", SQLDataType.LONGVARCHAR, timestamp, DSL.inline(pattern));
    }

    /**
     * 自定义聚合函数: first, 取每组中第一个元素.
     * <p>
     * <code>
     * CREATE OR REPLACE FUNCTION public.first_agg (anyelement, anyelement)
     * RETURNS anyelement
     * LANGUAGE sql IMMUTABLE STRICT AS
     * 'SELECT $1;'
     *
     * CREATE AGGREGATE public.first(anyelement) (
     * SFUNC = public.first_agg,
     * STYPE = anyelement
     * );
     * </code>
     *
     * @param field field
     * @param <T> field type
     *
     * @return field
     */
    @Support(SQLDialect.POSTGRES)
    public static <T> Field<T> first(final Field<T> field) {
        return DSL.function("first", field.getDataType(), field);
    }

    @Support(SQLDialect.POSTGRES)
    public static Field<Boolean> stContains(final Field<Geometry> geomA,
                                            final Field<Geometry> geomB) {
        return DSL.function("ST_Contains", SQLDataType.BOOLEAN, geomA, geomB);
    }

    @Support(SQLDialect.POSTGRES)
    public static Field<String> stAsGeoJSON(final Field<Geometry> geom) {
        return DSL.function("ST_AsGeoJSON", SQLDataType.LONGVARCHAR, geom);
    }
}