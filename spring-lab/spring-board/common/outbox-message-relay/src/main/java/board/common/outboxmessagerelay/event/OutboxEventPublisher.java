package board.common.outboxmessagerelay.event;

import board.common.outboxmessagerelay.MessageRelayConstants;
import board.common.outboxmessagerelay.entity.Outbox;
import board.common.snowflake.Snowflake;
import board.event.Event;
import board.event.EventPayload;
import board.event.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxEventPublisher {
    private final Snowflake outboxIdSnowflake = new Snowflake();
    private final Snowflake eventIdSnowflake = new Snowflake();
    private final ApplicationEventPublisher publisher;

    public void publish(EventType eventType, EventPayload eventPayload, Long shardKey) {
        // articleId = 10, shardKey == articleId
        // 10 % 4 = 물리적 샤드 2
        Outbox outbox = Outbox.create(
                outboxIdSnowflake.nextId(),
                eventType,
                Event.of(
                        eventIdSnowflake.nextId(),
                        eventType,
                        eventPayload
                ).toJson(),
                shardKey % MessageRelayConstants.SHARD_COUNT
        );
        publisher.publishEvent(OutboxEvent.of(outbox));
    }

}
