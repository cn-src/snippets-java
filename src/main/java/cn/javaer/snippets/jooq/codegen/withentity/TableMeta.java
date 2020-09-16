package cn.javaer.snippets.jooq.codegen.withentity;

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
    }
}
