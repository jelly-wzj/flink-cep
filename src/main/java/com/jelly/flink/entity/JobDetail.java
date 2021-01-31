package com.jelly.flink.entity;

import lombok.Data;

import java.util.List;

/**
 * @author jelly
 * <p>
 * <p>
 * {
 *   "id": "test_1",
 *   "timeType": "processing",
 *   "streamEngine": "com.jelly.flink.stream.SiddhiStreamConverter",
 *   "exp": "from ${0} select timestamp, id, aviator(name,'([\\w0-8]+)@\\w+[\\.\\w+]+') as name, aviator(price+price) as price insert into  outputStream",
 *   "sources": [
 *     {
 *       "id": "inputStream",
 *       "fields": "id String,name String,price Double,timestamp Long",
 *       "type": "kafka",
 *       "host": "172.16.58.181:9092,172.16.58.182:9092,172.16.58.183:9092",
 *       "auth": "",
 *       "storage": "siddhi02"
 *     }
 *   ],
 *   "sinks": [
 *     {
 *       "id": "1",
 *       "type": "hbase",
 *       "host": "172.16.58.181:2181,172.16.58.182:2181,172.16.58.183:2181",
 *       "auth": "",
 *       "storage": "flink"
 *     }
 *   ]
 * }
 */

@Data
public final class JobDetail {
    private String id;
    private String timeType;
    private String streamEngine;
    private String exp;
    private List<SourceDetail> sources;
    private List<SinkDetail> sinks;

    @Data
    public static abstract class AbstractDetail {
        private String id;
        private String type;
        private String host;
        private String auth;
        private String storage;
        private String fields;
    }

    @Data
    public static class SourceDetail extends AbstractDetail {
    }

    @Data
    public static class SinkDetail extends AbstractDetail {
    }
}