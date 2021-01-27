package com.jelly.flink.stream.groovy;

public class Event {
    private String topic;
    private int partition;
    private long offset;
    private String value;
    private String key;

    @Override
    public String toString() {
        return "Event{" +
                "topic='" + topic + '\'' +
                ", partition=" + partition +
                ", offset=" + offset +
                ", value='" + value + '\'' +
                ", key='" + key + '\'' +
                '}';

    }


    public Event() {
    }

    public Event(String key, String value, String topic, int partition, long offset) {
        this.key = key;
        this.value = value;
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
    }


    public String getKey() {
        return key;
    }


    public void setKey(String key) {
        this.key = key;
    }

    public String getTopic() {

        return topic;

    }

    public void setTopic(String topic) {

        this.topic = topic;

    }


    public int getPartition() {

        return partition;

    }


    public void setPartition(int partition) {

        this.partition = partition;

    }


    public long getOffset() {

        return offset;

    }


    public void setOffset(long offset) {

        this.offset = offset;

    }


    public String getValue() {

        return value;

    }


    public void setValue(String Value) {

        this.value = value;

    }

}