package com.bunch.of.ideas.doctorapi.repository;

import com.bunch.of.ideas.doctorapi.entity.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {

    @Query("SELECT COUNT(u) FROM UserMessage u WHERE u.userEmail = :email AND DATE(u.createdAt) = :date")
    int countMessagesByUserAndDate(String email, LocalDate date);
}
