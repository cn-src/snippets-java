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

    public ColumnMeta(final FieldInfo fieldInfo) {
        this.fieldName = fieldInfo.getName();
        this.fieldType = fieldInfo.getTypeDescriptor().toString();
        this.enumType = CodeGenTool.enums.containsName(this.fieldType);
        this.sqlType = this.enumType ? "org.jooq.impl.SQLDataType.VARCHAR" :
            TypeMapping.get(this.fieldType);
        this.enumConverter = this.enumType ? enumConverter(this.fieldType) : "";
        final String columnValue = NameUtils.columnValue(fieldInfo);
        this.columnName = columnValue.isEmpty() ?
            NameUtils.toLcUnderline(fieldInfo.getName()) : columnValue;
        this.tableFieldName = NameUtils.toUcUnderline(this.fieldName);
    }

    static String enumConverter(final String fieldType) {
        return String.format("new org.jooq.impl.EnumConverter<java.lang.String, %s>(java.lang" +
                ".String.class, %s.class)",
            fieldType, fieldType);
    }
}
