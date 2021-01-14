package cn.javaer.snippets.easybatch;

import org.jeasy.batch.core.mapper.RecordMapper;
import org.jeasy.batch.core.record.GenericRecord;
import org.jeasy.batch.core.record.Record;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;

import java.sql.ResultSet;

/**
 * A {@link RecordMapper} that maps database rows to domain objects.
 * <p>
 * 使用 sfm-jdbc 来从 ResultSet 到实体对象的转换，
 * 解决官方自带的 JdbcRecordMapper 不支持下划线映射到驼峰命名的问题。
 *
 * @author cn-src
 */
public class ResultSetRecordMapper<P> implements RecordMapper<ResultSet, P> {

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
            .ignorePropertyNotFound()
            .newMapper(recordClass);
    }

    public ResultSetRecordMapper(final JdbcMapper<P> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Record<P> processRecord(final Record<ResultSet> record) {
        final ResultSet resultSet = record.getPayload();
        final P p = this.mapper.map(resultSet);
        return new GenericRecord<>(record.getHeader(), p);
    }
}
