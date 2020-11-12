package cn.javaer.snippets.easybatch;

import org.jeasy.batch.core.mapper.RecordMapper;
import org.jeasy.batch.core.record.GenericRecord;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.jdbc.JdbcRecord;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;

import java.sql.ResultSet;

/**
 * A {@link RecordMapper} that maps database rows to domain objects.
 *
 * 使用 sfm-jdbc 来从 ResultSet 到实体对象的转换。
 *
 * @author cn-src
 */
public class ResultSetRecordMapper<P> implements RecordMapper<JdbcRecord, Record<P>> {

    private final JdbcMapper<P> mapper;

    /**
     * Create a new {@link ResultSetRecordMapper}. Column names will be fetched from the jdbc result
     * set meta data
     * and set to fields with the same name of the target object.
     *
     * @param recordClass the target domain object class
     */
    public ResultSetRecordMapper(final Class<P> recordClass) {
        this.mapper = JdbcMapperFactory
            .newInstance()
            .newMapper(recordClass);
    }

    public ResultSetRecordMapper(final JdbcMapper<P> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Record<P> processRecord(final JdbcRecord record) throws Exception {
        final ResultSet resultSet = record.getPayload();
        final P p = this.mapper.map(resultSet);
        return new GenericRecord<>(record.getHeader(), p);
    }
}
