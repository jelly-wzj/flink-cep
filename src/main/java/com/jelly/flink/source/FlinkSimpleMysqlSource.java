package com.jelly.flink.source;

import com.alibaba.fastjson.JSON;
import com.jelly.flink.util.MysqlUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.List;
import java.util.Map;

/**
 * @author jenkin
 */
public class FlinkSimpleMysqlSource extends RichSourceFunction<String> {
    private static final long serialVersionUID = 1L;
    private MysqlUtils mysqlUtils;

    private String jdbcUrl;
    private String jdbcUserName;
    private String jdbcPassword;
    private String tableName;

    private String selectSqlStr;

    private Integer size = 1000;
    private Long offset = 0L;


    public FlinkSimpleMysqlSource(String jdbcUrl, String jdbcUserName, String jdbcPassword, String tableName) {
        this.jdbcUrl = jdbcUrl;
        this.jdbcUserName = jdbcUserName;
        this.jdbcPassword = jdbcPassword;
        this.tableName = tableName;

        selectSqlStr = "select * from " + this.tableName;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        mysqlUtils = new MysqlUtils(this.jdbcUrl, this.jdbcUserName, this.jdbcPassword);
    }

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        for (; ; ) {
            selectSqlStr += " limit " + offset + "," + size;
            final List<Map<String, Object>> queryList = mysqlUtils.query(selectSqlStr);
            if (null != queryList) {
                int resSize = queryList.size();
                if (resSize >= size) {
                    offset += size;
                    queryList.forEach((m) -> {
                        ctx.collect(JSON.toJSONString(m));
                    });
                    continue;
                } else if (resSize > 0) {
                    queryList.forEach((m) -> {
                        ctx.collect(JSON.toJSONString(m));
                    });
                }
            }
            break;
        }
    }

    @Override
    public void cancel() {

    }
}
