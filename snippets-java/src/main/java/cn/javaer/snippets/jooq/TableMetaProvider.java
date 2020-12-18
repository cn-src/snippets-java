package cn.javaer.snippets.jooq;

import org.jetbrains.annotations.UnmodifiableView;
import org.jooq.Field;
import org.jooq.Table;

import java.util.List;
import java.util.Optional;

/**
 * The interface Table meta provider.
 *
 * @param <T> the type parameter
 *
 * @author cn -src
 */
public interface TableMetaProvider<T> {

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
    default Optional<ColumnMeta> id() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    default ColumnMeta getId() {
        throw new UnsupportedOperationException();
    }

    /**
     * 查询默认使用的字段.
     *
     * @return Field[] list
     */
    @UnmodifiableView
    List<Field<Object>> selectFields();

    /**
     * 插入或者更新默认使用的字段, 不含审计相关的字段.
     *
     * @return Field[] list
     */
    @UnmodifiableView
    List<ColumnMeta> saveColumnMetas();

    /**
     * 创建者字段.
     *
     * @return Field optional
     */
    default Optional<ColumnMeta> createdBy() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets created by.
     *
     * @return the created by
     */
    default ColumnMeta getCreatedBy() {
        throw new UnsupportedOperationException();
    }

    /**
     * 创建时间字段.
     *
     * @return Field optional
     */
    default Optional<ColumnMeta> createdDate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets created date.
     *
     * @return the created date
     */
    default ColumnMeta getCreatedDate() {
        throw new UnsupportedOperationException();
    }

    /**
     * 更新者字段.
     *
     * @return Field optional
     */
    default Optional<ColumnMeta> updatedBy() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets updated by.
     *
     * @return the updated by
     */
    default ColumnMeta getUpdatedBy() {
        throw new UnsupportedOperationException();
    }

    /**
     * 更新时间字段.
     *
     * @return Field optional
     */
    default Optional<ColumnMeta> updatedDate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets updated date.
     *
     * @return the updated date
     */
    default ColumnMeta getUpdatedDate() {
        throw new UnsupportedOperationException();
    }
}
