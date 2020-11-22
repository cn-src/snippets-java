package cn.javaer.snippets.easybatch;

import org.jeasy.batch.core.job.JobMetrics;
import org.jeasy.batch.core.job.JobParameters;
import org.jeasy.batch.core.job.JobReport;
import org.jeasy.batch.core.listener.JobListener;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * @author cn-src
 */
public class PersistenceListener implements JobListener {
    private final JdbcTemplate jdbcTemplate;
    private final JobExecutionRecord jobRecord;
    private final WeakHashMap<String, JobMetrics> monitor;

    public PersistenceListener(final JdbcTemplate jdbcTemplate,
                               final JobExecutionRecord jobRecord,
                               final WeakHashMap<String, JobMetrics> monitor) {
        Objects.requireNonNull(jdbcTemplate, "'jdbcTemplate' must be not null");
        Objects.requireNonNull(jobRecord, "'jobRecord' must be not null");

        this.jdbcTemplate = jdbcTemplate;
        this.jobRecord = jobRecord;
        this.monitor = monitor;
    }

    public PersistenceListener(final JdbcTemplate jdbcTemplate,
                               final JobExecutionRecord jobRecord) {
        this(jdbcTemplate, jobRecord, null);
    }

    @Override
    public void beforeJob(final JobParameters jobParameters) {
        if (this.monitor != null) {
            this.monitor.put(this.jobRecord.getBatchId(), this.jobRecord.getJobMetrics());
        }

        if (this.jobRecord.getDataStartTime() == null) {
            final LocalDateTime dataStartTime = this.jdbcTemplate.queryForObject(
                "SELECT data_start_time FROM easybatch_job_time WHERE job_name=?",
                LocalDateTime.class, this.jobRecord.getJobName());
            this.jobRecord.setDataStartTime(dataStartTime);
            this.jdbcTemplate.update("UPDATE easybatch_job_time SET data_start_time=? WHERE " +
                    "job_name=?"
                , this.jobRecord.getDataEndTime(), this.jobRecord.getJobName());
        }
        this.jdbcTemplate.update("INSERT INTO easybatch_job_record (id, job_name, " +
                "job_start_time, job_end_time, job_status, batch_id, " +
                "data_start_time, data_end_time, " +
                "read_count, write_count, filter_count, error_count, " +
                "job_parameters, last_error, created_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
            this.jobRecord.getId(), this.jobRecord.getJobName(),
            this.jobRecord.getJobStartTime(), this.jobRecord.getJobEndTime(),
            this.jobRecord.getJobStatus(), this.jobRecord.getBatchId(),
            this.jobRecord.getDataStartTime(), this.jobRecord.getDataEndTime(),
            this.jobRecord.getReadCount(), this.jobRecord.getWriteCount(),
            this.jobRecord.getFilterCount(), this.jobRecord.getErrorCount(),
            this.jobRecord.getJobParameters(), this.jobRecord.getLastError(),
            this.jobRecord.getCreatedDate());
    }

    @Override
    public void afterJob(final JobReport jobReport) {
        if (this.monitor != null) {
            this.monitor.remove(this.jobRecord.getBatchId());
        }
        
        this.jobRecord.updateFrom(jobReport);
        this.jdbcTemplate.update("UPDATE easybatch_job_record SET  job_name = ?, " +
                "job_start_time = ?, job_end_time = ?, job_status = ?, batch_id = ?," +
                " data_start_time = ?, data_end_time = ?, read_count = ?, write_count = ?, " +
                "filter_count = ?, error_count = ?, job_parameters = ?, " +
                "last_error = ?, created_date = ? WHERE id = ? ",
            this.jobRecord.getJobName(),
            this.jobRecord.getJobStartTime(), this.jobRecord.getJobEndTime(),
            this.jobRecord.getJobStatus(), this.jobRecord.getBatchId(),
            this.jobRecord.getDataStartTime(), this.jobRecord.getDataEndTime(),
            this.jobRecord.getReadCount(), this.jobRecord.getWriteCount(),
            this.jobRecord.getFilterCount(), this.jobRecord.getErrorCount(),
            this.jobRecord.getJobParameters(), this.jobRecord.getLastError(),
            this.jobRecord.getCreatedDate(), this.jobRecord.getId());
    }
}
