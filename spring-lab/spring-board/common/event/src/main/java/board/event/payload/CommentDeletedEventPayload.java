package board.event.payload;

import board.event.EventPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDeletedEventPayload implements EventPayload {
    private Long commentId;
    private String content;
    private String path;
    private Long articleId;
    private Long writerId;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private Long articleCommentCount;
}
