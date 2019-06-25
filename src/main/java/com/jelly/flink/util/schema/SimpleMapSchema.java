package com.jelly.flink.util.schema;

import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.apache.flink.util.Preconditions.checkNotNull;

public class SimpleMapSchema implements DeserializationSchema<Map<String, String>>, SerializationSchema<Map<String, String>> {

    private static final long serialVersionUID = 1L;

    /**
     * The charset to use to convert between strings and bytes.
     * The field is transient because we serialize a different delegate object instead
     */
    private transient Charset charset;

    /**
     * Creates a new SimpleStringSchema that uses "UTF-8" as the encoding.
     */
    public SimpleMapSchema() {
        this(StandardCharsets.UTF_8);
    }

    /**
     * Creates a new SimpleStringSchema that uses the given charset to convert between strings and bytes.
     *
     * @param charset The charset to use to convert between strings and bytes.
     */
    public SimpleMapSchema(Charset charset) {
        this.charset = checkNotNull(charset);
    }

    /**
     * Gets the charset used by this schema for serialization.
     *
     * @return The charset used by this schema for serialization.
     */
    public Charset getCharset() {
        return charset;
    }


    @Override
    public Map deserialize(byte[] message) throws IOException {
        return JSON.parseObject(new String(message, charset), Map.class);
    }

    @Override
    public boolean isEndOfStream(Map nextElement) {
        return false;
    }

    @Override
    public byte[] serialize(Map element) {
        return JSON.toJSONString(element).getBytes();
    }

    @Override
    public TypeInformation<Map<String, String>> getProducedType() {
        return Types.MAP(Types.STRING, Types.STRING);
    }
}
