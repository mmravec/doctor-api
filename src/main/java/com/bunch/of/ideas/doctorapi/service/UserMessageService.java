package com.bunch.of.ideas.doctorapi.service;

import com.bunch.of.ideas.doctorapi.entity.UserMessage;
import com.bunch.of.ideas.doctorapi.repository.UserMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class UserMessageService {

    @Autowired
    private UserMessageRepository repository;

    public boolean canSendMessage(String userEmail) {
        LocalDate today = LocalDate.now();
        int messageCount = repository.countMessagesByUserAndDate(userEmail, today);
        return messageCount < 5;
    }

    public void saveMessage(String userEmail, String userMessage, String response){
        UserMessage message = new UserMessage();
        message.setUserEmail(userEmail);
        message.setCreatedAt(LocalDateTime.now());
        message.setMessage(userMessage);
        message.setResponse(response);
        this.repository.save(message);
    }
}
