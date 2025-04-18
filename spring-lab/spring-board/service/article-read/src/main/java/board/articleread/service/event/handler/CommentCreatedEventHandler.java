package board.articleread.service.event.handler;

import board.articleread.repository.ArticleQueryModelRepository;
import board.event.Event;
import board.event.EventType;
import board.event.payload.ArticleDeletedEventPayload;
import board.event.payload.CommentCreatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentCreatedEventHandler implements EventHandler<CommentCreatedEventPayload> {
    private final ArticleQueryModelRepository articleQueryModelRepository;

    @Override
    public void handle(Event<CommentCreatedEventPayload> event) {
        articleQueryModelRepository.read(event.getPayload().getArticleId())
                .ifPresent(articleQueryModel -> {
                    articleQueryModel.updateBy(event.getPayload());
                    articleQueryModelRepository.update(articleQueryModel);
                });

    }

    @Override
    public boolean support(Event<CommentCreatedEventPayload> event) {
        return EventType.COMMENT_CREATED.equals(event.getType());
    }

}
