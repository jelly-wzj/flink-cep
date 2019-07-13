package com.jelly.flink.entity;

import lombok.Data;

import java.util.List;

/**
 * @author jelly
 * <p>
 * <p>
 * {
 *   "jobId": "test_1",
 *   "runType": "new",
 *   "timeType": "ProcessingTime",
 *   "cql": "from inputStream select timestamp, id, aviator(name,'([\\w0-8]+)@\\w+[\\.\\w+]+') as name, aviator(price+price) as price insert into  outputStream",
 *   "outputStreamId": "outputStream",
 *   "sources": [
 *     {
 *       "id": "inputStream",
 *       "inputFields": "id String,name String,price Double,timestamp Long",
 *       "type": "kafka",
 *       "host": "192.168.204.181:9092,192.168.204.182:9092,192.168.204.183:9092",
 *       "auth": "",
 *       "resource": "siddhi02"
 *     }
 *   ],
 *   "sinks": [
 *     {
 *       "id": "1",
 *       "type": "hbase",
 *       "host": "192.168.204.181:2181,192.168.204.182:2181,192.168.204.183:2181",
 *       "auth": "",
 *       "store": "flink"
 *     }
 *   ]
 * }
 */

@Data
public final class JobDetail {
    private String jobId;
    private String runType;
    private String timeType;
    private String cql;
    private List<SourceDetail> sources;
    private List<SinkDetail> sinks;
    private String outputStreamId;

    @Data
    public static abstract class AbstractDetail {
        private String id;
        private String type;
        private String host;
        private String auth;
    }

    @Data
    public static class SourceDetail extends AbstractDetail {
        private String resource;
        private String inputFields;
    }

    @Data
    public static class SinkDetail extends AbstractDetail {
        private String store;
    }
}