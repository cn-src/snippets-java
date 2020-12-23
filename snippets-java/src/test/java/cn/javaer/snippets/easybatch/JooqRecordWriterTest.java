package cn.javaer.snippets.easybatch;

import cn.javaer.snippets.TestDataSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jeasy.batch.core.job.Job;
import org.jeasy.batch.core.job.JobBuilder;
import org.jeasy.batch.core.job.JobReport;
import org.jeasy.batch.core.reader.IterableRecordReader;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cn-src
 */
class JooqRecordWriterTest {
    DataSource dataSource = null;

    @BeforeEach
    void setUp() {
        this.dataSource = TestDataSource.create();
        final DSLContext dsl = DSL.using(this.dataSource, SQLDialect.H2);
        dsl.dropTableIfExists("DEMO").execute();
        dsl.createTable("DEMO")
            .column("ID", SQLDataType.BIGINT)
            .column("NAME", SQLDataType.VARCHAR)
            .execute();
    }

    @Test
    void writeRecords() {
        final Job job = new JobBuilder<Demo, Demo>()
            .named("demo")
            .reader(new IterableRecordReader<>(Arrays.asList(
                new Demo(1L, "name1"),
                new Demo(2L, "name2"))))
            .writer(new JooqRecordWriter<>(this.dataSource))
            .build();
        final JobReport jobReport = job.call();
        assertThat(jobReport.getMetrics().getReadCount()).isEqualTo(2L);
        assertThat(jobReport.getMetrics().getWriteCount()).isEqualTo(2L);
    }

    @Data
    @AllArgsConstructor
    public static class Demo {
        private Long id;
        private String name;
    }
}