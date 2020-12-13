package cn.javaer.snippets.jooq.codegen.withentity;

import cn.javaer.snippets.util.StrUtils;
import io.github.classgraph.FieldInfo;
import lombok.Value;

/**
 * @author cn-src
 */
@SuppressWarnings("AlibabaUndefineMagicConstant")
@Value
public class ColumnMeta {
    String fieldName;
    String fieldType;
    boolean enumType;
    boolean customField;
    String enumConverter;
    String sqlType;
    String columnName;

    String customFieldType;
    String tableFieldName;

    public ColumnMeta(final String fieldName, final String fieldType, final String columnName) {
        this.fieldName = fieldName;
        this.fieldType = type(fieldType);
        this.enumType = CodeGenTool.enums.containsName(this.fieldType);
        this.sqlType = this.enumType ? "org.jooq.impl.SQLDataType.VARCHAR" :
            TypeMapping.get(this.fieldType);
        this.enumConverter = this.enumType ? enumConverter(this.fieldType) : "";
        this.columnName = columnName;
        this.tableFieldName = StrUtils.toSnakeUpper(this.fieldName);
        this.customField = isCustomField(fieldType);
        this.customFieldType = customFieldType(fieldType);
    }

    public ColumnMeta(final FieldInfo fieldInfo) {
        this(fieldInfo.getName(), fieldInfo.getTypeDescriptor().toString(),
            StrUtils.defaultEmpty(NameUtils.columnValue(fieldInfo),
                StrUtils.toSnakeLower(fieldInfo.getName()))
        );
    }

    public ColumnMeta(final GenColumn genColumn) {
        this(genColumn.field(), genColumn.fieldType().getName(),
            StrUtils.defaultEmpty(genColumn.column(),
                StrUtils.toSnakeLower(genColumn.field())));
    }

    static String enumConverter(final String fieldType) {
        return String.format("new org.jooq.impl.EnumConverter<java.lang.String, %s>(java.lang" +
                ".String.class, %s.class)",
            fieldType, fieldType);
    }

    static String customFieldType(final String fieldType) {
        if (fieldType.endsWith("[]") && !"byte[]".equals(fieldType)) {
            return "cn.javaer.snippets.jooq.field.ArrayField";
        }
        if ("org.jooq.JSONB".equals(fieldType)) {
            return "cn.javaer.snippets.jooq.field.JsonbField";
        }
        return "";
    }

    static boolean isCustomField(final String fieldType) {
        if (fieldType.endsWith("[]") && !"byte[]".equals(fieldType)) {
            return true;
        }
        return "org.jooq.JSONB".equals(fieldType);
    }

    static String type(final String type) {
        switch (type) {
            case "short":
                return Short.class.getName();
            case "int":
                return Integer.class.getName();
            case "long":
                return Long.class.getName();
            case "boolean":
                return Boolean.class.getName();
            case "float":
                return Float.class.getName();
            case "double":
                return Double.class.getName();
            default:
                return type;
        }
    }
}
