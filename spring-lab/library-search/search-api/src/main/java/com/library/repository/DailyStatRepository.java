package com.library.repository;

import com.library.entity.DailyStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyStatRepository extends JpaRepository<DailyStat, Long> {
}