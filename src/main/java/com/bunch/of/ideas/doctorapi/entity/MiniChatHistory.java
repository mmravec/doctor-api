package com.bunch.of.ideas.doctorapi.entity;

import java.util.ArrayList;
import java.util.List;

public class MiniChatHistory {
    private String userEmail;
    private List<Integer> responses = new ArrayList<>();
    private static final int TOTAL_QUESTIONS = 5;

    public MiniChatHistory(String userEmail) {
        this.userEmail = userEmail;
    }

    public void addResponse(int response) {
        if (responses.size() < TOTAL_QUESTIONS) {
            responses.add(response);
        }
    }

    public boolean isComplete() {
        return responses.size() == TOTAL_QUESTIONS;
    }

    public double calculateAverageScore() {
        return responses.stream().mapToInt(Integer::intValue).average().orElse(0);
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<Integer> getResponses() {
        return responses;
    }

    public void setResponses(List<Integer> responses) {
        this.responses = responses;
    }
}
