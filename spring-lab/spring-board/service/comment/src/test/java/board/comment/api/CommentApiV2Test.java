package board.comment.api;

import board.comment.service.response.CommentPageResponse;
import board.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class CommentApiV2Test {
    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponse response1 = create(new CommentCreateRequestV2(1L, "my comment1", null, 1L));
        CommentResponse response2 = create(new CommentCreateRequestV2(1L, "my comment2", response1.getPath(), 1L));
        CommentResponse response3 = create(new CommentCreateRequestV2(1L, "my comment3", response2.getPath(), 1L));

        System.out.println("response1.getCommentId() = " + response1.getCommentId());
        System.out.println("\tresponse2.getCommentId() = " + response2.getCommentId());
        System.out.println("\t\tresponse3.getCommentId() = " + response3.getCommentId());
    }

    /**
     * response1.getCommentId() = 135598417225306112
     * 	response2.getCommentId() = 135598418408099840
     * 		response3.getCommentId() = 135598418496180224
     */
    @Test
    void read() {
        CommentResponse response = restClient.get().uri("/api/v2/comments/{commentId}", 135598417225306112L)
                .retrieve()
                .body(CommentResponse.class);

        System.out.println("response = " + response);
    }

    @Test
    void delete() {
        restClient.delete().uri("/api/v2/comments/{commentId}", 134562979012816896L)
                .retrieve()
                .body(Void.class);
    }

    @Test
    void readAll() {
        CommentPageResponse response = restClient.get().uri("/api/v2/comments?articleId=1&page=1&pageSize=10")
                .retrieve()
                .body(new ParameterizedTypeReference<CommentPageResponse>() {
                });

        System.out.println("response.getCommentCount() = " + response.getCommentCount());
        for (CommentResponse comment : response.getComments()) {
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }
    }

    @Test
    void readAllInfiniteScroll() {
        List<CommentResponse> response1 = restClient.get().uri("/api/v2/comments/infinite-scroll?articleId=1&pageSize=5")
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("firstPage");
        for (CommentResponse comment : response1) {
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }

        String lastPath = response1.getLast().getPath();
        List<CommentResponse> response2 = restClient.get().uri("/api/v2/comments/infinite-scroll?articleId=1&pageSize" +
                        "=5&lastPath={lastPath}", lastPath)
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });
        System.out.println("secondPage");
        for (CommentResponse comment : response2) {
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }

    }

    @Test
    void countTest() {
        CommentResponse response = create(new CommentCreateRequestV2(2L, "my comment1", null, 1L));

        Long count1 = restClient.get().uri("/api/v2/comments/article/{articleId}/count", 2L)
                .retrieve()
                .body(Long.class);
        System.out.println("count1 = " + count1);

        restClient.delete().uri("/api/v2/comments/{commentId}", response.getCommentId())
                .retrieve()
                .body(Void.class);

        Long count2 = restClient.get().uri("/api/v2/comments/article/{articleId}/count", 2L)
                .retrieve()
                .body(Long.class);
        System.out.println("count1 = " + count2);
    }


    CommentResponse create(CommentCreateRequestV2 request) {
        return restClient.post().uri("/api/v2/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }

    @Getter
    @AllArgsConstructor
    public class CommentCreateRequestV2 {
        private Long articleId;
        private String content;
        private String parentPath;
        private Long writerId;
    }

}
