package board.articleread.service.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ArticleReadPageResponse {
    private List<ArticleReadResponse> articles;
    private Long articleCount;

    public static ArticleReadPageResponse of(List<ArticleReadResponse> articles, Long articleCount) {
        ArticleReadPageResponse response = new ArticleReadPageResponse();
        response.articles = articles;
        response.articleCount = articleCount;
        return response;
    }
}
