
// @formatter:off
package cn.javaer.snippets.security.rbac.gen;

import javax.annotation.Generated;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.jetbrains.annotations.UnmodifiableView;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import cn.javaer.snippets.jooq.TableMetaProvider;
import cn.javaer.snippets.jooq.ColumnMeta;
import cn.javaer.snippets.security.rbac.Permission;

/**
 * This class is automatic generated.
 */
@SuppressWarnings({"ALL"})

@Generated("cn.javaer.snippets.jooq.codegen.withentity.CodeGenTool")
public class TPermission extends TableImpl<Record> implements TableMetaProvider<Permission, java.lang.Long, Void> {

    public static final TPermission PERMISSION = new TPermission();

    public static final Field<?>[] PERMISSION_FIELDS = new Field[]{ PERMISSION.ID,PERMISSION.NAME,PERMISSION.AUTHORITY };


    public final TableField<Record, java.lang.Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    public final TableField<Record, java.lang.String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    public final TableField<Record, java.lang.String> AUTHORITY = createField(DSL.name("authority"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    private final Table<?> _table = DSL.table(getUnqualifiedName());

    private final List<Field<?>> _selectFields = Arrays.asList();

    private final List<ColumnMeta<Permission, ?>> _savedColumnMetas = Arrays.asList(new ColumnMeta((Function<Permission, java.lang.Long>) Permission::getId, this.ID),new ColumnMeta((Function<Permission, java.lang.String>) Permission::getName, this.NAME),new ColumnMeta((Function<Permission, java.lang.String>) Permission::getAuthority, this.AUTHORITY));

    private final ColumnMeta<Permission, java.lang.Long> _idMeta = new ColumnMeta<>(Permission::getId, this.ID);

    public TPermission() {
        this(DSL.name("permission"), null);
    }

    private TPermission(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private TPermission(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    @Override
    public TPermission as(String alias) {
        return new TPermission(DSL.name(alias), this);
    }

    @Override
    public Table<?> getTable() {
        return this._table;
    }

    @Override
    public Class getEntityClass() {
        return Permission.class;
    }

    @Override
    public Optional<ColumnMeta<Permission, java.lang.Long>> idGenerator() {
        return Optional.of(this._idMeta);
    }

    @Override
    public Optional<ColumnMeta<Permission, java.lang.Long>> getId() {
        return Optional.of(this._idMeta);
    }


    @Override
    public @UnmodifiableView List<Field<?>> selectFields() {
        return _selectFields;
    }

    @Override
    public @UnmodifiableView List<ColumnMeta<Permission, ?>> saveColumnMetas() {
        return this._savedColumnMetas;
    }
}