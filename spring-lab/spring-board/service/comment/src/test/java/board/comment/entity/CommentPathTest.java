package board.comment.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentPathTest {

    @Test
    void createChildCommentTest() {
        // 00000 처음 댓글
        createChildCommentTest(CommentPath.create(""), null, "00000");

        // 00000
        // ㄴ 00000
        createChildCommentTest(CommentPath.create("00000"), null, "0000000000");

        // 00000
        // ㄴ 00000
        // ㄴ 00001
        createChildCommentTest(CommentPath.create(""), "00000", "00001");

        // 0000z
        // ㄴ abcdz
        //     ㄴ zzzzz
        //        ㄴ zzzzz
        // ㄴ abce0
        createChildCommentTest(CommentPath.create("0000z"), "0000zabcdzzzzzzzz", "0000zabce0");

    }

    @Test
    void createdChildCommentPathIfMaxDepth() {
        assertThatThrownBy(() ->
                CommentPath.create("zzzzz".repeat(5)).createChildCommentPath(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createChildCommentPathIfChunkOverflowTest() {
        // given
        CommentPath commentPath = CommentPath.create("");

        // when, then
        assertThatThrownBy(() -> commentPath.createChildCommentPath("zzzzz"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    void createChildCommentTest(CommentPath commentPath, String descendentsTopPath, String expectedChildPath) {
        CommentPath childCommentPath = commentPath.createChildCommentPath(descendentsTopPath);
        assertThat(childCommentPath.getPath()).isEqualTo(expectedChildPath);
    }

}