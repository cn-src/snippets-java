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

    public static Condition arrayContained(Field<String[]> arrayField, String[] arrayValue) {
        return DSL.condition("{0} <@ {1}", arrayField,
                DSL.val(arrayValue, arrayField.getDataType()));
    }

    @Deprecated
    public static Condition jsonbContains(Field<JSONB> jsonField, String jsonKey, Object jsonValue) {
        String json = JSONValue.toJSONString(Collections.singletonMap(jsonKey, jsonValue));
        return DSL.condition("{0}::jsonb @> {1}::jsonb", jsonField,
                DSL.val(json, jsonField.getDataType()));
    }

    @Deprecated
    public static Condition jsonbContains(Field<JSONB> jsonField, JSONB jsonb) {
        return DSL.condition("{0}::jsonb @> {1}::jsonb", jsonField,
                DSL.val(jsonb, jsonField.getDataType()));
    }

    @Support(SQLDialect.POSTGRES)
    public static Field<Boolean> stContains(Field<Geometry> geomA, Field<Geometry> geomB) {
        return DSL.function("ST_Contains", Boolean.TYPE, geomA, geomB);
    }

    @Support(SQLDialect.POSTGRES)
    public static Field<String> stAsGeoJSON(Field<Geometry> geom) {
        return DSL.function("ST_AsGeoJSON", String.class, geom);
    }
}
