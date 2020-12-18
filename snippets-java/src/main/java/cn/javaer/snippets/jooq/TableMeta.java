package cn.javaer.snippets.jooq;

import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.UnmodifiableView;
import org.jooq.Field;
import org.jooq.Table;

import java.util.List;
import java.util.Optional;

/**
 * @author cn-src
 */
@Builder
@Value
public class TableMeta<T> implements TableMetaProvider<T> {
    Table<?> table;
    Class<T> entityClass;
    boolean idReadOnly;
    ColumnMeta id;
    ColumnMeta createdBy;
    ColumnMeta createdDate;
    ColumnMeta updatedBy;
    ColumnMeta updatedDate;
    @UnmodifiableView
    List<Field<Object>> selectFields;
    @UnmodifiableView
    List<ColumnMeta> saveColumnMetas;

    @Override
    public @UnmodifiableView List<Field<Object>> selectFields() {
        return this.selectFields;
    }

    @Override
    public @UnmodifiableView List<ColumnMeta> saveColumnMetas() {
        return this.saveColumnMetas;
    }

    @Override
    public Optional<ColumnMeta> id() {
        return Optional.ofNullable(this.id);
    }

    @Override
    public Optional<ColumnMeta> idGenerator() {
        if (!this.idReadOnly) {
            return Optional.ofNullable(this.id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<ColumnMeta> createdBy() {
        return Optional.ofNullable(this.createdBy);
    }

    @Override
    public Optional<ColumnMeta> createdDate() {
        return Optional.ofNullable(this.createdDate);
    }

    @Override
    public Optional<ColumnMeta> updatedBy() {
        return Optional.ofNullable(this.updatedBy);
    }

    @Override
    public Optional<ColumnMeta> updatedDate() {
        return Optional.ofNullable(this.updatedDate);
    }
}
