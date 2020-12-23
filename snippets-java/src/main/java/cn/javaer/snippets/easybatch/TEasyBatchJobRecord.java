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
import java.util.Optional;
import java.util.function.Function;

/**
 * This class is automatic generated.
 */
@SuppressWarnings({"ALL"})
@Generated("cn.javaer.snippets.jooq.codegen.withentity.CodeGenTool")
public class TEasyBatchJobRecord extends TableImpl<Record> implements TableMetaProvider<EasyBatchJobRecord, String, Void> {

    public static final TEasyBatchJobRecord EASY_BATCH_JOB_RECORD = new TEasyBatchJobRecord();

    public final TableField<Record, String> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    public final TableField<Record, String> JOB_NAME = createField(DSL.name("job_name"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    public final TableField<Record, java.time.LocalDateTime> JOB_START_TIME = createField(DSL.name("job_start_time"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    public final TableField<Record, java.time.LocalDateTime> JOB_END_TIME = createField(DSL.name("job_end_time"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    public final TableField<Record, java.time.LocalDateTime> DATA_START_TIME = createField(DSL.name("data_start_time"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    public final TableField<Record, java.time.LocalDateTime> DATA_END_TIME = createField(DSL.name("data_end_time"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    public final TableField<Record, String> JOB_STATUS = createField(DSL.name("job_status"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    public final TableField<Record, String> BATCH_ID = createField(DSL.name("batch_id"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    public final TableField<Record, Long> READ_COUNT = createField(DSL.name("read_count"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    public final TableField<Record, Long> WRITE_COUNT = createField(DSL.name("write_count"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    public final TableField<Record, Long> FILTER_COUNT = createField(DSL.name("filter_count"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    public final TableField<Record, Long> ERROR_COUNT = createField(DSL.name("error_count"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    public final TableField<Record, String> JOB_PARAMETERS = createField(DSL.name("job_parameters"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    public final TableField<Record, String> LAST_ERROR = createField(DSL.name("last_error"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    public final TableField<Record, java.time.LocalDateTime> CREATED_DATE = createField(DSL.name("created_date"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    private final Table<?> __table = DSL.table(getUnqualifiedName());

    private final List<Field<?>> __selectFields = Arrays.asList();

    private final List<ColumnMeta<EasyBatchJobRecord, ?>> __savedColumnMetas = Arrays.asList(new ColumnMeta((Function<EasyBatchJobRecord, String>) EasyBatchJobRecord::getId, this.ID),new ColumnMeta((Function<EasyBatchJobRecord, String>) EasyBatchJobRecord::getJobName, this.JOB_NAME),new ColumnMeta((Function<EasyBatchJobRecord, java.time.LocalDateTime>) EasyBatchJobRecord::getJobStartTime, this.JOB_START_TIME),new ColumnMeta((Function<EasyBatchJobRecord, java.time.LocalDateTime>) EasyBatchJobRecord::getJobEndTime, this.JOB_END_TIME),new ColumnMeta((Function<EasyBatchJobRecord, java.time.LocalDateTime>) EasyBatchJobRecord::getDataStartTime, this.DATA_START_TIME),new ColumnMeta((Function<EasyBatchJobRecord, java.time.LocalDateTime>) EasyBatchJobRecord::getDataEndTime, this.DATA_END_TIME),new ColumnMeta((Function<EasyBatchJobRecord, String>) EasyBatchJobRecord::getJobStatus, this.JOB_STATUS),new ColumnMeta((Function<EasyBatchJobRecord, String>) EasyBatchJobRecord::getBatchId, this.BATCH_ID),new ColumnMeta((Function<EasyBatchJobRecord, Long>) EasyBatchJobRecord::getReadCount, this.READ_COUNT),new ColumnMeta((Function<EasyBatchJobRecord, Long>) EasyBatchJobRecord::getWriteCount, this.WRITE_COUNT),new ColumnMeta((Function<EasyBatchJobRecord, Long>) EasyBatchJobRecord::getFilterCount, this.FILTER_COUNT),new ColumnMeta((Function<EasyBatchJobRecord, Long>) EasyBatchJobRecord::getErrorCount, this.ERROR_COUNT),new ColumnMeta((Function<EasyBatchJobRecord, String>) EasyBatchJobRecord::getJobParameters, this.JOB_PARAMETERS),new ColumnMeta((Function<EasyBatchJobRecord, String>) EasyBatchJobRecord::getLastError, this.LAST_ERROR),new ColumnMeta((Function<EasyBatchJobRecord, java.time.LocalDateTime>) EasyBatchJobRecord::getCreatedDate, this.CREATED_DATE));

    private final ColumnMeta<EasyBatchJobRecord, String> __idMeta = new ColumnMeta<>(EasyBatchJobRecord::getId, this.ID);

    public TEasyBatchJobRecord() {
        this(DSL.name("easy_batch_job_record"), null);
    }

    private TEasyBatchJobRecord(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private TEasyBatchJobRecord(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    @Override
    public TEasyBatchJobRecord as(String alias) {
        return new TEasyBatchJobRecord(DSL.name(alias), this);
    }

    @Override
    public Table<?> getTable() {
        return this.__table;
    }

    @Override
    public Class getEntityClass() {
        return EasyBatchJobRecord.class;
    }

    @Override
    public Optional<ColumnMeta<EasyBatchJobRecord, String>> idGenerator() {
        return Optional.of(this.__idMeta);
    }

    @Override
    public Optional<ColumnMeta<EasyBatchJobRecord, String>> getId() {
        return Optional.of(this.__idMeta);
    }


    @Override
    public @UnmodifiableView List<Field<?>> selectFields() {
        return __selectFields;
    }

    @Override
    public @UnmodifiableView List<ColumnMeta<EasyBatchJobRecord, ?>> saveColumnMetas() {
        return this.__savedColumnMetas;
    }
}