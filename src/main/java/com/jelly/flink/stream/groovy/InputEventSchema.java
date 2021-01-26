package com.jelly.flink.stream.groovy;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchema;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.apache.flink.api.java.typeutils.TypeExtractor.getForClass;

public class InputEventSchema implements KeyedDeserializationSchema<Event> {

    @Override

    public Event deserialize(byte[] messageKey, byte[] message, String topic, int partition, long offset) throws IOException {

        String msg = new String(message, StandardCharsets.UTF_8);

        String key = null;

        if (messageKey != null) {

            key = new String(messageKey, StandardCharsets.UTF_8);

        }

        return new Event(key, msg, topic, partition, offset);

    }


    @Override
    public boolean isEndOfStream(Event nextElement) {

        return false;

    }

    @Override
    public TypeInformation<Event> getProducedType() {

        return getForClass(Event.class);

    }

}