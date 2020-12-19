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
public interface TableMetaProvider<T, ID, A> {

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
     * id 主键字段.
     *
     * @return Field optional
     */
    default Optional<ColumnMeta<T, ID>> id() {
        throw new UnsupportedOperationException();
    }

    /**
     * Id generator optional.
     *
     * @return the optional
     */
    default Optional<ColumnMeta<T, ID>> idGenerator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    default ColumnMeta<T, ID> getId() {
        throw new UnsupportedOperationException();
    }

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
     * 创建者字段.
     *
     * @return Field optional
     */
    default Optional<ColumnMeta<T, A>> createdBy() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets created by.
     *
     * @return the created by
     */
    default ColumnMeta<T, A> getCreatedBy() {
        throw new UnsupportedOperationException();
    }

    /**
     * 创建时间字段.
     *
     * @return Field optional
     */
    default Optional<ColumnMeta<T, LocalDateTime>> createdDate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets created date.
     *
     * @return the created date
     */
    default ColumnMeta<T, LocalDateTime> getCreatedDate() {
        throw new UnsupportedOperationException();
    }

    /**
     * 更新者字段.
     *
     * @return Field optional
     */
    default Optional<ColumnMeta<T, A>> updatedBy() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets updated by.
     *
     * @return the updated by
     */
    default ColumnMeta<T, A> getUpdatedBy() {
        throw new UnsupportedOperationException();
    }

    /**
     * 更新时间字段.
     *
     * @return Field optional
     */
    default Optional<ColumnMeta<T, LocalDateTime>> updatedDate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets updated date.
     *
     * @return the updated date
     */
    default ColumnMeta<T, LocalDateTime> getUpdatedDate() {
        throw new UnsupportedOperationException();
    }
}
