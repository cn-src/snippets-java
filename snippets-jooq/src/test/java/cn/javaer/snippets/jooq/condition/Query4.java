package cn.javaer.snippets.jooq.condition;

import cn.javaer.snippets.jooq.condition.annotation.ConditionBetweenMax;
import cn.javaer.snippets.jooq.condition.annotation.ConditionBetweenMin;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author cn-src
 */
@Data
@AllArgsConstructor
public class Query4 {

    @ConditionBetweenMin(column = "colName")
    private LocalDateTime start;

    @ConditionBetweenMax(column = "colName")
    private LocalDateTime end;
}
