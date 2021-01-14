package cn.javaer.snippets.jooq;

import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.UnmodifiableView;
import org.jooq.Field;
import org.jooq.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author cn-src
 */
@Builder
@Value
public class TableMeta<T, ID, A> implements TableMetaProvider<T, ID, A> {
    Table<?> table;
    Class<T> entityClass;
    boolean idReadOnly;
    ColumnMeta<T, ID> id;
    ColumnMeta<T, A> createdBy;
    ColumnMeta<T, LocalDateTime> createdDate;
    ColumnMeta<T, A> updatedBy;
    ColumnMeta<T, LocalDateTime> updatedDate;
    @UnmodifiableView
    List<Field<?>> selectFields;
    @UnmodifiableView
    List<ColumnMeta<T, ?>> savedColumnMetas;

    @Override
    public @UnmodifiableView List<Field<?>> selectFields() {
        return this.selectFields;
    }

    @Override
    public @UnmodifiableView List<ColumnMeta<T, ?>> saveColumnMetas() {
        return this.savedColumnMetas;
    }

    @Override
    public Optional<ColumnMeta<T, ID>> idGenerator() {
        if (!this.idReadOnly) {
            return Optional.ofNullable(this.id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<ColumnMeta<T, ID>> getId() {
        return Optional.ofNullable(this.id);
    }

    @Override
    public Optional<ColumnMeta<T, A>> getCreatedBy() {
        return Optional.ofNullable(this.createdBy);
    }

    @Override
    public Optional<ColumnMeta<T, LocalDateTime>> getCreatedDate() {
        return Optional.ofNullable(this.createdDate);
    }

    @Override
    public Optional<ColumnMeta<T, A>> getUpdatedBy() {
        return Optional.ofNullable(this.updatedBy);
    }

    @Override
    public Optional<ColumnMeta<T, LocalDateTime>> getUpdatedDate() {
        return Optional.ofNullable(this.updatedDate);
    }
}
