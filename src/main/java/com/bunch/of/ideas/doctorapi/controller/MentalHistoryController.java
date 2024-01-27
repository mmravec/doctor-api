package com.bunch.of.ideas.doctorapi.controller;

import com.bunch.of.ideas.doctorapi.entity.MentalHistory;
import com.bunch.of.ideas.doctorapi.repository.MentalHistoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class MentalHistoryController {

    private final MentalHistoryRepository mentalHistoryRepository;

    public MentalHistoryController(MentalHistoryRepository mentalHistoryRepository) {
        this.mentalHistoryRepository = mentalHistoryRepository;
    }

    @GetMapping("/mentalHistory/weekly/{email}")
    public ResponseEntity<List<MentalHistory>> getMentalHistoryByEmail(@PathVariable String email) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(7);
        List<MentalHistory> history = mentalHistoryRepository.findByEmailAndDateRange(email, startDate, endDate);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/mentalHistory/monthly/{email}")
    public ResponseEntity<List<MentalHistory>> getMentalHistorymonthlyByEmail(@PathVariable String email) {
        LocalDateTime endDate = LocalDateTime.now().minusDays(7);
        LocalDateTime startDate = endDate.minusDays(20);
        List<MentalHistory> history = mentalHistoryRepository.findByEmailAndDateRange(email, startDate, endDate);
        return ResponseEntity.ok(history);
    }
}
