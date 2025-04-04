package board.comment.service;

import board.comment.entity.Comment;
import board.comment.repository.CommentRepository;
import board.comment.service.request.CommentCreateRequest;
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
public class CommentService {
    private final Snowflake snowflake = new Snowflake();
    private final CommentRepository commentRepository;


    @Transactional
    public CommentResponse create(CommentCreateRequest request) {
        Comment parent = findParent(request);
        Comment comment = commentRepository.save(
                Comment.create(
                        snowflake.nextId(),
                        request.getContent(),
                        parent == null ? null : parent.getCommentId(),
                        request.getArticleId(),
                        request.getWriterId()
                )
        );
        return CommentResponse.from(comment);
    }

    public CommentResponse read(Long commentId) {
        return CommentResponse.from(
                commentRepository.findById(commentId)
                        .orElseThrow()
        );
    }

    @Transactional
    public void delete(Long commentId) {
        commentRepository.findById(commentId)
                .filter(not(Comment::getDeleted))
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
                commentRepository.findAll(articleId, (page - 1) * pageSize, pageSize).stream()
                        .map(CommentResponse::from).toList(),
                commentRepository.count(articleId, PageLimitCalculator.calculatePageLimit(page, pageSize, 10L))
        );
    }

    public List<CommentResponse> readAll(Long articleId, Long lastParentCommentId, Long lastCommentId, Long limit) {
        List<Comment> comments = lastParentCommentId == null || lastCommentId == null ?
                commentRepository.findAllInfiniteScroll(articleId, limit) :
                commentRepository.findAllInfiniteScroll(articleId, lastParentCommentId, lastCommentId, limit);
        return comments.stream().map(CommentResponse::from).toList();
    }

    private boolean hasChildren(Comment comment) {
        return commentRepository.countBy(comment.getArticleId(), comment.getCommentId(), 2L) == 2;
    }

    private void delete(Comment comment) {
        commentRepository.delete(comment);
        if (!comment.isRoot()) {
            commentRepository.findById(comment.getParentCommentId())
                    .filter(Comment::getDeleted)
                    .filter(not(this::hasChildren))
                    .ifPresent(this::delete);
        }
    }

    private Comment findParent(CommentCreateRequest request) {
        Long parentCommentId = request.getParentCommentId();
        if (parentCommentId == null) {
            return null;
        }
        return commentRepository.findById(parentCommentId)
                .filter(not(Comment::getDeleted))
                .filter(Comment::isRoot)
                .orElseThrow();
    }
}
