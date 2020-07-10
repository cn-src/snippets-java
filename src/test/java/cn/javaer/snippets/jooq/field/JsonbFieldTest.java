package cn.javaer.snippets.jooq.field;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cn-src
 */
class JsonbFieldTest {

    @Test
    void contains() {
        Condition condition = new JsonbField<>("ca", SQLDataType.JSONB, DSL.table("demo"))
                .containsJsonb(JSONB.valueOf("{}"));

        DSLContext dsl = DSL.using(SQLDialect.POSTGRES);
        SelectConditionStep<Record> step = dsl.selectFrom(DSL.table("demo"))
                .where(condition);
        String renderInlined = dsl.renderInlined(step);
        assertThat(renderInlined)
                .isEqualTo("select * from demo where (demo.\"ca\"::jsonb @> cast('{}' as jsonb)::jsonb)");
    }

    @Test
    void containsKv() {
        Condition condition = new JsonbField<>("ca", SQLDataType.JSONB, DSL.table("demo"))
                .containsJsonb("key", "value");

        DSLContext dsl = DSL.using(SQLDialect.POSTGRES);
        SelectConditionStep<Record> step = dsl.selectFrom(DSL.table("demo"))
                .where(condition);
        String renderInlined = dsl.renderInlined(step);
        assertThat(renderInlined)
                .isEqualTo("select * from demo where (demo.\"ca\"::jsonb @> cast('{\"key\":\"value\"}' as jsonb)::jsonb)");
    }
}