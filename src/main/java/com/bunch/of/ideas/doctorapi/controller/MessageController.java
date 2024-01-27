package com.bunch.of.ideas.doctorapi.controller;

import com.bunch.of.ideas.doctorapi.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @Autowired
    private UserMessageService userMessageService;

    @GetMapping("/canSendMessage/{userEmail}")
    public ResponseEntity<Boolean> canSendMessage(@PathVariable String userEmail) {
        boolean canSend = userMessageService.canSendMessage(userEmail);
        return ResponseEntity.ok(canSend);
    }
}
