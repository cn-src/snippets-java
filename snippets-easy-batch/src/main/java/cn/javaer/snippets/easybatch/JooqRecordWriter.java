package cn.javaer.snippets.easybatch;

import cn.javaer.snippets.jooq.CrudReflection;
import cn.javaer.snippets.jooq.JdbcCrud;
import cn.javaer.snippets.jooq.TableMetaProvider;
import org.jeasy.batch.core.record.Batch;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.util.Utils;
import org.jeasy.batch.core.writer.RecordWriter;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author cn-src
 */
public class JooqRecordWriter<P> implements RecordWriter<P> {
    private final JdbcCrud crud;

    public JooqRecordWriter(final JdbcCrud crud) {
        Utils.checkNotNull(crud, "JdbcCrud");
        this.crud = crud;
    }

    public JooqRecordWriter(final DataSource dataSource) {
        Utils.checkNotNull(dataSource, "DataSource");
        this.crud = new JdbcCrud(dataSource);
    }

    @Override
    public void writeRecords(final Batch<P> batch) {
        final List<P> collect = StreamSupport.stream(batch.spliterator(), false)
            .map(Record::getPayload).collect(Collectors.toList());
        @SuppressWarnings("unchecked")
        final Class<P> clazz = (Class<P>) collect.get(0).getClass();
        final TableMetaProvider<P, ?, ?> meta = CrudReflection.getTableMeta(clazz);
        this.crud.batchInsert(meta, collect);
    }
}
