package com.roc.stream.cep;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.flink.streaming.api.datastream.DataStream;

import java.util.LinkedHashMap;

/**
 * TransformStream
 * <p>
 *
 * @author jelly.wang
 * @create 2021/01/30
 * @description
 */
@Data
@AllArgsConstructor
public class TransformStream {
    private String streamId;
    private DataStream<LinkedHashMap<String, Object>> dataStream;
    private String[] types;
    private String[] fields;
}
