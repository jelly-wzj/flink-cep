package com.jelly.flink.entity;

import lombok.Data;
import org.apache.flink.streaming.api.datastream.DataStream;

import java.util.Map;

/**
 * @author jenkin
 */
@Data
public class DataStreamDetail {
    private DataStream<Map<String, Object>> dataStream;
    private String inputStreamId;
    public static String outputStreamId;
    private String[] typeList;
    private String[] fieldList;
}
