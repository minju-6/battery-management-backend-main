package wid.bmsbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoraMessageConverterWithMockDataTest {
    private LoraMessageConverterWithMockData converter;
    @BeforeEach
    void setUp() {
        converter = new LoraMessageConverterWithMockData(new ObjectMapper());
    }

    @Test
    @DisplayName("Random 데이터의 범위를 검증한다")
    void randomDataRange() {
        for (int i = 0; i < 1000; i++) {
            int randomData = converter.randomInt(-10, 100);
            assertTrue(randomData >= -10 && randomData <= 100);
        }
    }
}