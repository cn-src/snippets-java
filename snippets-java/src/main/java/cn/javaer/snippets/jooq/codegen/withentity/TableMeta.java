package cn.javaer.snippets.jooq.codegen.withentity;

import io.github.classgraph.AnnotationClassRef;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.AnnotationInfoList;
import io.github.classgraph.AnnotationParameterValueList;
import io.github.classgraph.ClassInfo;
import lombok.Value;

import java.util.ArrayList;
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
    List<ColumnMeta> declaredColumnMetas;
    List<ColumnMeta> allColumnMetas;
    boolean hasAttachColumn;
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
        this.declaredColumnMetas = classInfo.getDeclaredFieldInfo().stream()
            .filter(it -> !it.isStatic())
            .filter(it -> !it.hasAnnotation("org.springframework.data.annotation.Transient"))
            .filter(it -> !it.hasAnnotation(
                "cn.javaer.snippets.jooq.codegen.withentity.ExcludeDeclared"))
            .map(ColumnMeta::new)
            .collect(Collectors.toList());

        final List<ColumnMeta> allColumnMetas = new ArrayList<>(this.columnMetas);

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
            if (allColumnMetas.stream().noneMatch(it -> it.getFieldName().equals(fieldName))) {
                final String s = NameUtils.defaultValue(columnName,
                    NameUtils.toLcUnderline(fieldName));
                allColumnMetas.add(new ColumnMeta(fieldName, fieldType, s));
            }
        }
        this.hasAttachColumn = allColumnMetas.size() > this.columnMetas.size();
        this.allColumnMetas = allColumnMetas;
    }
}
