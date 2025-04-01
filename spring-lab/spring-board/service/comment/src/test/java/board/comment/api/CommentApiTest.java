package board.comment.api;

import board.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class CommentApiTest {
    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponse response1 = createComment(new CommentCreateRequest(1L, "comment1", null, 1L));
        CommentResponse response2 = createComment(new CommentCreateRequest(1L, "comment2", response1.getCommentId(), 1L));
        CommentResponse response3 = createComment(new CommentCreateRequest(1L, "comment3", response1.getCommentId(), 1L));

        System.out.println("commentId=%s".formatted(response1.getCommentId()));
        System.out.println("\tcommentId=%s".formatted(response2.getCommentId()));
        System.out.println("\tcommentId=%s".formatted(response3.getCommentId()));
    }

    @Test
    void read() {
        CommentResponse response = restClient.get().uri("/api/v1/comments/{commentId}", 134562979012816896L)
                .retrieve()
                .body(CommentResponse.class);

        System.out.println("response = " + response);
    }

    @Test
    void delete() {
//        commentId=134562979012816896L
//          commentId=134562980619235328L
//          commentId=134562980715704320L
        restClient.delete().uri("/api/v1/comments/{commentId}", 134562979012816896L)
                .retrieve()
                .body(Void.class);
    }

    CommentResponse createComment(CommentCreateRequest request) {
        return restClient.post().uri("/api/v1/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }

    @Getter
    @AllArgsConstructor
    public static class CommentCreateRequest {
        private Long articleId;
        private String content;
        private Long parentCommentId;
        private Long writerId;
    }

}
