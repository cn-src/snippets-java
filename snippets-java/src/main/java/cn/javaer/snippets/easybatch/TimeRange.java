package cn.javaer.snippets.easybatch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author cn-src
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeRange {
    private LocalDateTime dataStartTime;
    private LocalDateTime dataEndTime;
}