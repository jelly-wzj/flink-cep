package com.roc.stellar.dsl;

import com.roc.stellar.exceptions.ParseException;
import org.apache.flink.streaming.api.datastream.DataStream;

public interface StellarFunction {
    <T> DataStream<T> apply(Context context) throws ParseException;
}