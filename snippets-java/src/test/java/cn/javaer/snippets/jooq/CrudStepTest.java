package cn.javaer.snippets.jooq;

import lombok.Value;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStepN;
import org.jooq.SQLDialect;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cn-src
 */
class CrudStepTest {
    DSLContext dsl = DSL.using(SQLDialect.POSTGRES);
    CrudStep crudStep = new CrudStep(this.dsl);

    @Test
    void insertStep() {
        final InsertValuesStepN<?> step = this.crudStep.insertStep(new Demo(1L, "name"));
        assertThat(this.dsl.renderInlined(step))
            .isEqualTo("insert into demo (id, name) values (1, 'name')");
    }

    @Test
    void batchInsertStep() {
        final InsertValuesStepN<?> step = this.crudStep.batchInsertStep(
            Arrays.asList(new Demo(1L, "name1"), new Demo(2L, "name2")));
        assertThat(this.dsl.renderInlined(step))
            .isEqualTo("insert into demo (id, name) values (1, 'name1'), (2, 'name2')");
    }

    @Test
    void dynamicUpdate() {
        final UpdateSetMoreStep<?> step = this.crudStep.dynamicUpdate(new Demo(null, "name"));
        assertThat(this.dsl.renderInlined(step))
            .isEqualTo("update demo set name = 'name'");
    }

    @Value
    static class Demo {
        Long id;
        String name;
    }
}