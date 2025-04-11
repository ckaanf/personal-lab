package board.like.controller;

import board.like.service.ArticleLikeService;
import board.like.service.response.ArticleLikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/article-likes")
public class ArticleLikeController {
    private final ArticleLikeService articleLikeService;

    @GetMapping("/articles/{articleId}/users/{userId}")
    public ArticleLikeResponse read(@PathVariable("articleId") Long articleId, @PathVariable("userId") Long userId) {
        return articleLikeService.read(articleId, userId);
    }

    @GetMapping("/articles/{articleId}/count")
    public Long count(@PathVariable("articleId") Long articleId) {
        return articleLikeService.count(articleId);
    }

    @PostMapping("/articles/{articleId}/users/{userId}/pessimistic-lock-1")
    public void likePessimisticLock1(@PathVariable("articleId") Long articleId, @PathVariable("userId") Long userId) {
        System.out.println("like");
        articleLikeService.likePessimisticLock1(articleId, userId);
    }

    @DeleteMapping("/articles/{articleId}/users/{userId}/pessimistic-lock-1")
    public void unlikePessimisticLock1(@PathVariable("articleId") Long articleId, @PathVariable("userId") Long userId) {
        articleLikeService.unlikePessimisticLock1(articleId, userId);
    }

    @PostMapping("/articles/{articleId}/users/{userId}/pessimistic-lock-2")
    public void likePessimisticLock2(@PathVariable("articleId") Long articleId, @PathVariable("userId") Long userId) {
        System.out.println("like");
        articleLikeService.likePessimisticLock2(articleId, userId);
    }

    @DeleteMapping("/articles/{articleId}/users/{userId}/pessimistic-lock-2")
    public void unlikePessimisticLock2(@PathVariable("articleId") Long articleId, @PathVariable("userId") Long userId) {
        articleLikeService.unlikePessimisticLock2(articleId, userId);
    }

    @PostMapping("/articles/{articleId}/users/{userId}/optimistic-lock")
    public void likeOptimisticLock(@PathVariable("articleId") Long articleId, @PathVariable("userId") Long userId) {
        System.out.println("like");
        articleLikeService.likeOptimisticLock(articleId, userId);
    }

    @DeleteMapping("/articles/{articleId}/users/{userId}/optimistic-lock")
    public void unlikeOptimisticLock(@PathVariable("articleId") Long articleId, @PathVariable("userId") Long userId) {
        articleLikeService.unlikeOptimisticLock(articleId, userId);
    }
}
