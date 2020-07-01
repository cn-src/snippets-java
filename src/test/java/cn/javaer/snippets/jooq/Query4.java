package cn.javaer.snippets.jooq;

import cn.javaer.snippets.jooq.condition.annotation.ConditionBetweenMax;
import cn.javaer.snippets.jooq.condition.annotation.ConditionBetweenMin;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author cn-src
 */
@Data
@AllArgsConstructor
public class Query4 {

    @ConditionBetweenMin(column = "colName", dateToDateTime = true)
    private LocalDate start;

    @ConditionBetweenMax(column = "colName", dateToDateTime = true)
    private LocalDate end;
}
