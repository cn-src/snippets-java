//package cn.javaer.snippets;
//
//import org.postgresql.ds.PGSimpleDataSource;
//import org.testcontainers.containers.JdbcDatabaseContainer;
//
//import javax.sql.DataSource;
//
///**
// * @author cn-src
// */
//public class TestContainer {
//
//    public static DataSource createDataSource(JdbcDatabaseContainer<?> container) {
//
//        PGSimpleDataSource dataSource = new PGSimpleDataSource();
//        dataSource.setUrl(container.getJdbcUrl());
//        dataSource.setUser(container.getUsername());
//        dataSource.setPassword(container.getPassword());
//
//        return dataSource;
//    }
//
//    public static DataSource createDataSource(DataSourceInfo info) {
//
//        PGSimpleDataSource dataSource = new PGSimpleDataSource();
//        dataSource.setUrl(info.getJdbcUrl());
//        dataSource.setUser(info.getUsername());
//        dataSource.setPassword(info.getPassword());
//
//        return dataSource;
//    }
//
//    public static DataSourceInfo createDataSourceInfo(JdbcDatabaseContainer<?> container) {
//        return DataSourceInfo.builder()
//                .jdbcUrl(container.getJdbcUrl())
//                .username(container.getUsername())
//                .password(container.getPassword())
//                .build();
//    }
//}
