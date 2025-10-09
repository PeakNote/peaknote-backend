package com.peaknote.demo.dto;
import java.time.Instant;

public class MeetingSummary {
    private String eventId;
    private String topic;
    private Instant startTime;
    private Instant endTime;

    public MeetingSummary(String eventId, String topic, Instant startTime, Instant endTime) {
        this.eventId = eventId;
        this.topic = topic;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }
    
    // Getters and setters (or use Lombok)
}
