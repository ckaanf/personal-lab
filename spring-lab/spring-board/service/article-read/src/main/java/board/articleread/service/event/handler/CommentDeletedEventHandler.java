package board.articleread.service.event.handler;

import board.articleread.repository.ArticleQueryModelRepository;
import board.event.Event;
import board.event.EventType;
import board.event.payload.ArticleDeletedEventPayload;
import board.event.payload.CommentCreatedEventPayload;
import board.event.payload.CommentDeletedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentDeletedEventHandler implements EventHandler<CommentDeletedEventPayload> {
    private final ArticleQueryModelRepository articleQueryModelRepository;

    @Override
    public void handle(Event<CommentDeletedEventPayload> event) {
        CommentDeletedEventPayload payload = event.getPayload();
        articleQueryModelRepository.delete(payload.getArticleId());
    }

    @Override
    public boolean support(Event<CommentDeletedEventPayload> event) {
        return EventType.COMMENT_DELETED.equals(event.getType());
    }

}
