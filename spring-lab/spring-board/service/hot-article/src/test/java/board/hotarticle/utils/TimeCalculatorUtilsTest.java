package board.hotarticle.utils;

import org.assertj.core.api.recursive.comparison.DualValue;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class TimeCalculatorUtilsTest {

    @Test
    void test() {
        Duration duration = TimeCalculatorUtils.calculateDurationToMidnight();
        System.out.println("Duration to midnight: " + duration.getSeconds() / 60);
    }

}