package cn.javaer.snippets.jooq.codegen.withentity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cn-src
 */
public class TypeMapping {
    private final static Map<String, String> MAPPING;

    static {
        final Map<String, String> mapping = new HashMap<>();
        mapping.put("java.lang.String", "org.jooq.impl.SQLDataType.VARCHAR");

        mapping.put("java.lang.Boolean", "org.jooq.impl.SQLDataType.BOOLEAN");

        mapping.put("java.lang.Short", "org.jooq.impl.SQLDataType.SMALLINT");
        mapping.put("java.lang.Integer", "org.jooq.impl.SQLDataType.INTEGER");
        mapping.put("java.lang.Long", "org.jooq.impl.SQLDataType.BIGINT");
        mapping.put("java.lang.Float", "org.jooq.impl.SQLDataType.REAL");
        mapping.put("java.lang.Double", "org.jooq.impl.SQLDataType.DOUBLE");
        mapping.put("java.math.BigInteger", "org.jooq.impl.SQLDataType.DECIMAL_INTEGER");
        mapping.put("java.math.BigDecimal", "org.jooq.impl.SQLDataType.DECIMAL");

        mapping.put("java.time.LocalDate", "org.jooq.impl.SQLDataType.LOCALDATE");
        mapping.put("java.time.LocalTime", "org.jooq.impl.SQLDataType.LOCALTIME");
        mapping.put("java.time.LocalDateTime", "org.jooq.impl.SQLDataType.LOCALDATETIME");
        mapping.put("java.time.OffsetTime", "org.jooq.impl.SQLDataType.OFFSETTIME");
        mapping.put("java.time.OffsetDateTime", "org.jooq.impl.SQLDataType.OFFSETDATETIME");
        mapping.put("java.time.Instant", "org.jooq.impl.SQLDataType.INSTANT");

        mapping.put("java.util.UUID", "org.jooq.impl.SQLDataType.UUID");

        mapping.put("org.jooq.JSON", "org.jooq.impl.SQLDataType.JSON");
        mapping.put("org.jooq.JSONB", "org.jooq.impl.SQLDataType.JSONB");
        mapping.put("cn.javaer.snippets.type.Geometry",
            "cn.javaer.snippets.jooq.PGDSL.GEOMETRY_TYPE");
        MAPPING = Collections.unmodifiableMap(mapping);
    }

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static String get(final String name) {
        if (name.endsWith("[]")) {
            final String arrayName = name.substring(0, name.length() - 2);
            if ("byte".equals(arrayName)) {
                return "org.jooq.impl.SQLDataType.BINARY";
            }
            return MAPPING.get(arrayName) + ".getArrayDataType()";
        }
        return MAPPING.get(name);
    }
}
