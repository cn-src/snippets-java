package cn.javaer.snippets.jooq;

import org.jetbrains.annotations.UnmodifiableView;
import org.jooq.Field;
import org.jooq.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The interface Table meta provider.
 *
 * @param <T> the type parameter
 *
 * @author cn -src
 */
public interface TableMeta<T, ID, A> {

    /**
     * Gets table.
     *
     * @return the table
     */
    Table<?> getTable();

    /**
     * Gets entity class.
     *
     * @return the entity class
     */
    Class<T> getEntityClass();

    /**
     * 查询默认使用的字段.
     *
     * @return Field[] list
     */
    @UnmodifiableView
    List<Field<?>> selectFields();

    /**
     * 插入或者更新默认使用的字段, 不含审计相关的字段.
     *
     * @return Field[] list
     */
    @UnmodifiableView
    List<ColumnMeta<T, ?>> saveColumnMetas();

    /**
     * id 主键字段.
     *
     * @return Field optional
     */
    default ColumnMeta<T, ID> id() {
        return this.getId().orElseThrow(UnsupportedOperationException::new);
    }

    /**
     * Id generator optional.
     *
     * @return the optional
     */
    default Optional<ColumnMeta<T, ID>> idGenerator() {
        return Optional.empty();
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    default Optional<ColumnMeta<T, ID>> getId() {
        return Optional.empty();
    }

    /**
     * 创建者字段.
     *
     * @return Field optional
     */
    default ColumnMeta<T, A> createdBy() {
        return this.getCreatedBy().orElseThrow(UnsupportedOperationException::new);
    }

    /**
     * Gets created by.
     *
     * @return the created by
     */
    default Optional<ColumnMeta<T, A>> getCreatedBy() {
        return Optional.empty();
    }

    /**
     * 创建时间字段.
     *
     * @return Field optional
     */
    default ColumnMeta<T, LocalDateTime> createdDate() {
        return this.getCreatedDate().orElseThrow(UnsupportedOperationException::new);
    }

    /**
     * Gets created date.
     *
     * @return the created date
     */
    default Optional<ColumnMeta<T, LocalDateTime>> getCreatedDate() {
        return Optional.empty();
    }

    /**
     * 更新者字段.
     *
     * @return Field optional
     */
    default ColumnMeta<T, A> updatedBy() {
        return this.getUpdatedBy().orElseThrow(UnsupportedOperationException::new);
    }

    /**
     * Gets updated by.
     *
     * @return the updated by
     */
    default Optional<ColumnMeta<T, A>> getUpdatedBy() {
        return Optional.empty();
    }

    /**
     * 更新时间字段.
     *
     * @return Field optional
     */
    default ColumnMeta<T, LocalDateTime> updatedDate() {
        return this.getUpdatedDate().orElseThrow(UnsupportedOperationException::new);
    }

    /**
     * Gets updated date.
     *
     * @return the updated date
     */
    default Optional<ColumnMeta<T, LocalDateTime>> getUpdatedDate() {
        return Optional.empty();
    }
}
