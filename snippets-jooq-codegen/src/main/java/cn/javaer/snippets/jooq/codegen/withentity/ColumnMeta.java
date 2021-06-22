package cn.javaer.snippets.jooq.codegen.withentity;

import cn.javaer.snippets.jooq.field.ArrayField;
import cn.javaer.snippets.jooq.field.JsonbField;
import cn.javaer.snippets.util.ReflectionUtils;
import cn.javaer.snippets.util.StrUtils;
import io.github.classgraph.FieldInfo;
import io.github.classgraph.TypeSignature;
import lombok.Value;
import org.jooq.JSONB;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;

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
    boolean customField;
    boolean id;
    boolean updatedBy;
    boolean updatedDate;
    boolean createdBy;
    boolean createdDate;
    String converter;
    String sqlType;
    String columnName;

    String customFieldType;
    String tableFieldName;

    public ColumnMeta(final String fieldName, final String fieldType, final String columnName,
                      final boolean id, final boolean updatedBy, final boolean updatedDate,
                      final boolean createdBy, final boolean createdDate, final boolean readOnly) {
        this.fieldName = fieldName;
        this.getterName = ReflectionUtils.toGetterName(fieldName, fieldType);
        this.fieldType = type(fieldType).replace('$', '.');
        this.sqlType = TypeMapping.get(type(fieldType));
        this.converter = converter(type(fieldType));
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
        this(fieldInfo.getName(), className(fieldInfo),
            StrUtils.defaultIfEmpty(NameUtils.columnValue(fieldInfo),
                StrUtils.toSnakeLower(fieldInfo.getName())),
            fieldInfo.hasAnnotation("org.springframework.data.annotation.Id"),
            fieldInfo.hasAnnotation("org.springframework.data.annotation.LastModifiedBy"),
            fieldInfo.hasAnnotation("org.springframework.data.annotation.LastModifiedDate"),
            fieldInfo.hasAnnotation("org.springframework.data.annotation.CreatedBy"),
            fieldInfo.hasAnnotation("org.springframework.data.annotation.CreatedDate"),
            fieldInfo.hasAnnotation("org.springframework.data.annotation.ReadOnlyProperty")
        );
    }

    public ColumnMeta(final GenColumn genColumn) {
        this(genColumn.field(), genColumn.fieldType().getName(),
            StrUtils.defaultIfEmpty(genColumn.column(),
                StrUtils.toSnakeLower(genColumn.field())),
            false, false, false, false, false, false);
    }

    static String enumConverter(final String fieldType) {
        final String type = fieldType.replace('$', '.');
        return String.format("new org.jooq.impl.EnumConverter<java.lang.String, %s>(java.lang" +
                ".String.class, %s.class)",
            type, type);
    }

    static String className(final FieldInfo fieldInfo) {
        final TypeSignature td = fieldInfo.getTypeDescriptor();
        try {
            final Method getClassName = td.getClass().getDeclaredMethod("getClassName");
            getClassName.setAccessible(true);
            return (String) getClassName.invoke(td);
        }
        catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    static String customFieldType(final String fieldType) {
        if (fieldType.endsWith("[]") && !"byte[]".equals(fieldType)) {
            return ArrayField.class.getName();
        }
        if (JSONB.class.getName().equals(fieldType)) {
            return JsonbField.class.getName();
        }
        return "";
    }

    static boolean isCustomField(final String fieldType) {
        if (fieldType.endsWith("[]") && !"byte[]".equals(fieldType)) {
            return true;
        }
        return "org.jooq.JSONB".equals(fieldType);
    }

    static String converter(final String fieldType) {
        if (CodeGenTool.enums.containsName(fieldType)) {
            return enumConverter(fieldType);
        }
        if (Duration.class.getName().equals(fieldType)) {
            return "cn.javaer.snippets.jooq.DurationConverter.INSTANCE";
        }
        return null;
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