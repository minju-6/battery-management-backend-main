package wid.bmsbackend.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TimeUtilTest {

    @Test
    @DisplayName("시간 우선 비교")
    void getCurrentTimeMillis() {
        //integer range (0..5)
        TimeUtil timeUtil = new TimeUtil();
        IntStream.range(0, 100).forEach(i -> {
            long currentTimeMillis = timeUtil.getCurrentTimeMillis();
            long currentTimeMillis2 = timeUtil.getCurrentTimeMillis();
            assertThat(currentTimeMillis).isLessThanOrEqualTo(currentTimeMillis2);
        });
    }

    @Test
    @DisplayName("current time in milliseconds 가 유효 시간 범위 내에 있는지 확인")
    void getCurrentTimeMillisInRange() {
        TimeUtil timeUtil = new TimeUtil();
        long currentTimeMillis = timeUtil.getCurrentTimeMillis();
        long currentTimeMillis2 = System.currentTimeMillis();
        System.out.println(currentTimeMillis);
        assertThat(currentTimeMillis2 - currentTimeMillis).isLessThan(10);
    }

}