package cn.javaer.snippets.jooq;

import org.jooq.Field;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author cn-src
 */
public interface ColumnsProvider {

    /**
     * id 主键字段.
     *
     * @return Field
     */
    default <ID> Optional<Field<ID>> id() {
        return Optional.empty();
    }

    default <ID> Field<ID> requiredId() {
        //noinspection unchecked
        return (Field<ID>) this.id().orElseThrow(
            () -> new IllegalStateException("Id must bu not null"));
    }

    /**
     * 查询默认使用的字段.
     *
     * @return Field[]
     */
    Field<?>[] selectColumns();

    /**
     * 插入或者更新默认使用的字段, 不含审计相关的字段.
     *
     * @return Field[]
     */
    Field<?>[] saveColumns();

    /**
     * 创建者字段.
     *
     * @return Field
     */
    default <ID> Optional<Field<ID>> createdBy() {
        return Optional.empty();
    }

    default <ID> Field<ID> requiredCreatedBy() {
        //noinspection unchecked
        return (Field<ID>) this.createdBy().orElseThrow(
            () -> new IllegalStateException("CreatedBy must bu not null"));
    }

    /**
     * 创建时间字段.
     *
     * @return Field
     */
    default Optional<Field<LocalDateTime>> createdDate() {
        return Optional.empty();
    }

    default Field<LocalDateTime> requiredCreatedDate() {
        return this.createdDate().orElseThrow(
            () -> new IllegalStateException("CreatedDate must bu not null"));
    }

    /**
     * 更新者字段.
     *
     * @return Field
     */
    default <ID> Optional<Field<ID>> updatedBy() {
        return Optional.empty();
    }

    default <ID> Field<ID> requiredUpdatedBy() {
        //noinspection unchecked
        return (Field<ID>) this.updatedBy().orElseThrow(
            () -> new IllegalStateException("UpdatedBy must bu not null"));
    }

    /**
     * 更新时间字段.
     *
     * @return Field
     */
    default Optional<Field<LocalDateTime>> updatedDate() {
        return Optional.empty();
    }

    default Field<LocalDateTime> requiredUpdatedDate() {
        return this.updatedDate().orElseThrow(
            () -> new IllegalStateException("UpdatedDate must bu not null"));
    }
}
