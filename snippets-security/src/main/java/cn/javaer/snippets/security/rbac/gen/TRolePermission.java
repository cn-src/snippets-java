
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
import cn.javaer.snippets.security.rbac.RolePermission;

/**
 * This class is automatic generated.
 */
@SuppressWarnings({"ALL"})

@Generated("cn.javaer.snippets.jooq.codegen.withentity.CodeGenTool")
public class TRolePermission extends TableImpl<Record> implements TableMetaProvider<RolePermission, Void, Void> {

    public static final TRolePermission ROLE_PERMISSION = new TRolePermission();

    public static final Field<?>[] ROLE_PERMISSION_FIELDS = new Field[]{ ROLE_PERMISSION.ROLE_ID,ROLE_PERMISSION.PERMISSION_ID };


    public final TableField<Record, java.lang.Long> ROLE_ID = createField(DSL.name("role_id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    public final TableField<Record, java.lang.Long> PERMISSION_ID = createField(DSL.name("permission_id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    private final Table<?> _table = DSL.table(getUnqualifiedName());

    private final List<Field<?>> _selectFields = Arrays.asList(this.ROLE_ID,this.PERMISSION_ID);

    private final List<ColumnMeta<RolePermission, ?>> _savedColumnMetas = Arrays.asList(new ColumnMeta((Function<RolePermission, java.lang.Long>) RolePermission::getRoleId, this.ROLE_ID),new ColumnMeta((Function<RolePermission, java.lang.Long>) RolePermission::getPermissionId, this.PERMISSION_ID));

    public TRolePermission() {
        this(DSL.name("role_permission"), null);
    }

    private TRolePermission(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private TRolePermission(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    @Override
    public TRolePermission as(String alias) {
        return new TRolePermission(DSL.name(alias), this);
    }

    @Override
    public Table<?> getTable() {
        return this._table;
    }

    @Override
    public Class getEntityClass() {
        return RolePermission.class;
    }


    @Override
    public @UnmodifiableView List<Field<?>> selectFields() {
        return _selectFields;
    }

    @Override
    public @UnmodifiableView List<ColumnMeta<RolePermission, ?>> saveColumnMetas() {
        return this._savedColumnMetas;
    }
}