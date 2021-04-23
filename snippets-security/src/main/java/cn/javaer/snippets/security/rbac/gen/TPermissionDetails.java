
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
import cn.javaer.snippets.security.rbac.PermissionDetails;

/**
 * This class is automatic generated.
 */
@SuppressWarnings({"ALL"})

@Generated("cn.javaer.snippets.jooq.codegen.withentity.CodeGenTool")
public class TPermissionDetails extends TableImpl<Record> implements TableMetaProvider<PermissionDetails, java.lang.Long, java.lang.Long> {

    public static final TPermissionDetails PERMISSION_DETAILS = new TPermissionDetails();

    public static final Field<?>[] PERMISSION_DETAILS_FIELDS = new Field[]{ PERMISSION_DETAILS.ID,PERMISSION_DETAILS.NAME,PERMISSION_DETAILS.AUTHORITY,PERMISSION_DETAILS.DESCRIPTION,PERMISSION_DETAILS.CREATED_DATE,PERMISSION_DETAILS.CREATED_BY_ID };


    public final TableField<Record, java.lang.Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    public final TableField<Record, java.lang.String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    public final TableField<Record, java.lang.String> AUTHORITY = createField(DSL.name("authority"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    public final TableField<Record, java.lang.String> DESCRIPTION = createField(DSL.name("description"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    public final TableField<Record, java.time.LocalDateTime> CREATED_DATE = createField(DSL.name("created_date"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    public final TableField<Record, java.lang.Long> CREATED_BY_ID = createField(DSL.name("created_by_id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    private final Table<?> _table = DSL.table(getUnqualifiedName());

    private final List<Field<?>> _selectFields = Arrays.asList();

    private final List<ColumnMeta<PermissionDetails, ?>> _savedColumnMetas = Arrays.asList(new ColumnMeta((Function<PermissionDetails, java.lang.Long>) PermissionDetails::getId, this.ID),new ColumnMeta((Function<PermissionDetails, java.lang.String>) PermissionDetails::getName, this.NAME),new ColumnMeta((Function<PermissionDetails, java.lang.String>) PermissionDetails::getAuthority, this.AUTHORITY),new ColumnMeta((Function<PermissionDetails, java.lang.String>) PermissionDetails::getDescription, this.DESCRIPTION));

    private final ColumnMeta<PermissionDetails, java.lang.Long> _idMeta = new ColumnMeta<>(PermissionDetails::getId, this.ID);

    private final ColumnMeta<PermissionDetails, java.lang.Long> _createdByMeta = new ColumnMeta<>(PermissionDetails::getCreatedById, this.CREATED_BY_ID);

    private final ColumnMeta<PermissionDetails, java.time.LocalDateTime> _createdDateMeta = new ColumnMeta<>(PermissionDetails::getCreatedDate, this.CREATED_DATE);

    public TPermissionDetails() {
        this(DSL.name("permission_details"), null);
    }

    private TPermissionDetails(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private TPermissionDetails(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    @Override
    public TPermissionDetails as(String alias) {
        return new TPermissionDetails(DSL.name(alias), this);
    }

    @Override
    public Table<?> getTable() {
        return this._table;
    }

    @Override
    public Class getEntityClass() {
        return PermissionDetails.class;
    }

    @Override
    public Optional<ColumnMeta<PermissionDetails, java.lang.Long>> idGenerator() {
        return Optional.of(this._idMeta);
    }

    @Override
    public Optional<ColumnMeta<PermissionDetails, java.lang.Long>> getId() {
        return Optional.of(this._idMeta);
    }

    @Override
    public Optional<ColumnMeta<PermissionDetails, java.lang.Long>> getCreatedBy() {
        return Optional.of(this._createdByMeta);
    }


    @Override
    public Optional<ColumnMeta<PermissionDetails, java.time.LocalDateTime>> getCreatedDate() {
        return Optional.of(this._createdDateMeta);
    }

    @Override
    public @UnmodifiableView List<Field<?>> selectFields() {
        return _selectFields;
    }

    @Override
    public @UnmodifiableView List<ColumnMeta<PermissionDetails, ?>> saveColumnMetas() {
        return this._savedColumnMetas;
    }
}