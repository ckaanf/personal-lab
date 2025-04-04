package board.article.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PageLimitCalculatorTest {

    @Test
    void  calculatePageLimitTest() {
        calculatePageLimitTest(1L, 30L, 10L, 301L);
    }

    void calculatePageLimitTest(Long page, Long pageSize, Long movablePageCount, Long expected) {
        Long result = PageLimitCalculator.calculatePageLimit(page, pageSize, movablePageCount);
        assertThat(result).isEqualTo(expected);
    }

}