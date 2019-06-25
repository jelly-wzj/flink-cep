package com.jelly.flink.sink;

import com.jelly.flink.util.MysqlUtils;
import com.jelly.flink.util.ObjectUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author jenkin
 */
public class FlinkSimpleMysqlSink extends RichSinkFunction<Map<String, Object>> {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(FlinkSimpleMysqlSink.class);
    private MysqlUtils mysqlUtils;

    private String jdbcUrl;
    private String jdbcUserName;
    private String jdbcPassword;
    private String tableName;

    private String selectSqlStr;
    private String insertSqlStr;
    private String updateSqlStr;

    public FlinkSimpleMysqlSink(String jdbcUrl, String jdbcUserName, String jdbcPassword, String tableName) {
        this.jdbcUrl = jdbcUrl;
        this.jdbcUserName = jdbcUserName;
        this.jdbcPassword = jdbcPassword;
        this.tableName = tableName;
        //初始化 sqlString
        selectSqlStr = "select id from " + this.tableName + " where id=%s;";
        insertSqlStr = "insert into " + this.tableName + " (%s) VALUES (%s);";
        updateSqlStr = "update " + this.tableName + "%s where id=%s;";
    }

    @Override
    public void open(Configuration parameters) {
        mysqlUtils = new MysqlUtils(this.jdbcUrl, this.jdbcUserName, this.jdbcPassword);
    }

    @Override
    public void invoke(Map<String, Object> value, Context context) {
        String selectSql = formatSelectSql(value);
        String insertSql = formatInsertSql(value);
        String updateSql = formatUpdateSql(value);

        try {
            final List<Map<String, Object>> queryRes = mysqlUtils.query(selectSql);
            // 有就更新
            if (null != queryRes && queryRes.size() > 0) {
                mysqlUtils.update(updateSql);
                //否则插入
            } else {
                mysqlUtils.insert(insertSql);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private String formatSelectSql(Map<String, Object> value) {
        checkId(value);
        Object id = value.get("id");
        return String.format(selectSqlStr, id instanceof String ? "'" + id + "'" : id);
    }

    private String formatInsertSql(Map<String, Object> value) {
        checkId(value);
        StringBuffer fields = new StringBuffer();
        StringBuffer values = new StringBuffer();
        value.forEach((k, v) -> {
            fields.append(k);
            fields.append(",");
            if (v instanceof String) {
                values.append("'");
                values.append(v);
                values.append("'");
            } else {
                values.append(v);
            }
            values.append(",");
        });
        return String.format(insertSqlStr, fields.substring(0, fields.length() - 1), values.substring(0, values.length() - 1));
    }

    private String formatUpdateSql(Map<String, Object> value) {
        checkId(value);
        Object id = value.remove("id");
        StringBuffer values = new StringBuffer(" set ");
        value.forEach((k, v) -> {
            values.append(k);
            values.append("=");
            if (v instanceof String) {
                values.append("'");
                values.append(v);
                values.append("'");
            } else {
                values.append(v);
            }
            values.append(",");
        });
        return String.format(updateSqlStr, values.substring(0, values.length() - 1), id instanceof String ? "'" + id + "'" : id);
    }

    private void checkId(Map<String, Object> value) {
        if (null == value || value.size() == 0 || !value.containsKey("id") || ObjectUtils.isNull(value)) {
            throw new IllegalArgumentException("'id' field is required and is not null");
        }
    }

    @Override
    public void close() {
    }
}
