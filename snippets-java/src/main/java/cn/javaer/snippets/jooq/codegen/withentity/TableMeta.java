package cn.javaer.snippets.jooq.codegen.withentity;

import cn.javaer.snippets.util.StrUtils;
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
    String entityClassName;
    String staticFieldName;
    ColumnMeta idColumnMeta;
    ColumnMeta updatedByColumnMeta;
    ColumnMeta updatedDateColumnMeta;
    ColumnMeta createdByColumnMeta;
    ColumnMeta createdDateColumnMeta;
    boolean hasAuditor;
    String auditorType;
    List<ColumnMeta> columnMetas;
    List<ColumnMeta> declaredColumnMetas;
    List<ColumnMeta> allColumnMetas;
    boolean hasAttachColumn;
    String tableClassName;

    public TableMeta(final ClassInfo classInfo, final String generatedPackage) {
        this.generatedPackage = generatedPackage;
        this.entityName = classInfo.getSimpleName();
        this.entityClassName = classInfo.getName();
        this.staticFieldName = StrUtils.toSnakeUpper(classInfo.getSimpleName());

        final String tableValue = NameUtils.tableValue(classInfo);
        this.tableName = tableValue.isEmpty() ?
            StrUtils.toSnakeLower(classInfo.getSimpleName()) : tableValue;

        this.tableClassName = "T" + this.entityName;

        this.columnMetas = classInfo.getDeclaredFieldInfo().stream()
            .filter(it -> !it.isStatic())
            .filter(it -> !it.hasAnnotation("org.springframework.data.annotation.Transient"))
            .map(ColumnMeta::new)
            .collect(Collectors.toList());

        this.idColumnMeta = this.columnMetas.stream()
            .filter(ColumnMeta::isId).findFirst().orElse(null);
        this.updatedByColumnMeta = this.columnMetas.stream()
            .filter(ColumnMeta::isUpdatedBy).findFirst().orElse(null);
        this.updatedDateColumnMeta = this.columnMetas.stream()
            .filter(ColumnMeta::isUpdatedDate).findFirst().orElse(null);
        this.createdByColumnMeta = this.columnMetas.stream()
            .filter(ColumnMeta::isCreatedBy).findFirst().orElse(null);
        this.createdDateColumnMeta = this.columnMetas.stream()
            .filter(ColumnMeta::isCreatedDate).findFirst().orElse(null);
        this.hasAuditor = (this.updatedByColumnMeta != null) || (this.createdByColumnMeta != null);
        if (this.updatedByColumnMeta != null) {
            this.auditorType = this.updatedByColumnMeta.getFieldType();
        }
        else if (this.createdByColumnMeta != null) {
            this.auditorType = this.createdByColumnMeta.getFieldType();
        }
        else {
            this.auditorType = null;
        }

        this.declaredColumnMetas = classInfo.getDeclaredFieldInfo().stream()
            .filter(it -> !it.isStatic())
            .filter(it -> !it.hasAnnotation("org.springframework.data.annotation.Transient"))
            .filter(it -> !it.hasAnnotation(ExcludeDeclared.class.getName()))
            .map(ColumnMeta::new)
            .collect(Collectors.toList());

        final List<ColumnMeta> allColumnMetas = new ArrayList<>(this.columnMetas);
        final AnnotationInfoList annotationInfoList =
            classInfo.getAnnotationInfoRepeatable(GenColumn.class.getName());
        for (final AnnotationInfo annotationInfo : annotationInfoList) {
            final AnnotationParameterValueList parameterValues =
                annotationInfo.getParameterValues();
            final String fieldName = (String) parameterValues.getValue("field");
            final String fieldType =
                ((AnnotationClassRef) parameterValues.getValue("fieldType")).getName();
            final String columnName = (String) parameterValues.getValue("column");
            if (allColumnMetas.stream().noneMatch(it -> it.getFieldName().equals(fieldName))) {
                final String s = StrUtils.defaultEmpty(columnName,
                    StrUtils.toSnakeLower(fieldName));
                allColumnMetas.add(new ColumnMeta(fieldName, fieldType, s));
            }
        }
        this.hasAttachColumn = allColumnMetas.size() > this.columnMetas.size();
        this.allColumnMetas = allColumnMetas;
    }
}
