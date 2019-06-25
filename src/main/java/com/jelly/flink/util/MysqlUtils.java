package com.jelly.flink.util;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class MysqlUtils {
    public DruidDataSource dataSource;


    public MysqlUtils(String jdbcUrl, String jdbcUserName, String jdbcPassword) {
        dataSource = new DruidDataSource();

        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUserName);
        dataSource.setPassword(jdbcPassword);

        // 默认配置
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(5);
        dataSource.setMaxActive(20);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 1 FROM DUAL");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setPoolPreparedStatements(false);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
//        dataSource.setFilters("stat,wall,log4j");
        dataSource.setConnectionProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000");
    }


    public Connection getConn() throws SQLException {
        return dataSource.getConnection();
    }

    public void close(Connection connection) throws SQLException {
        if (null != connection) {
            connection.close();
        }
    }

    /**
     * 查询
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> query(String sql) throws SQLException {
        final Connection conn = getConn();
        try {
            QueryRunner queryRunner = new QueryRunner();
            return queryRunner.query(conn, sql, new MapListHandler());
        } finally {
            close(conn);
        }
    }

    /**
     * 更新
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public int update(String sql) throws SQLException {
        final Connection conn = getConn();
        try {
            QueryRunner queryRunner = new QueryRunner();
            return queryRunner.update(conn, sql);
        } finally {
            close(conn);
        }
    }

    /**
     * 插入
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public Long insert(String sql) throws SQLException {
        final Connection conn = getConn();
        try {
            QueryRunner queryRunner = new QueryRunner();
            return queryRunner.insert(conn, sql, new ScalarHandler<>());
        } finally {
            close(conn);
        }
    }

}
    
    
