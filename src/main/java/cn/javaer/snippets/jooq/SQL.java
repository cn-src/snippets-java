package cn.javaer.snippets.jooq;

import cn.javaer.snippets.type.Geometry;
import org.jooq.Condition;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.SQLDialect;
import org.jooq.Support;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.tools.json.JSONValue;

import java.util.Collections;

/**
 * @author cn-src
 */
public class SQL {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static DataType<Geometry> GEOMETRY_TYPE = SQLDataType.OTHER.asConvertedDataType((Converter) PostGISGeometryConverter.INSTANCE);

    private SQL() {
    }

    /**
     * 被包含，操作符: <@
     */
    @Support(SQLDialect.POSTGRES)
    public static <T> Condition containedBy(final Field<T> field, final T value) {
        return DSL.condition("{0} <@ {1}", field,
                DSL.val(value, field.getDataType()));
    }

    @Support(SQLDialect.POSTGRES)
    public static Condition containsJsonb(final Field<JSONB> jsonField, final String jsonKey, final Object jsonValue) {
        final String json = JSONValue.toJSONString(Collections.singletonMap(jsonKey, jsonValue));
        return DSL.condition("{0}::jsonb @> {1}::jsonb", jsonField,
                DSL.val(json, jsonField.getDataType()));
    }

    @Support(SQLDialect.POSTGRES)
    public static Condition containsJsonb(final Field<JSONB> jsonField, final JSONB jsonb) {
        return DSL.condition("{0}::jsonb @> {1}::jsonb", jsonField,
                DSL.val(jsonb, jsonField.getDataType()));
    }

    @Support(SQLDialect.POSTGRES)
    public static Field<Boolean> stContains(final Field<Geometry> geomA, final Field<Geometry> geomB) {
        return DSL.function("ST_Contains", Boolean.TYPE, geomA, geomB);
    }

    @Support(SQLDialect.POSTGRES)
    public static Field<String> stAsGeoJSON(final Field<Geometry> geom) {
        return DSL.function("ST_AsGeoJSON", String.class, geom);
    }
}
