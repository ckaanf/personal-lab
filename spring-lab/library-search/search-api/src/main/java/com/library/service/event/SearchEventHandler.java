package com.library.service.event;

import com.library.entity.DailyStat;
import com.library.service.DailyStatCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchEventHandler {
    private final DailyStatCommandService dailyStatCommandService;

    @Async
    @EventListener
    public void handleEvent(SearchEvent event) throws InterruptedException {
        Thread.sleep(5000L);
        log.info("[SearchEventHandler] handleEvent: {}", event);
        DailyStat dailyStat = new DailyStat(event.query(), event.timeStamp());
        dailyStatCommandService.save(dailyStat);
    }
}
