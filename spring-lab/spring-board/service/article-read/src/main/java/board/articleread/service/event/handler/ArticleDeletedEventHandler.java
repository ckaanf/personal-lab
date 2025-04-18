package board.articleread.service.event.handler;

import board.articleread.repository.ArticleQueryModelRepository;
import board.event.Event;
import board.event.EventType;
import board.event.payload.ArticleDeletedEventPayload;
import board.event.payload.ArticleUpdatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {
    private final ArticleQueryModelRepository articleQueryModelRepository;

    @Override
    public void handle(Event<ArticleDeletedEventPayload> event) {
        ArticleDeletedEventPayload payload = event.getPayload();
        articleQueryModelRepository.delete(payload.getArticleId());

    }

    @Override
    public boolean support(Event<ArticleDeletedEventPayload> event) {
        return EventType.ARTICLE_DELETED.equals(event.getType());
    }

}
