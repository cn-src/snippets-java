package cn.javaer.snippets.easybatch;

import org.jeasy.batch.core.reader.RecordReader;
import org.jeasy.batch.core.record.Header;
import org.jeasy.batch.jdbc.JdbcRecord;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.simpleflatmapper.jdbc.QueryPreparer;
import org.simpleflatmapper.jdbc.named.NamedSqlQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.jeasy.batch.core.util.Utils.checkArgument;
import static org.jeasy.batch.core.util.Utils.checkNotNull;

/**
 * @author cn-src
 */
public class SfmJdbcRecordReader implements RecordReader<ResultSet> {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(SfmJdbcRecordReader.class.getSimpleName());

    private final QueryPreparer<TimeRange> selectQueryPreparer;
    private final DataSource dataSource;
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private final String query;
    private String dataSourceName;
    private long currentRecordNumber;

    private int maxRows;
    private int queryTimeout;
    private int fetchSize;
    private final PersistenceJobRecord jobRecord;

    public SfmJdbcRecordReader(final DataSource dataSource, final String query,
                               final PersistenceJobRecord jobRecord) {
        checkNotNull(dataSource, "dataSource");
        checkNotNull(query, "query");
        checkNotNull(jobRecord, "jobRecord");
        this.dataSource = dataSource;
        this.query = query;
        this.jobRecord = jobRecord;
        this.selectQueryPreparer = JdbcMapperFactory
            .newInstance()
            .from(TimeRange.class)
            .to(NamedSqlQuery.parse(query));
    }

    @Override
    public void open() throws Exception {
        this.currentRecordNumber = 0;
        LOGGER.debug("Opening JDBC connection");

        this.connection = this.dataSource.getConnection();
        final TimeRange timeRange = new TimeRange(this.jobRecord.getDataStartTime(),
            this.jobRecord.getDataEndTime());
        this.statement = this.selectQueryPreparer.prepare(this.connection).bind(timeRange);
//        this.statement = this.connection.prepareStatement(this.query, ResultSet.TYPE_FORWARD_ONLY,
//            ResultSet.CONCUR_READ_ONLY);
//        这种使用方式目前会出现参数下标溢出的问题
//        this.selectQueryPreparer.prepare(this.connection).bindTo(timeRange, this.statement);
        if (this.maxRows >= 1) {
            this.statement.setMaxRows(this.maxRows);
        }
        if (this.fetchSize >= 1) {
            this.statement.setFetchSize(this.fetchSize);
        }
        if (this.queryTimeout >= 1) {
            this.statement.setQueryTimeout(this.queryTimeout);
        }
        this.resultSet = this.statement.executeQuery();
        this.dataSourceName = this.getDataSourceName();
    }

    private boolean hasNextRecord() {
        try {
            return this.resultSet.next();
        }
        catch (final SQLException e) {
            LOGGER.error("Unable to check the existence of next database record", e);
            return false;
        }
    }

    @Override
    public JdbcRecord readRecord() {
        if (this.hasNextRecord()) {
            final Header header = new Header(++this.currentRecordNumber, this.dataSourceName,
                LocalDateTime.now());
            return new JdbcRecord(header, this.resultSet);
        }
        else {
            return null;
        }
    }

    private String getDataSourceName() {
        try {
            return "Connection URL: " +
                this.connection.getMetaData().getURL() +
                " | Query string: " + this.query;
        }
        catch (final SQLException e) {
            LOGGER.error("Unable to get data source name", e);
            return "N/A";
        }
    }

    @Override
    public void close() throws Exception {
        if (this.resultSet != null) {
            this.resultSet.close();
        }
        if (this.statement != null) {
            this.statement.close();
        }
        if (this.connection != null) {
            LOGGER.debug("Closing JDBC connection");
            this.connection.close();
        }
    }

    /**
     * Set the maximum number of rows to fetch.
     *
     * @param maxRows the maximum number of rows to fetch
     */
    public void setMaxRows(final int maxRows) {
        checkArgument(maxRows >= 1, "max rows parameter must be greater than or equal to 1");
        this.maxRows = maxRows;
    }

    /**
     * Set the statement fetch size.
     *
     * @param fetchSize the fetch size to set
     */
    public void setFetchSize(final int fetchSize) {
        checkArgument(fetchSize >= 1, "fetch size parameter must be greater than or equal to 1");
        this.fetchSize = fetchSize;
    }

    /**
     * Set the statement query timeout.
     *
     * @param queryTimeout the query timeout in seconds
     */
    public void setQueryTimeout(final int queryTimeout) {
        checkArgument(queryTimeout >= 1,
            "query timeout parameter must be greater than or equal to 1");
        this.queryTimeout = queryTimeout;
    }
}
