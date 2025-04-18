package board.articleread.service.event.handler;

import board.articleread.repository.ArticleIdListRepository;
import board.articleread.repository.ArticleQueryModel;
import board.articleread.repository.ArticleQueryModelRepository;
import board.articleread.repository.BoardArticleCountRepository;
import board.event.Event;
import board.event.EventType;
import board.event.payload.ArticleCreatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ArticleCreatedEventHandler implements EventHandler<ArticleCreatedEventPayload> {
    private final ArticleIdListRepository articleIdListRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;
    private final ArticleQueryModelRepository articleQueryModelRepository;

    @Override
    public void handle(Event<ArticleCreatedEventPayload> event) {
        ArticleCreatedEventPayload payload = event.getPayload();
        articleQueryModelRepository.create(
                ArticleQueryModel.create(payload),
                Duration.ofDays(1)
        );

        articleIdListRepository.add(payload.getBoardId(), payload.getArticleId(), 1000L);
        boardArticleCountRepository.createOrUpdate(payload.getBoardId(), payload.getBoardArticleCount());
    }

    @Override
    public boolean support(Event<ArticleCreatedEventPayload> event) {
        return EventType.ARTICLE_CREATED.equals(event.getType());
    }

}
