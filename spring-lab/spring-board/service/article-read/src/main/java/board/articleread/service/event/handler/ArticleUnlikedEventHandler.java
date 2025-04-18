package board.articleread.service.event.handler;

import board.articleread.repository.ArticleQueryModelRepository;
import board.event.Event;
import board.event.EventType;
import board.event.payload.ArticleLikedEventPayload;
import board.event.payload.ArticleUnlikedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleUnlikedEventHandler implements EventHandler<ArticleUnlikedEventPayload> {
    private final ArticleQueryModelRepository articleQueryModelRepository;

    @Override
    public void handle(Event<ArticleUnlikedEventPayload> event) {
        articleQueryModelRepository.read(event.getPayload().getArticleId())
                .ifPresent(articleQueryModel -> {
                    articleQueryModel.updateBy(event.getPayload());
                    articleQueryModelRepository.update(articleQueryModel);
                });
    }

    @Override
    public boolean support(Event<ArticleUnlikedEventPayload> event) {
        return EventType.ARTICLE_UNLIKED.equals(event.getType());
    }

}
