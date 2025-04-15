package board.hotarticle.service;

import board.hotarticle.repository.ArticleCommentCountRepository;
import board.hotarticle.repository.ArticleLikeCountRepository;
import board.hotarticle.repository.ArticleViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HotArticleScoreCalculator {
    private final ArticleLikeCountRepository articleLikeCountRepository;
    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleCommentCountRepository articleCommentCountRepository;

    private static final long ARTICLE_LIKE_COUNT_WEIGHT = 3L;
    private static final long ARTICLE_COMMENT_COUNT_WEIGHT = 2L;
    private static final long ARTICLE_VIEW_COUNT_WEIGHT = 1L;

    public long calculate(Long articleId) {
        Long articleLikeCount = articleLikeCountRepository.read(articleId);
        Long articleCommentCount = articleCommentCountRepository.read(articleId);
        Long articleViewCount = articleViewCountRepository.read(articleId);
        return (articleLikeCount * ARTICLE_LIKE_COUNT_WEIGHT) +
                (articleCommentCount * ARTICLE_COMMENT_COUNT_WEIGHT) +
                (articleViewCount * ARTICLE_VIEW_COUNT_WEIGHT);
    }
}
