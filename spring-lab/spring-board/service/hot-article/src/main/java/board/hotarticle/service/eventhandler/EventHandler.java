package board.hotarticle.service.eventhandler;

import board.event.Event;
import board.event.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
    Long findArticleId(Event<T> event);
}
