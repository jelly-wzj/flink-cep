package com.jelly.flink.sink;

import com.alibaba.fastjson.JSON;
import com.jelly.flink.util.ObjectUtils;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

import java.io.Serializable;
import java.util.Map;

/**
 * @author jenkin
 * <p>
 * 默认以json字符串存储
 */
public class FlinkSimpleRedisSink implements Serializable {
    private static final long serialVersionUID = 1L;

    private String host;

    public FlinkSimpleRedisSink(String host) {
        this.host = host;
    }

    public RedisSink build() {
        return new RedisSink(new FlinkJedisPoolConfig.Builder().setHost(this.host).build(), new RedisMapper<Map<String, Object>>() {
            @Override
            public RedisCommandDescription getCommandDescription() {
                return new RedisCommandDescription(RedisCommand.SET, null);
            }

            @Override
            public String getKeyFromData(Map<String, Object> data) {
                final Object id = data.get("id");
                if (ObjectUtils.isNull(id)) {
                    throw new IllegalArgumentException("id field is required");
                }
                return id.toString();
            }

            @Override
            public String getValueFromData(Map<String, Object> data) {
                return JSON.toJSONString(data);
            }
        });
    }
}
