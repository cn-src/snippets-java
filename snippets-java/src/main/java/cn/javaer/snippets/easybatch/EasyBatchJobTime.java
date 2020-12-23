package cn.javaer.snippets.easybatch;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Setter(AccessLevel.PROTECTED)
public class EasyBatchJobTime {
    private String jobName;
    private LocalDateTime dataStartTime;
}
