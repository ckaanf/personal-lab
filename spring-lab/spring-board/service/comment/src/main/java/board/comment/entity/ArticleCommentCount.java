package board.comment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "article_comment_count")
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ArticleCommentCount {
    @Id
    private Long articleId;
    private Long commentCount;

    public static ArticleCommentCount init(Long articleId, Long commentCount) {
        ArticleCommentCount articleCommentCount = new ArticleCommentCount();
        articleCommentCount.articleId = articleId;
        articleCommentCount.commentCount = commentCount;
        return articleCommentCount;
    }
}
