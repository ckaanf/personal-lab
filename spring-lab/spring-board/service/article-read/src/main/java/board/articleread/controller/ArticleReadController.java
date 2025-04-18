package board.articleread.controller;

import board.articleread.service.ArticleReadService;
import board.articleread.service.response.ArticleReadPageResponse;
import board.articleread.service.response.ArticleReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleReadController {
    private final ArticleReadService articleReadService;

    @GetMapping("/{articleId}")
    public ArticleReadResponse read(@PathVariable Long articleId) {
        return articleReadService.read(articleId);
    }

    @GetMapping
    public ArticleReadPageResponse readAll(@RequestParam Long boardId,
                                           @RequestParam Long offset,
                                           @RequestParam Long pageSize) {
        return articleReadService.readAll(boardId, offset, pageSize);
    }

    @GetMapping("/infinite-scroll")
    public List<ArticleReadResponse> readAllInfiniteScroll(@RequestParam Long boardId,
                                                           @RequestParam(required = false) Long lastArticleId,
                                                           @RequestParam Long pageSize) {
        return articleReadService.readAllInfiniteScroll(boardId, lastArticleId, pageSize);
    }
}
