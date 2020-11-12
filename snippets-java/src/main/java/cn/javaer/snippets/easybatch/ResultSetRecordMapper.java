/*
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
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
