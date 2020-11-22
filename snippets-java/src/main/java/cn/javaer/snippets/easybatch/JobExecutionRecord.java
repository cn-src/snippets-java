package cn.javaer.snippets.easybatch;

import cn.javaer.snippets.jackson.Json;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jeasy.batch.core.job.Job;
import org.jeasy.batch.core.job.JobMetrics;
import org.jeasy.batch.core.job.JobReport;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * easy-batch 的执行记录.
 *
 * @author cn-src
 */
@Data
@Setter(AccessLevel.PROTECTED)
public class JobExecutionRecord {
    private String id;
    private String jobName;
    private LocalDateTime jobStartTime;
    private LocalDateTime jobEndTime;
    private LocalDateTime dataStartTime;
    private LocalDateTime dataEndTime;
    private String jobStatus;
    private String batchId;
    private Long readCount;
    private Long writeCount;
    private Long filterCount;
    private Long errorCount;
    private String jobParameters;
    private String lastError;
    private LocalDateTime createdDate;

    private transient JobMetrics jobMetrics;

    public static JobExecutionRecord newRecord(final String jobName) {
        Objects.requireNonNull(jobName, "'jobName' must be not null");

        final JobExecutionRecord record = new JobExecutionRecord();
        record.setId(UUID.randomUUID().toString());
        record.setJobName(jobName);
        record.setCreatedDate(LocalDateTime.now());
        return record;
    }

    public static JobExecutionRecord newRecord(final String jobName, final String batchId,
                                               final LocalDateTime dataEndTime) {
        Objects.requireNonNull(jobName, "'jobName' must be not null");

        final JobExecutionRecord record = newRecord(jobName);
        record.setBatchId(batchId);
        record.setDataEndTime(dataEndTime);
        return record;
    }

    public static JobExecutionRecord newRecord(final String jobName, final String batchId,
                                               final LocalDateTime dataStartTime,
                                               final LocalDateTime dataEndTime) {
        Objects.requireNonNull(jobName, "'jobName' must be not null");

        final JobExecutionRecord record = newRecord(jobName);
        record.setBatchId(batchId);
        record.setDataStartTime(dataStartTime);
        record.setDataEndTime(dataEndTime);
        return record;
    }

    /**
     * 从 JobReport 中更新记录信息.
     *
     * @param jobReport JobReport
     */
    public void updateFrom(final JobReport jobReport) {

        if (jobReport.getStatus() != null) {
            this.setJobStatus(jobReport.getStatus().name());
        }

        if (jobReport.getLastError() != null) {
            final StringWriter stackTrace = new StringWriter();
            jobReport.getLastError().printStackTrace(new PrintWriter(stackTrace));
            stackTrace.flush();
            this.setLastError(stackTrace.toString());
        }

        if (jobReport.getParameters() != null) {
            this.setJobParameters(Json.DEFAULT.write(jobReport.getParameters()));
        }

        final JobMetrics metrics = jobReport.getMetrics();
        if (null != metrics) {
            this.setJobStartTime(metrics.getStartTime());
            this.setJobEndTime(metrics.getEndTime());
            this.setReadCount(metrics.getReadCount());
            this.setWriteCount(metrics.getWriteCount());
            this.setFilterCount(metrics.getFilterCount());
            this.setErrorCount(metrics.getErrorCount());
        }
    }

    /**
     * 获取 Job 的监控信息并注册给 JobExecutionRecord.
     *
     * @param job Job
     */
    public void registryJobMetrics(final Job job) {
        final Class<?> aClass;
        try {
            aClass = Class.forName("org.jeasy.batch.core.job.BatchJob");
            final Field field = aClass.getDeclaredField("metrics");
            field.setAccessible(true);
            this.jobMetrics = (JobMetrics) field.get(job);
        }
        catch (final ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
