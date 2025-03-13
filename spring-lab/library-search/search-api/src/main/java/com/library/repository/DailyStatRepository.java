package com.library.repository;

import com.library.entity.DailyStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface DailyStatRepository extends JpaRepository<DailyStat, Long> {
    long countByQueryAndEventDateTimeBetween(String query, LocalDateTime start, LocalDateTime end);
}