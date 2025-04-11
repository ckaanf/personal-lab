package board.comment.service;

import board.comment.entity.ArticleCommentCount;
import board.comment.entity.CommentPath;
import board.comment.entity.CommentV2;
import board.comment.repository.ArticleCommentCountRepository;
import board.comment.repository.CommentRepositoryV2;
import board.comment.service.request.CommentCreateRequestV2;
import board.comment.service.response.CommentPageResponse;
import board.comment.service.response.CommentResponse;
import board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class CommentServiceV2 {
    private final Snowflake snowflake = new Snowflake();
    private final CommentRepositoryV2 commentRepositoryV2;
    private final ArticleCommentCountRepository articleCommentCountRepository;

    @Transactional
    public CommentResponse create(CommentCreateRequestV2 request) {
        CommentV2 parent = findParent(request);
        CommentPath parentCommentPath = parent == null ? CommentPath.create("") : parent.getCommentPath();
        CommentV2 response = commentRepositoryV2.save(
                CommentV2.create(
                        snowflake.nextId(),
                        request.getContent(),
                        request.getArticleId(),
                        request.getWriterId(),
                        parentCommentPath.createChildCommentPath(
                                commentRepositoryV2.findDescendantTopPath(request.getArticleId(), parentCommentPath.getPath())
                                        .orElse(null)
                        )
                )
        );
        int result = articleCommentCountRepository.increase(request.getArticleId());
        if (result == 0) {
            articleCommentCountRepository.save(ArticleCommentCount.init(request.getArticleId(), 1L));
        }
        return CommentResponse.from(response);
    }

    public CommentResponse read(Long commentId) {
        return CommentResponse.from(
                commentRepositoryV2.findById(commentId)
                        .orElseThrow()
        );
    }

    @Transactional
    public void delete(Long commentId) {
        commentRepositoryV2.findById(commentId)
                .filter(not(CommentV2::getDeleted))
                .ifPresent(comment -> {
                    if (hasChildren(comment)) {
                        comment.delete();
                    } else {
                        delete(comment);
                    }
                });

    }

    public CommentPageResponse readAll(Long articleId, Long page, Long pageSize) {
        return CommentPageResponse.of(
                commentRepositoryV2.findAll(articleId, (page - 1) * pageSize, pageSize).stream()
                        .map(CommentResponse::from)
                        .toList(),
                commentRepositoryV2.count(articleId, PageLimitCalculator.calculatePageLimit(page, pageSize, 10L))
        );
    }

    public List<CommentResponse> readAllInfiniteScroll(Long articleId, String lastPath, Long pageSize) {
        List<CommentV2> comments = lastPath == null ?
                commentRepositoryV2.findAllInfiniteScroll(articleId, pageSize) :
                commentRepositoryV2.findAllInfiniteScroll(articleId, lastPath, pageSize);
        return comments.stream()
                .map(CommentResponse::from)
                .toList();
    }

    private boolean hasChildren(CommentV2 comment) {
        return commentRepositoryV2.findDescendantTopPath(
                comment.getArticleId(),
                comment.getCommentPath().getPath()
        ).isPresent();
    }

    private void delete(CommentV2 comment) {
        commentRepositoryV2.delete(comment);
        articleCommentCountRepository.decrease(comment.getArticleId());
        if (!comment.isRoot()) {
            commentRepositoryV2.findByPath(comment.getCommentPath().getParentPath())
                    .filter(CommentV2::getDeleted)
                    .filter(not(this::hasChildren))
                    .ifPresent(this::delete);
        }
    }

    private CommentV2 findParent(CommentCreateRequestV2 request) {
        String parentPath = request.getParentPath();
        if (parentPath == null) {
            return null;
        }
        return commentRepositoryV2.findByPath(parentPath).filter(not(CommentV2::getDeleted)).orElseThrow();
    }

    public Long count(Long articleId) {
        return articleCommentCountRepository.findById(articleId)
                .map(ArticleCommentCount::getCommentCount)
                .orElse(0L);
    }
}
