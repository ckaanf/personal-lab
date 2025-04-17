package board.event;

import board.event.payload.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {
    ARTICLE_CREATED(ArticleCreatedEventPayload.class, Topic.BOARD_ARTICLE),
    ARTICLE_UPDATED(ArticleUpdatedEventPayload.class, Topic.BOARD_ARTICLE),
    ARTICLE_DELETED(ArticleDeletedEventPayload.class, Topic.BOARD_ARTICLE),
    COMMENT_CREATED(CommentCreatedEventPayload.class, Topic.BOARD_COMMENT),
    COMMENT_DELETED(CommentDeletedEventPayload.class, Topic.BOARD_COMMENT),
    ARTICLE_LIKED(ArticleLikedEventPayload.class, Topic.BOARD_LIKE),
    ARTICLE_UNLIKED(ArticleUnlikedEventPayload.class, Topic.BOARD_LIKE),
    ARTICLE_VIEWED(ArticleViewedEventPayload.class, Topic.BOARD_VIEW)
    ;

    private final Class<? extends EventPayload> payloadClass;
    private final String topic;

    public static EventType from(String type) {
        try {
            return valueOf(type);
        } catch (Exception e) {
            log.error("[EventType.from()] type={}", type, e);
            return null;
        }
    }

    public static class Topic {
        public static final String BOARD_ARTICLE = "board-article";
        public static final String BOARD_COMMENT = "board-comment";
        public static final String BOARD_LIKE = "board-like";
        public static final String BOARD_VIEW = "board-view";
    }
}
