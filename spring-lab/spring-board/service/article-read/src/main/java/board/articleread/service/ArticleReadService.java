package board.articleread.service;

import board.articleread.client.ArticleClient;
import board.articleread.client.CommentClient;
import board.articleread.client.LikeClient;
import board.articleread.client.ViewClient;
import board.articleread.repository.ArticleQueryModel;
import board.articleread.repository.ArticleQueryModelRepository;
import board.articleread.service.event.handler.EventHandler;
import board.articleread.service.response.ArticleReadResponse;
import board.event.Event;
import board.event.EventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleReadService {
    private final ArticleClient articleClient;
    private final CommentClient commentClient;
    private final LikeClient likeClient;
    private final ViewClient viewClient;
    private final ArticleQueryModelRepository articleQueryModelRepository;
    private final List<EventHandler> eventHandlers;

    public void handleEvent(Event<EventPayload> event) {
        for (EventHandler eventHandler : eventHandlers) {
            if (eventHandler.support(event)) {
                eventHandler.handle(event);
            }
        }
    }

    public ArticleReadResponse read(Long articleId) {
        ArticleQueryModel articleQueryModel = articleQueryModelRepository.read(articleId)
                .or(() -> fetch(articleId))
                .orElseThrow();

        return ArticleReadResponse.from(articleQueryModel, viewClient.count(articleId));
    }

    private Optional<ArticleQueryModel> fetch(Long articleId) {
        Optional<ArticleQueryModel> articleQueryModel = articleClient.read(articleId)
                .map(article -> ArticleQueryModel.create(
                        article, commentClient.count(articleId), likeClient.count(articleId)));
        articleQueryModel.ifPresent(a -> articleQueryModelRepository.create(a, Duration.ofDays(1)));
        log.info("[ArticleReadService.fetch()] articleId: {}, articleQueryModel: {}", articleId, articleQueryModel);
        return articleQueryModel;
    }
}
