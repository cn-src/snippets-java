package cn.javaer.snippets.jooq.codegen.withentity;

import io.github.classgraph.AnnotationClassRef;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.AnnotationInfoList;
import io.github.classgraph.AnnotationParameterValueList;
import io.github.classgraph.ClassInfo;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cn-src
 */
@Value
public class TableMeta {
    String generatedPackage;
    String tableName;
    String entityName;
    String staticFieldName;
    List<ColumnMeta> columnMetas;

    String tableClassName;

    public TableMeta(final ClassInfo classInfo, final String generatedPackage) {
        this.generatedPackage = generatedPackage;
        this.entityName = classInfo.getSimpleName();
        this.staticFieldName = NameUtils.toUcUnderline(classInfo.getSimpleName());
        final String tableValue = NameUtils.tableValue(classInfo);
        this.tableName = tableValue.isEmpty() ?
            NameUtils.toLcUnderline(classInfo.getSimpleName()) : tableValue;
        this.tableClassName = "T" + this.entityName;
        this.columnMetas = classInfo.getDeclaredFieldInfo().stream()
            .filter(it -> !it.isStatic())
            .filter(it -> !it.hasAnnotation("org.springframework.data.annotation.Transient"))
            .map(ColumnMeta::new)
            .collect(Collectors.toList());

        final AnnotationInfoList annotationInfoList = classInfo.getAnnotationInfo()
            .filter(it ->
                "cn.javaer.snippets.jooq.codegen.withentity.GenColumn".equals(it.getName()));
        for (final AnnotationInfo annotationInfo : annotationInfoList) {
            final AnnotationParameterValueList parameterValues =
                annotationInfo.getParameterValues();
            final String fieldName = (String) parameterValues.getValue("field");
            final String fieldType =
                ((AnnotationClassRef) parameterValues.getValue("fieldType")).getName();
            final String columnName = (String) parameterValues.getValue("column");
            if (this.columnMetas.stream().noneMatch(it -> it.getFieldName().equals(fieldName))) {
                final String s = NameUtils.defaultValue(columnName,
                    NameUtils.toLcUnderline(fieldName));
                this.columnMetas.add(new ColumnMeta(fieldName, fieldType, s));
            }
        }
    }
}
