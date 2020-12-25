package cn.javaer.snippets.jooq.codegen.withentity;

import cn.javaer.snippets.jooq.ExcludeSelect;
import cn.javaer.snippets.util.ReflectionUtils;
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
    String getterName;
    String fieldType;
    boolean readOnly;
    boolean enumType;
    boolean customField;
    boolean id;
    boolean updatedBy;
    boolean updatedDate;
    boolean createdBy;
    boolean createdDate;
    String enumConverter;
    String sqlType;
    String columnName;

    String customFieldType;
    String tableFieldName;

    public ColumnMeta(final String fieldName, final String fieldType, final String columnName,
                      final boolean id, final boolean updatedBy, final boolean updatedDate,
                      final boolean createdBy, final boolean createdDate, final boolean readOnly) {
        this.fieldName = fieldName;
        this.getterName = ReflectionUtils.toGetterName(fieldName, fieldType);
        this.fieldType = type(fieldType);
        this.enumType = CodeGenTool.enums.containsName(this.fieldType);
        this.sqlType = this.enumType ? "org.jooq.impl.SQLDataType.VARCHAR" :
            TypeMapping.get(this.fieldType);
        this.enumConverter = this.enumType ? enumConverter(this.fieldType) : "";
        this.columnName = columnName;
        this.tableFieldName = StrUtils.toSnakeUpper(this.fieldName);
        this.customField = isCustomField(fieldType);
        this.customFieldType = customFieldType(fieldType);
        this.id = id;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.readOnly = readOnly;
    }

    public ColumnMeta(final String fieldName, final String fieldType, final String columnName) {
        this(fieldName, fieldType, columnName, false, false, false, false, false, false);
    }

    public ColumnMeta(final FieldInfo fieldInfo) {
        this(fieldInfo.getName(), fieldInfo.getTypeDescriptor().toString(),
            StrUtils.defaultIfEmpty(NameUtils.columnValue(fieldInfo),
                StrUtils.toSnakeLower(fieldInfo.getName())),
            fieldInfo.hasAnnotation("org.springframework.data.annotation.Id"),
            fieldInfo.hasAnnotation("org.springframework.data.annotation.LastModifiedBy"),
            fieldInfo.hasAnnotation("org.springframework.data.annotation.LastModifiedDate"),
            fieldInfo.hasAnnotation("org.springframework.data.annotation.CreatedBy"),
            fieldInfo.hasAnnotation("org.springframework.data.annotation.CreatedDate"),
            fieldInfo.hasAnnotation("org.springframework.data.annotation.ReadOnlyProperty") ||
                // TODO
                fieldInfo.hasAnnotation(ExcludeSelect.class.getName())
        );
    }

    public ColumnMeta(final GenColumn genColumn) {
        this(genColumn.field(), genColumn.fieldType().getName(),
            StrUtils.defaultIfEmpty(genColumn.column(),
                StrUtils.toSnakeLower(genColumn.field())),
            false, false, false, false, false, false);
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
