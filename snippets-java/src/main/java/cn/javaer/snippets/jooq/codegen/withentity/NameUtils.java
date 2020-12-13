package cn.javaer.snippets.jooq.codegen.withentity;

import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.FieldInfo;

import java.util.Optional;

/**
 * @author cn-src
 */
interface NameUtils {

    static String tableValue(final ClassInfo classInfo) {
        return Optional.ofNullable(classInfo.getAnnotationInfo(
            "org.springframework.data.relational.core.mapping.Table"))
            .map(AnnotationInfo::getParameterValues)
            .map(it -> it.getValue("value"))
            .map(String.class::cast)
            .orElse("");
    }

    static String columnValue(final FieldInfo fieldInfo) {
        return Optional.ofNullable(fieldInfo.getAnnotationInfo(
            "org.springframework.data.relational.core.mapping.Column"))
            .map(AnnotationInfo::getParameterValues)
            .map(it -> it.getValue("value"))
            .map(String.class::cast)
            .orElse("");
    }
}
