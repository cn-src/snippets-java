// @formatter:off
package cn.javaer.snippets.easybatch;

import cn.javaer.snippets.jooq.ColumnMeta;
import cn.javaer.snippets.jooq.TableMetaProvider;
import org.jetbrains.annotations.UnmodifiableView;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * This class is automatic generated.
 */
@SuppressWarnings({"ALL"})
@Generated("cn.javaer.snippets.jooq.codegen.withentity.CodeGenTool")
public class TEasyBatchJobTime extends TableImpl<Record> implements TableMetaProvider<EasyBatchJobTime, Void, Void> {

    public static final TEasyBatchJobTime EASY_BATCH_JOB_TIME = new TEasyBatchJobTime();

    public final TableField<Record, String> JOB_NAME = createField(DSL.name("job_name"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    public final TableField<Record, java.time.LocalDateTime> DATA_START_TIME = createField(DSL.name("data_start_time"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    private final Table<?> __table = DSL.table(getUnqualifiedName());

    private final List<Field<?>> __selectFields = Arrays.asList();

    private final List<ColumnMeta<EasyBatchJobTime, ?>> __savedColumnMetas = Arrays.asList(new ColumnMeta((Function<EasyBatchJobTime, String>) EasyBatchJobTime::getJobName, this.JOB_NAME),new ColumnMeta((Function<EasyBatchJobTime, java.time.LocalDateTime>) EasyBatchJobTime::getDataStartTime, this.DATA_START_TIME));

    public TEasyBatchJobTime() {
        this(DSL.name("easy_batch_job_time"), null);
    }

    private TEasyBatchJobTime(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private TEasyBatchJobTime(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    @Override
    public TEasyBatchJobTime as(String alias) {
        return new TEasyBatchJobTime(DSL.name(alias), this);
    }

    @Override
    public Table<?> getTable() {
        return this.__table;
    }

    @Override
    public Class getEntityClass() {
        return EasyBatchJobTime.class;
    }


    @Override
    public @UnmodifiableView List<Field<?>> selectFields() {
        return __selectFields;
    }

    @Override
    public @UnmodifiableView List<ColumnMeta<EasyBatchJobTime, ?>> saveColumnMetas() {
        return this.__savedColumnMetas;
    }
}