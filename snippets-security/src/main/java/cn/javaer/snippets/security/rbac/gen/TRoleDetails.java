
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
import cn.javaer.snippets.security.rbac.RoleDetails;

/**
 * This class is automatic generated.
 */
@SuppressWarnings({"ALL"})

@Generated("cn.javaer.snippets.jooq.codegen.withentity.CodeGenTool")
public class TRoleDetails extends TableImpl<Record> implements TableMetaProvider<RoleDetails, java.lang.Long, java.lang.Long> {

    public static final TRoleDetails ROLE_DETAILS = new TRoleDetails();

    public static final Field<?>[] ROLE_DETAILS_FIELDS = new Field[]{ ROLE_DETAILS.ID,ROLE_DETAILS.NAME,ROLE_DETAILS.DESCRIPTION,ROLE_DETAILS.CREATED_DATE,ROLE_DETAILS.CREATED_BY_ID };


    public final TableField<Record, java.lang.Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    public final TableField<Record, java.lang.String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    public final TableField<Record, java.lang.String> DESCRIPTION = createField(DSL.name("description"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    public final TableField<Record, java.time.LocalDateTime> CREATED_DATE = createField(DSL.name("created_date"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    public final TableField<Record, java.lang.Long> CREATED_BY_ID = createField(DSL.name("created_by_id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    private final Table<?> _table = DSL.table(getUnqualifiedName());

    private final List<Field<?>> _selectFields = Arrays.asList();

    private final List<ColumnMeta<RoleDetails, ?>> _savedColumnMetas = Arrays.asList(new ColumnMeta((Function<RoleDetails, java.lang.Long>) RoleDetails::getId, this.ID),new ColumnMeta((Function<RoleDetails, java.lang.String>) RoleDetails::getName, this.NAME),new ColumnMeta((Function<RoleDetails, java.lang.String>) RoleDetails::getDescription, this.DESCRIPTION));

    private final ColumnMeta<RoleDetails, java.lang.Long> _idMeta = new ColumnMeta<>(RoleDetails::getId, this.ID);

    private final ColumnMeta<RoleDetails, java.lang.Long> _createdByMeta = new ColumnMeta<>(RoleDetails::getCreatedById, this.CREATED_BY_ID);

    private final ColumnMeta<RoleDetails, java.time.LocalDateTime> _createdDateMeta = new ColumnMeta<>(RoleDetails::getCreatedDate, this.CREATED_DATE);

    public TRoleDetails() {
        this(DSL.name("role_details"), null);
    }

    private TRoleDetails(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private TRoleDetails(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    @Override
    public TRoleDetails as(String alias) {
        return new TRoleDetails(DSL.name(alias), this);
    }

    @Override
    public Table<?> getTable() {
        return this._table;
    }

    @Override
    public Class getEntityClass() {
        return RoleDetails.class;
    }

    @Override
    public Optional<ColumnMeta<RoleDetails, java.lang.Long>> idGenerator() {
        return Optional.of(this._idMeta);
    }

    @Override
    public Optional<ColumnMeta<RoleDetails, java.lang.Long>> getId() {
        return Optional.of(this._idMeta);
    }

    @Override
    public Optional<ColumnMeta<RoleDetails, java.lang.Long>> getCreatedBy() {
        return Optional.of(this._createdByMeta);
    }


    @Override
    public Optional<ColumnMeta<RoleDetails, java.time.LocalDateTime>> getCreatedDate() {
        return Optional.of(this._createdDateMeta);
    }

    @Override
    public @UnmodifiableView List<Field<?>> selectFields() {
        return _selectFields;
    }

    @Override
    public @UnmodifiableView List<ColumnMeta<RoleDetails, ?>> saveColumnMetas() {
        return this._savedColumnMetas;
    }
}