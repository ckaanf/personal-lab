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

    @PostMapping("/articles/{articleId}/users/{userId}")
    public void like(@PathVariable("articleId") Long articleId, @PathVariable("userId") Long userId) {
        System.out.println("like");
        articleLikeService.like(articleId, userId);
    }

    @DeleteMapping("/articles/{articleId}/users/{userId}")
    public void unlike(@PathVariable("articleId") Long articleId, @PathVariable("userId") Long userId) {
        articleLikeService.unlike(articleId, userId);
    }
}
