package com.library.service;

import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.controller.response.StatResponse;
import com.library.entity.DailyStat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class BookApplicationService {
    private final BookQueryService bookQueryService;
    private final DailyStatCommandService dailyStatCommandService;
    private final DailyStatQueryService dailyStatQueryService;

    public PageResult<SearchResponse> search(String query, int page, int size) {
        // 외부 api 호출 시 통계 저장
        // TODO 외부 API 호출 후 DB 통계 저장 완료 될 때 까지 조회 값을 받아볼 수 없음 CQRS?
        PageResult<SearchResponse> response = bookQueryService.search(query, page, size);
        LocalDateTime eventDateTime = LocalDateTime.now();

        DailyStat dailyStat = new DailyStat(query, eventDateTime);
        dailyStatCommandService.save(dailyStat);

        return response;
    }

    public StatResponse findQueryCount(String query, LocalDate date) {
        return dailyStatQueryService.findQueryCount(query, date);
    }
}
