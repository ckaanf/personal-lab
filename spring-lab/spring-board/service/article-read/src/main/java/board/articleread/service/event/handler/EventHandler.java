package board.articleread.service.event.handler;

import board.event.Event;
import board.event.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(Event<T> event);

    boolean support(Event<T> event);

}
