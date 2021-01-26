package com.jelly.flink.entity;

import lombok.Data;
import org.apache.flink.streaming.api.datastream.DataStream;

import java.util.LinkedHashMap;

/**
 * @author : jelly.wang
 * @date : Created in 2021-01-26 下午10:15
 * @description:
 */
@Data
public class DataStreamDetail {
    private DataStream<LinkedHashMap<String, Object>> dataStream;
    private String inputStreamId;
    private String outputStreamId;
    private String[] typeList;
    private String[] fieldList;
}
