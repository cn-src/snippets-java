package cn.javaer.snippets.jooq.codegen.withentity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table
public class EasyBatchJobTime {
    private String jobName;

    private LocalDateTime dataStartTime;
}
