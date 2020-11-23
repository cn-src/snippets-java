package cn.javaer.snippets.easybatch;

import org.jeasy.batch.core.record.Batch;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.util.Utils;
import org.jeasy.batch.core.writer.RecordWriter;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateCrud;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.simpleflatmapper.jdbc.spring.SqlParameterSourceFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author cn-src
 */
public class SfmJdbcRecordWriter<P> implements RecordWriter<P> {

    private final Consumer<List<P>> call;

    public SfmJdbcRecordWriter(final JdbcTemplate jdbcTemplate, final String query,
                               final Class<P> clazz) {
        this(jdbcTemplate, query, JdbcTemplateMapperFactory.newInstance()
            .newSqlParameterSourceFactory(clazz));
    }

    public SfmJdbcRecordWriter(final JdbcTemplate jdbcTemplate, final String query,
                               final SqlParameterSourceFactory<P> parameterSourceFactory) {
        Utils.checkNotNull(jdbcTemplate, "jdbcTemplate");
        Utils.checkNotNull(query, "query");
        Utils.checkNotNull(parameterSourceFactory, "parameterSourceFactory");
        this.call = (list) -> new NamedParameterJdbcTemplate(jdbcTemplate)
            .batchUpdate(query, parameterSourceFactory.newSqlParameterSources(list));
    }

    public SfmJdbcRecordWriter(final JdbcTemplateCrud<P, ?> crud) {
        this.call = crud::create;
    }

    @Override
    public void writeRecords(final Batch<P> batch) throws Exception {
        final List<P> collect = StreamSupport.stream(batch.spliterator(), false)
            .map(Record::getPayload).collect(Collectors.toList());
        this.call.accept(collect);
    }
}