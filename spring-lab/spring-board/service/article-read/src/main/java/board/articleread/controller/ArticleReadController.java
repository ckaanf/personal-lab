package board.articleread.controller;

import board.articleread.service.ArticleReadService;
import board.articleread.service.response.ArticleReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleReadController {
    private final ArticleReadService articleReadService;

    @GetMapping("/{articleId}")
    public ArticleReadResponse read(@PathVariable Long articleId) {
        return articleReadService.read(articleId);
    }
}
