package com.library.service;

import com.library.controller.response.StatResponse;
import com.library.repository.DailyStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DailyStatQueryService {

    private final DailyStatRepository dailyStatRepository;

    public StatResponse findQueryCount(String query, LocalDate date) {
        long count = dailyStatRepository.countByQueryAndEventDateTimeBetween(
                query,
                date.atStartOfDay(),
                date.atTime(LocalTime.MAX));
        return new StatResponse(query, count);
    }

    public List<StatResponse> findTopNQuery(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return dailyStatRepository.findTopQuery(pageable);
    }
}
