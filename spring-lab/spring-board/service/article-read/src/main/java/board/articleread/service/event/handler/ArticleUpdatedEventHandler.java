package board.articleread.service.event.handler;

import board.articleread.repository.ArticleQueryModel;
import board.articleread.repository.ArticleQueryModelRepository;
import board.event.Event;
import board.event.EventType;
import board.event.payload.ArticleCreatedEventPayload;
import board.event.payload.ArticleUpdatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ArticleUpdatedEventHandler implements EventHandler<ArticleUpdatedEventPayload> {
    private final ArticleQueryModelRepository articleQueryModelRepository;

    @Override
    public void handle(Event<ArticleUpdatedEventPayload> event) {
        articleQueryModelRepository.read(event.getPayload().getArticleId())
                .ifPresent(articleQueryModel -> {
                    articleQueryModel.updateBy(event.getPayload());
                    articleQueryModelRepository.update(articleQueryModel);
                });
    }

    @Override
    public boolean support(Event<ArticleUpdatedEventPayload> event) {
        return EventType.ARTICLE_UPDATED.equals(event.getType());
    }

}
