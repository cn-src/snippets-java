package cn.javaer.snippets.easybatch;

import org.jeasy.batch.core.record.Batch;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.util.Utils;
import org.jeasy.batch.core.writer.RecordWriter;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.simpleflatmapper.jdbc.spring.SqlParameterSourceFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author cn-src
 */
public class SfmJdbcRecordWriter<P> implements RecordWriter<P> {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String query;
    private final SqlParameterSourceFactory<P> parameterSourceFactory;

    public SfmJdbcRecordWriter(final JdbcTemplate jdbcTemplate, final String query,
                               final Class<P> clazz) {
        Utils.checkNotNull(jdbcTemplate, "jdbcTemplate");
        Utils.checkNotNull(query, "query");
        Utils.checkNotNull(query, "clazz");

        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.query = query;
        this.parameterSourceFactory =
            JdbcTemplateMapperFactory.newInstance().newSqlParameterSourceFactory(clazz);
    }

    public SfmJdbcRecordWriter(final JdbcTemplate jdbcTemplate, final String query,
                               final SqlParameterSourceFactory<P> parameterSourceFactory) {
        Utils.checkNotNull(jdbcTemplate, "jdbcTemplate");
        Utils.checkNotNull(query, "query");
        Utils.checkNotNull(parameterSourceFactory, "parameterSourceFactory");

        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.query = query;
        this.parameterSourceFactory = parameterSourceFactory;
    }

    @Override
    public void writeRecords(final Batch<P> batch) throws Exception {
        final List<P> collect = StreamSupport.stream(batch.spliterator(), false)
            .map(Record::getPayload).collect(Collectors.toList());
        this.jdbcTemplate.batchUpdate(this.query,
            this.parameterSourceFactory.newSqlParameterSources(collect));
    }
}