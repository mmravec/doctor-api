package com.bunch.of.ideas.doctorapi.controller;

import com.bunch.of.ideas.doctorapi.repository.MentalHistoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final MentalHistoryRepository mentalHistoryRepository;

    public MessageController(MentalHistoryRepository mentalHistoryRepository) {
        this.mentalHistoryRepository = mentalHistoryRepository;
    }

    @GetMapping("/canSendMessage/{userEmail}")
    public ResponseEntity<Boolean> canSendMessage(@PathVariable String userEmail) {
        boolean canSend = canUserParticipate(userEmail);
        return ResponseEntity.ok(canSend);
    }

    public boolean canUserParticipate(String userEmail) {
        return mentalHistoryRepository.countTodayEntriesByEmail(userEmail) == 0;
    }
}
