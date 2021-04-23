
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
import cn.javaer.snippets.security.rbac.UserPermission;

/**
 * This class is automatic generated.
 */
@SuppressWarnings({"ALL"})

@Generated("cn.javaer.snippets.jooq.codegen.withentity.CodeGenTool")
public class TUserPermission extends TableImpl<Record> implements TableMetaProvider<UserPermission, Void, Void> {

    public static final TUserPermission USER_PERMISSION = new TUserPermission();

    public static final Field<?>[] USER_PERMISSION_FIELDS = new Field[]{ USER_PERMISSION.USER_ID,USER_PERMISSION.PERMISSION_ID };


    public final TableField<Record, java.lang.Long> USER_ID = createField(DSL.name("user_id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    public final TableField<Record, java.lang.Long> PERMISSION_ID = createField(DSL.name("permission_id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    private final Table<?> _table = DSL.table(getUnqualifiedName());

    private final List<Field<?>> _selectFields = Arrays.asList();

    private final List<ColumnMeta<UserPermission, ?>> _savedColumnMetas = Arrays.asList(new ColumnMeta((Function<UserPermission, java.lang.Long>) UserPermission::getUserId, this.USER_ID),new ColumnMeta((Function<UserPermission, java.lang.Long>) UserPermission::getPermissionId, this.PERMISSION_ID));

    public TUserPermission() {
        this(DSL.name("user_permission"), null);
    }

    private TUserPermission(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private TUserPermission(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    @Override
    public TUserPermission as(String alias) {
        return new TUserPermission(DSL.name(alias), this);
    }

    @Override
    public Table<?> getTable() {
        return this._table;
    }

    @Override
    public Class getEntityClass() {
        return UserPermission.class;
    }


    @Override
    public @UnmodifiableView List<Field<?>> selectFields() {
        return _selectFields;
    }

    @Override
    public @UnmodifiableView List<ColumnMeta<UserPermission, ?>> saveColumnMetas() {
        return this._savedColumnMetas;
    }
}