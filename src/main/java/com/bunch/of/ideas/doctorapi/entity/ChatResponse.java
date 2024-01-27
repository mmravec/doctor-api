package com.bunch.of.ideas.doctorapi.entity;

public class ChatResponse {
    private String message;
    private String timestamp;

    public ChatResponse(String respons, String formattedTime) {
        this.message=respons;
        this.timestamp=formattedTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
