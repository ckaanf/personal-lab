package board.comment.controller;

import board.comment.service.CommentServiceV2;
import board.comment.service.request.CommentCreateRequestV2;
import board.comment.service.response.CommentPageResponse;
import board.comment.service.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/comments")
public class CommentControllerV2 {
    private final CommentServiceV2 commentService;

    @GetMapping("/{commentId}")
    public CommentResponse read(@PathVariable Long commentId) {
        return commentService.read(commentId);
    }

    @PostMapping
    public CommentResponse create(@RequestBody CommentCreateRequestV2 request) {
        return commentService.create(request);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long commentId) {
        commentService.delete(commentId);
    }

    @GetMapping
    public CommentPageResponse readAll(@RequestParam Long articleId,
                                       @RequestParam Long page,
                                       @RequestParam Long pageSize) {
        return commentService.readAll(articleId, page, pageSize);
    }

    @GetMapping("/infinite-scroll")
    public List<CommentResponse> readAll(@RequestParam Long articleId,
                                         @RequestParam(required = false) String lastPath,
                                         @RequestParam Long pageSize) {
        return commentService.readAllInfiniteScroll(articleId, lastPath, pageSize);
    }
}
