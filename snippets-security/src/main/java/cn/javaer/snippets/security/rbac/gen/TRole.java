
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
import cn.javaer.snippets.security.rbac.Role;

/**
 * This class is automatic generated.
 */
@SuppressWarnings({"ALL"})

@Generated("cn.javaer.snippets.jooq.codegen.withentity.CodeGenTool")
public class TRole extends TableImpl<Record> implements TableMetaProvider<Role, java.lang.Long, Void> {

    public static final TRole ROLE = new TRole();

    public static final Field<?>[] ROLE_FIELDS = new Field[]{ ROLE.ID,ROLE.NAME };


    public final TableField<Record, java.lang.Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    public final TableField<Record, java.lang.String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    private final Table<?> _table = DSL.table(getUnqualifiedName());

    private final List<Field<?>> _selectFields = Arrays.asList(this.ID,this.NAME);

    private final List<ColumnMeta<Role, ?>> _savedColumnMetas = Arrays.asList(new ColumnMeta((Function<Role, java.lang.Long>) Role::getId, this.ID),new ColumnMeta((Function<Role, java.lang.String>) Role::getName, this.NAME));

    private final ColumnMeta<Role, java.lang.Long> _idMeta = new ColumnMeta<>(Role::getId, this.ID);

    public TRole() {
        this(DSL.name("role"), null);
    }

    private TRole(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private TRole(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    @Override
    public TRole as(String alias) {
        return new TRole(DSL.name(alias), this);
    }

    @Override
    public Table<?> getTable() {
        return this._table;
    }

    @Override
    public Class getEntityClass() {
        return Role.class;
    }

    @Override
    public Optional<ColumnMeta<Role, java.lang.Long>> idGenerator() {
        return Optional.of(this._idMeta);
    }

    @Override
    public Optional<ColumnMeta<Role, java.lang.Long>> getId() {
        return Optional.of(this._idMeta);
    }


    @Override
    public @UnmodifiableView List<Field<?>> selectFields() {
        return _selectFields;
    }

    @Override
    public @UnmodifiableView List<ColumnMeta<Role, ?>> saveColumnMetas() {
        return this._savedColumnMetas;
    }
}