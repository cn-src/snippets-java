package cn.javaer.snippets.jooq;

import org.jooq.Field;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author cn-src
 */
public interface FieldsProvider {

    /**
     * id 主键字段.
     *
     * @return Field
     */
    default <ID> Optional<Field<ID>> idField() {
        return Optional.empty();
    }

    ;

    default <ID> Field<ID> requiredId() {
        //noinspection unchecked
        return (Field<ID>) idField().orElseThrow(
            () -> new IllegalStateException("Id must bu not null"));
    }

    /**
     * 查询默认使用的字段.
     *
     * @return Field[]
     */
    Field<?>[] selectFields();

    /**
     * 插入或者更新默认使用的字段, 不含审计相关的字段.
     *
     * @return Field[]
     */
    Field<?>[] saveFields();

    /**
     * 创建者字段.
     *
     * @return Field
     */
    default <ID> Optional<Field<ID>> createdByField() {
        return Optional.empty();
    }

    default <ID> Field<ID> requiredCreatedBy() {
        //noinspection unchecked
        return (Field<ID>) createdByField().orElseThrow(
            () -> new IllegalStateException("CreatedBy must bu not null"));
    }

    /**
     * 创建时间字段.
     *
     * @return Field
     */
    default Optional<Field<LocalDateTime>> createdDateField() {
        return Optional.empty();
    }

    default Field<LocalDateTime> requiredCreatedDate() {
        return createdDateField().orElseThrow(
            () -> new IllegalStateException("CreatedDate must bu not null"));
    }

    /**
     * 更新者字段.
     *
     * @return Field
     */
    default <ID> Optional<Field<ID>> updatedByField() {
        return Optional.empty();
    }

    default <ID> Field<ID> requiredUpdatedBy() {
        //noinspection unchecked
        return (Field<ID>) updatedByField().orElseThrow(
            () -> new IllegalStateException("UpdatedBy must bu not null"));
    }

    /**
     * 更新时间字段.
     *
     * @return Field
     */
    default Optional<Field<LocalDateTime>> updatedDateField() {
        return Optional.empty();
    }

    default Field<LocalDateTime> requiredUpdatedDate() {
        return updatedDateField().orElseThrow(
            () -> new IllegalStateException("UpdatedDate must bu not null"));
    }
}
