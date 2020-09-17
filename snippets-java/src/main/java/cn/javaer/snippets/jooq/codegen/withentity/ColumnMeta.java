package cn.javaer.snippets.jooq.codegen.withentity;

import io.github.classgraph.FieldInfo;
import lombok.Value;

/**
 * @author cn-src
 */
@Value
public class ColumnMeta {
    String fieldName;

    String fieldType;

    boolean enumType;

    String enumConverter;

    String sqlType;
    String columnName;
    String tableFieldName;

    public ColumnMeta(final String fieldName, final String fieldType, final String columnName) {
        this.fieldName = fieldName;
        this.fieldType = type(fieldType);
        this.enumType = CodeGenTool.enums.containsName(this.fieldType);
        this.sqlType = this.enumType ? "org.jooq.impl.SQLDataType.VARCHAR" :
            TypeMapping.get(this.fieldType);
        this.enumConverter = this.enumType ? enumConverter(this.fieldType) : "";
        this.columnName = columnName;
        this.tableFieldName = NameUtils.toUcUnderline(this.fieldName);
    }

    public ColumnMeta(final FieldInfo fieldInfo) {
        this(fieldInfo.getName(), fieldInfo.getTypeDescriptor().toString(),
            NameUtils.defaultValue(NameUtils.columnValue(fieldInfo),
                NameUtils.toLcUnderline(fieldInfo.getName()))
        );
    }

    public ColumnMeta(final GenColumn genColumn) {
        this(genColumn.field(), genColumn.fieldType().getName(),
            NameUtils.defaultValue(genColumn.column(),
                NameUtils.toLcUnderline(genColumn.field())));
    }

    static String enumConverter(final String fieldType) {
        return String.format("new org.jooq.impl.EnumConverter<java.lang.String, %s>(java.lang" +
                ".String.class, %s.class)",
            fieldType, fieldType);
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
