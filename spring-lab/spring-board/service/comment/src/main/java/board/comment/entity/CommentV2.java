package board.comment.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Table(name = "comment_v2")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class CommentV2 {
    @Id
    private Long commentId;
    private String content;
    private Long articleId; // shard key
    private Long writerId;
    @Embedded
    private CommentPath commentPath;
    private Boolean deleted;
    private LocalDateTime createdAt;

    public static CommentV2 create(Long commentId, String content, Long articleId, Long writerId,
                                   CommentPath commentPath) {
        CommentV2 comment = new CommentV2();
        comment.commentId = commentId;
        comment.content = content;
        comment.articleId = articleId;
        comment.writerId = writerId;
        comment.commentPath = commentPath;
        comment.deleted = false;
        comment.createdAt = LocalDateTime.now();
        return comment;
    }

    public boolean isRoot() {
        return commentPath.isRoot();
    }

    public void delete() {
        deleted = true;
    }
}
