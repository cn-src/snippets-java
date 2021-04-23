package cn.javaer.snippets.jooq.codegen.withentity;

import cn.javaer.snippets.jooq.ExcludeSaved;
import cn.javaer.snippets.jooq.ExcludeSelect;
import cn.javaer.snippets.util.ReflectionUtils;
import cn.javaer.snippets.util.StrUtils;
import io.github.classgraph.AnnotationClassRef;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.AnnotationInfoList;
import io.github.classgraph.AnnotationParameterValueList;
import io.github.classgraph.ClassInfo;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

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
    @Nullable
    ColumnMeta idColumnMeta;
    @Nullable
    ColumnMeta updatedByColumnMeta;
    @Nullable
    ColumnMeta updatedDateColumnMeta;
    @Nullable
    ColumnMeta createdByColumnMeta;
    @Nullable
    ColumnMeta createdDateColumnMeta;
    boolean hasAuditor;
    String auditorType;
    List<ColumnMeta> columnMetas;
    List<ColumnMeta> savedColumnMetas;
    List<ColumnMeta> selectColumnMetas;
    List<ColumnMeta> allColumnMetas;
    boolean hasAttachColumn;
    String tableClassName;

    boolean hasGeneratedAnnotation;

    public TableMeta(final ClassInfo classInfo, final String generatedPackage) {
        this.hasGeneratedAnnotation = ReflectionUtils.isPresent("javax.annotation.Generated");
        this.generatedPackage = generatedPackage;
        this.entityName = classInfo.getSimpleName();
        this.entityClassName = classInfo.getName();
        this.staticFieldName = StrUtils.toSnakeUpper(classInfo.getSimpleName());

        final String tableValue = NameUtils.tableValue(classInfo);
        this.tableName = tableValue.isEmpty() ?
            StrUtils.toSnakeLower(classInfo.getSimpleName()) : tableValue;

        this.tableClassName = "T" + this.entityName;

        this.columnMetas = getColumnMetas(classInfo);
        this.savedColumnMetas = getSavedColumnMetas(classInfo);

        // TODO 考虑联合主键处理
        final List<ColumnMeta> idColumnMetas = this.columnMetas.stream()
            .filter(ColumnMeta::isId).collect(Collectors.toList());
        this.idColumnMeta = idColumnMetas.size() == 1 ? idColumnMetas.get(0) : null;

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

        this.selectColumnMetas = classInfo.getDeclaredFieldInfo().stream()
            .filter(it -> !it.isStatic())
            .filter(it -> !it.hasAnnotation("org.springframework.data.annotation.Transient"))
            .filter(it -> !it.hasAnnotation(ExcludeSelect.class.getName()))
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
                final String s = StrUtils.defaultIfEmpty(columnName,
                    StrUtils.toSnakeLower(fieldName));
                allColumnMetas.add(new ColumnMeta(fieldName, fieldType, s));
            }
        }
        this.hasAttachColumn = allColumnMetas.size() > this.columnMetas.size();
        this.allColumnMetas = allColumnMetas;
    }

    static List<ColumnMeta> getColumnMetas(final ClassInfo classInfo) {
        return classInfo.getDeclaredFieldInfo().stream()
            .filter(it -> !it.isStatic())
            .filter(it -> !it.hasAnnotation("org.springframework.data.annotation.Transient"))
            .map(ColumnMeta::new)
            .collect(Collectors.toList());
    }

    static List<ColumnMeta> getSavedColumnMetas(final ClassInfo classInfo) {
        return classInfo.getDeclaredFieldInfo().stream()
            .filter(it -> !it.isStatic())
            .filter(it -> !it.hasAnnotation("org.springframework.data.annotation.Transient"))
            .filter(it -> !it.hasAnnotation(ExcludeSaved.class.getName()))
            .map(ColumnMeta::new)
            .filter(it -> !it.isReadOnly())
            .filter(it -> !it.isUpdatedBy())
            .filter(it -> !it.isUpdatedDate())
            .filter(it -> !it.isCreatedBy())
            .filter(it -> !it.isCreatedDate())
            .collect(Collectors.toList());
    }
}
