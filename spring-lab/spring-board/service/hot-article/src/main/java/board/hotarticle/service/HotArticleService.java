package board.hotarticle.service;

import board.event.Event;
import board.event.EventPayload;
import board.event.EventType;
import board.hotarticle.client.ArticleClient;
import board.hotarticle.repository.HotArticleListRepository;
import board.hotarticle.service.eventhandler.EventHandler;
import board.hotarticle.service.response.HotArticleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotArticleService {
    private final ArticleClient articleClient;
    private final List<EventHandler> eventHandlers;
    private final HotArticleScoreUpdater hotArticleScoreUpdater;
    private final HotArticleListRepository hotArticleListRepository;

    public void handleEvent(Event<EventPayload> event) {
        EventHandler<EventPayload> eventHandler = findEventHandler(event);
        if (eventHandler == null) {
            log.error("[HotArticleService.handleEvent] No handler found for event: {}", event);
            return;
        }

        if (isArticleCreatedOrDeleted(event)) {
            eventHandler.handle(event);
        } else {
            hotArticleScoreUpdater.updateScore(event, eventHandler);
        }
    }

    public List<HotArticleResponse> readAll(String dateStr) {
        // yyyyMMdd
        return hotArticleListRepository.readAll(dateStr).stream()
                .map(articleClient::read)
                .filter(Objects::nonNull)
                .map(HotArticleResponse::from)
                .toList();
    }


    private EventHandler<EventPayload> findEventHandler(Event<EventPayload> event) {
        return eventHandlers.stream()
                .filter(eventHandler -> eventHandler.supports(event))
                .findAny()
                .orElse(null);
    }

    private boolean isArticleCreatedOrDeleted(Event<EventPayload> event) {
        return EventType.ARTICLE_CREATED == event.getType() || EventType.ARTICLE_DELETED == event.getType();
    }
}
