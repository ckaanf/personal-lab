package board.like.repository;

import board.like.entity.ArticleLikeCount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleLikeCountRepository extends JpaRepository<ArticleLikeCount, Long> {
    // select ... for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ArticleLikeCount> findLockedByArticleId(Long articleId);


    @Query(
            value = "update article_like_count set like_count = like_count + 1 where article_id = :articleId",
            nativeQuery = true
    )
    @Modifying
    int increase(@Param("articleId") Long articleId);

    @Query(
            value = "update article_like_count set like_count = like_count - 1 where article_id = :articleId",
            nativeQuery = true
    )
    @Modifying
    int decrease(@Param("articleId") Long articleId);
}