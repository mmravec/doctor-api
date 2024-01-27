package com.bunch.of.ideas.doctorapi.repository;

import com.bunch.of.ideas.doctorapi.entity.MentalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MentalHistoryRepository extends JpaRepository<MentalHistory, String> {

    @Query("SELECT mh FROM MentalHistory mh WHERE mh.email = :email AND mh.createDate BETWEEN :startDate AND :endDate ORDER BY mh.createDate")
    List<MentalHistory> findByEmailAndDateRange(@Param("email") String email, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(m) FROM MentalHistory m WHERE m.email = :email AND DATE(m.createDate) = CURRENT_DATE")
    int countTodayEntriesByEmail(@Param("email") String email);
}
