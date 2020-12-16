package cn.javaer.snippets.jooq;

import org.jooq.Field;

import java.time.LocalDateTime;

/**
 * @author cn-src
 */
public interface FieldsProvider<ID> {

    /**
     * id 主键字段.
     *
     * @return Field
     */
    Field<ID> idField();

    /**
     * crud 默认使用的字段.
     *
     * @return Field
     */
    Field<?>[] crudFields();

    /**
     * 创建者字段.
     *
     * @return Field
     */
    Field<Object> createdByField();

    /**
     * 创建时间字段.
     *
     * @return Field
     */
    Field<LocalDateTime> createdDateField();

    /**
     * 更新者字段.
     *
     * @return Field
     */
    Field<Object> updatedByField();

    /**
     * 更新时间字段.
     *
     * @return Field
     */
    Field<LocalDateTime> updatedDateField();
}
