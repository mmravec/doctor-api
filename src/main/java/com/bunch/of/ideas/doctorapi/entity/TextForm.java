package com.bunch.of.ideas.doctorapi.entity;

public class TextForm {
    private String text;

    private String threadName;
    // Getters and setters
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
}
