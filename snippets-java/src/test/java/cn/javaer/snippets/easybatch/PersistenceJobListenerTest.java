package cn.javaer.snippets.easybatch;

import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;

import static cn.javaer.snippets.easybatch.TEasyBatchJobRecord.EASY_BATCH_JOB_RECORD;

/**
 * @author cn-src
 */
class PersistenceJobListenerTest {
    @Test
    void name() {
        System.out.println(DSL.using(SQLDialect.H2)
            .createTable(EASY_BATCH_JOB_RECORD)
            .columns(EASY_BATCH_JOB_RECORD.fields())
            .getSQL());
    }
}