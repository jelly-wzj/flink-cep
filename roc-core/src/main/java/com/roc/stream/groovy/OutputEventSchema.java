package com.roc.stream.groovy;

import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;

public class OutputEventSchema implements KeyedSerializationSchema<Event> {

    @Override
    public byte[] serializeKey(Event event) {
        return event.getKey().getBytes();
    }

    @Override
    public byte[] serializeValue(Event event) {
        return event.getValue().getBytes();
    }

    @Override
    public String getTargetTopic(Event event) {
        return null;
    }

}