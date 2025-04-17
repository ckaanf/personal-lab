package board.common.outboxmessagerelay.event;

import board.common.outboxmessagerelay.entity.Outbox;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OutboxEvent {
    private Outbox outbox;

    public static OutboxEvent of(final Outbox outbox) {
        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.outbox = outbox;
        return outboxEvent;
    }
}
