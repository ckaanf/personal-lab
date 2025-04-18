package board.articleread.service.event.handler;

import board.articleread.repository.ArticleIdListRepository;
import board.articleread.repository.ArticleQueryModelRepository;
import board.articleread.repository.BoardArticleCountRepository;
import board.event.Event;
import board.event.EventType;
import board.event.payload.ArticleDeletedEventPayload;
import board.event.payload.ArticleUpdatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {
    private final ArticleIdListRepository articleIdListRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;
    private final ArticleQueryModelRepository articleQueryModelRepository;

    @Override
    public void handle(Event<ArticleDeletedEventPayload> event) {
        ArticleDeletedEventPayload payload = event.getPayload();

        articleIdListRepository.delete(payload.getBoardId(), payload.getArticleId());
        articleQueryModelRepository.delete(payload.getArticleId());
        boardArticleCountRepository.createOrUpdate(payload.getBoardId(), payload.getBoardArticleCount());
    }

    @Override
    public boolean support(Event<ArticleDeletedEventPayload> event) {
        return EventType.ARTICLE_DELETED.equals(event.getType());
    }

}
