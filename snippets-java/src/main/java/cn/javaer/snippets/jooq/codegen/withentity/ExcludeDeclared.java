package cn.javaer.snippets.jooq.codegen.withentity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注此字段不作为默认的声明字段，比如从全局的 ENTITY_FIELDS 和 declaredFields() 方法中排除此字段。
 *
 * @author cn-src
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcludeDeclared {
}
