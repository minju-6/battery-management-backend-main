package wid.bmsbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.support.GenericMessage;
import wid.bmsbackend.exception.InvalidLoraMessageFormatException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DefaultLoraMessageConverterTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private DefaultLoraMessageConverter sut;

    @BeforeEach
    void setUp() {
        sut = new DefaultLoraMessageConverter(objectMapper);
    }

    @Test
    @DisplayName("Json 메시지는 LoraMessage Object 로 변환되어야 한다.")
    void convert() {
        GenericMessage<String> message = new GenericMessage<>(DefaultLoraMessageConverterTest.message);
        assertDoesNotThrow(() -> sut.convert(message));
    }
    @Test
    @DisplayName("올바르지 않은 Json 메시지는 InvalidLoraMessageFormatException 예외가 발생한다.")
    void convertInvalidMessage() {
        GenericMessage<String> message = new GenericMessage<>("{invalid}");
        Assertions.assertThrows(InvalidLoraMessageFormatException.class, () -> sut.convert(message));
    }

    private static final String message = "{\n" +
            "    \"applicationID\": \"123\",\n" +
            "    \"applicationName\": \"temperature-sensor\",\n" +
            "    \"deviceName\": \"garden-sensor\",\n" +
            "    \"devEUI\": \"AgICAgICAgI=\",\n" +
            "    \"rxInfo\": [\n" +
            "        {\n" +
            "            \"gatewayID\": \"AwMDAwMDAwM=\",\n" +
            "            \"time\": \"2019-11-08T13:59:25.048445Z\",\n" +
            "            \"timeSinceGPSEpoch\": null,\n" +
            "            \"rssi\": -48,\n" +
            "            \"loRaSNR\": 9,\n" +
            "            \"channel\": 5,\n" +
            "            \"rfChain\": 0,\n" +
            "            \"board\": 0,\n" +
            "            \"antenna\": 0,\n" +
            "            \"location\": {\n" +
            "                \"latitude\": 52.3740364,\n" +
            "                \"longitude\": 4.9144401,\n" +
            "                \"altitude\": 10.5\n" +
            "            },\n" +
            "            \"fineTimestampType\": \"NONE\",\n" +
            "            \"context\": \"9u/uvA==\",\n" +
            "            \"uplinkID\": \"jhMh8Gq6RAOChSKbi83RHQ==\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"txInfo\": {\n" +
            "        \"frequency\": 868100000,\n" +
            "        \"modulation\": \"LORA\",\n" +
            "        \"loRaModulationInfo\": {\n" +
            "            \"bandwidth\": 125,\n" +
            "            \"spreadingFactor\": 11,\n" +
            "            \"codeRate\": \"4/5\",\n" +
            "            \"polarizationInversion\": false\n" +
            "        }\n" +
            "    },\n" +
            "    \"adr\": true,\n" +
            "    \"dr\": 1,\n" +
            "    \"fCnt\": 10,\n" +
            "    \"fPort\": 5,\n" +
            "    \"data\": \"...\",\n" +
            "    \"objectJSON\": \"{\\\"temperatureSensor\\\":25,\\\"humiditySensor\\\":32}\",\n" +
            "    \"tags\": {\n" +
            "        \"key\": \"value\"\n" +
            "    }\n" +
            "}";
}