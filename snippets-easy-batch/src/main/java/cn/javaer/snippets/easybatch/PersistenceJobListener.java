package cn.javaer.snippets.easybatch;

import cn.javaer.snippets.jooq.JdbcCrud;
import org.jeasy.batch.core.job.JobParameters;
import org.jeasy.batch.core.job.JobReport;
import org.jeasy.batch.core.listener.BatchListener;
import org.jeasy.batch.core.listener.JobListener;
import org.jeasy.batch.core.record.Batch;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.WeakHashMap;

import static cn.javaer.snippets.easybatch.TEasyBatchJobRecord.EASY_BATCH_JOB_RECORD;
import static cn.javaer.snippets.easybatch.TEasyBatchJobTime.EASY_BATCH_JOB_TIME;

/**
 * @author cn-src
 */
public class PersistenceJobListener implements JobListener, BatchListener<Object> {
    private final JdbcCrud crud;
    private final EasyBatchJobRecord jobRecord;
    private final WeakHashMap<String, JobReport> monitor;

    public PersistenceJobListener(final JdbcCrud crud,
                                  final EasyBatchJobRecord jobRecord,
                                  final WeakHashMap<String, JobReport> monitor) {
        Objects.requireNonNull(crud, "'jdbcCrud' must be not null");
        Objects.requireNonNull(jobRecord, "'jobRecord' must be not null");

        this.crud = crud;
        this.jobRecord = jobRecord;
        this.monitor = monitor;
    }

    public PersistenceJobListener(final JdbcCrud crud,
                                  final EasyBatchJobRecord jobRecord) {
        this(crud, jobRecord, null);
    }

    @Override
    public void beforeJob(final JobParameters jobParameters) {
        if (this.monitor != null) {
            this.monitor.put(this.jobRecord.getBatchId(), this.jobRecord.getJobReport());
        }

        if (this.jobRecord.getDataStartTime() == null) {

            final LocalDateTime dataStartTime = this.crud.dsl()
                .select(EASY_BATCH_JOB_TIME.DATA_START_TIME)
                .from(EASY_BATCH_JOB_TIME)
                .where(EASY_BATCH_JOB_TIME.JOB_NAME.eq(this.jobRecord.getJobName()))
                .fetchOneInto(LocalDateTime.class);
            this.jobRecord.setDataStartTime(dataStartTime);

            this.crud.dsl().update(EASY_BATCH_JOB_TIME)
                .set(EASY_BATCH_JOB_TIME.DATA_START_TIME, this.jobRecord.getDataEndTime())
                .where(EASY_BATCH_JOB_TIME.JOB_NAME.eq(this.jobRecord.getJobName()))
                .execute();
        }
        this.crud.insert(EASY_BATCH_JOB_RECORD, this.jobRecord);
    }

    @Override
    public void afterJob(final JobReport jobReport) {
        if (this.monitor != null) {
            this.monitor.remove(this.jobRecord.getBatchId());
        }
        this.jobRecord.updateJobReport(jobReport);
        this.saveJobReport();
    }

    @Override
    public void afterBatchWriting(final Batch<Object> batch) {
        this.jobRecord.updateJobReport();
        this.saveJobReport();
    }

    void saveJobReport() {
        this.crud.update(EASY_BATCH_JOB_RECORD, this.jobRecord);
    }
}
