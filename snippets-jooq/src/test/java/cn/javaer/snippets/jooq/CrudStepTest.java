package cn.javaer.snippets.jooq;

import lombok.Value;
import org.apache.commons.lang3.ObjectUtils;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStepN;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.SelectLimitStep;
import org.jooq.UpdateConditionStep;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cn-src
 */
class CrudStepTest {
    DSLContext dsl = DSL.using(SQLDialect.POSTGRES);
    CrudStep crudStep = new CrudStep(this.dsl, (AuditorAware<Long>) () -> Optional.of(999L));

    @Test
    void insertStep() {
        final InsertValuesStepN<?> step = this.crudStep.insertStep(
            CrudReflection.getTableMeta(Demo.class), new Demo(1L, "name", 996L));
        assertThat(this.dsl.renderInlined(step))
            .isEqualTo("insert into demo (id, name, created_by_id) values (1, 'name', 999)");
    }

    @Test
    void batchInsertStep() {
        final InsertValuesStepN<?> step = this.crudStep.batchInsertStep(
            CrudReflection.getTableMeta(Demo.class),
            Arrays.asList(new Demo(1L, "name1", 996L), new Demo(2L, "name2", null)));
        assertThat(this.dsl.renderInlined(step))
            .isEqualTo("insert into demo (id, name, created_by_id) " +
                "values (1, 'name1', 999), (2, 'name2', 999)");
    }

    @Test
    void dynamicUpdateStep() {
        final UpdateConditionStep<?> step = this.crudStep.dynamicUpdateStep(
            CrudReflection.getTableMeta(Demo.class), new Demo(3L, "name", null),
            ObjectUtils::isNotEmpty);
        assertThat(this.dsl.renderInlined(step))
            .isEqualTo("update demo set name = 'name' where id = 3");
    }

    @Test
    void findByIdAndCreatorStep() {
        final SelectLimitStep<Record> step = this.crudStep.findByIdAndCreatorStep(
            CrudReflection.getTableMeta(Demo.class), 1L);
        assertThat(this.dsl.renderInlined(step))
            .isEqualTo("select id, name, created_by_id from demo " +
                "where (id = 1 and created_by_id = 999)");
    }

    @Value
    public static class Demo {
        @Id
        Long id;

        String name;

        @CreatedBy
        Long createdById;
    }
}