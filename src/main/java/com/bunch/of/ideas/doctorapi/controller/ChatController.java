package com.bunch.of.ideas.doctorapi.controller;

import com.bunch.of.ideas.doctorapi.entity.User;
import com.bunch.of.ideas.doctorapi.repository.UserRepository;
import com.bunch.of.ideas.doctorapi.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class ChatController {

    private final DoctorService doctorService;

    private final UserRepository userRepository;

    public ChatController(DoctorService doctorService, UserRepository userRepository) {
        this.doctorService = doctorService;
        this.userRepository = userRepository;
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestBody String message) {
        String threadName;
        Optional<User> oUser = userRepository.findUserByEmail("Martin@doktor.com");
        if (oUser.isPresent()){
            User user = oUser.get();
            threadName = user.getThreadId();
        }else {
            threadName = this.doctorService.registerThread();
            User u = new User("Martin@doktor.com", "Martin", "", LocalDateTime.now(), LocalDateTime.now());
            u.setThreadId(threadName);
            this.userRepository.save(u);
        }

        // Logika na spracovanie prijatej správy (ak je potrebná)
        System.out.println("Prijatá správa: " + message);

        String responseMsg = this.doctorService.runWorkflow(message, threadName);

        return ResponseEntity.ok(responseMsg);
    }
}
