package cn.javaer.snippets.jooq.codegen.withentity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * easy-batch 的执行记录.
 *
 * @author cn-src
 */
@Data
@Setter(AccessLevel.PROTECTED)
@Table
public class EasyBatchJobRecord {
    @Id
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
}
