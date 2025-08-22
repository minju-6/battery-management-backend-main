package wid.bmsbackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import wid.bmsbackend.entity.DeviceMessage;
import wid.bmsbackend.entity.LoraMessage;
import wid.bmsbackend.exception.InvalidLoraMessageFormatException;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Random;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("sample-data")
@Primary
public class LoraMessageConverterWithMockData implements LoraMessageConverter {
    private final ObjectMapper objectMapper;
    @Override
    public LoraMessage convert(Message<String> message) {
        String body = message.getPayload();
        try {
            log.debug("Converting message body to LoraMessage: {}", body);
            LoraMessage originLoraMessage = objectMapper.readValue(body, LoraMessage.class);

            return LoraMessage.builder()
                    .applicationID(originLoraMessage.getApplicationID())
                    .applicationName(originLoraMessage.getApplicationName())
                    .deviceName(originLoraMessage.getDeviceName())
                    .devEUI(originLoraMessage.getDevEUI())
                    .loraRxInfo(originLoraMessage.getLoraRxInfo())
                    .loRaTxInfo(originLoraMessage.getLoRaTxInfo())
                    .adr(originLoraMessage.isAdr())
                    .dr(originLoraMessage.getDr())
                    .fCnt(originLoraMessage.getFCnt())
                    .fPort(originLoraMessage.getFPort())
                    .data(generateSampleData())
                    .objectJSON(originLoraMessage.getObjectJSON())
                    .build();
        } catch (JsonProcessingException e) {
            log.error("Error while converting message body to LoraMessage: {}", e.getMessage());
            throw new InvalidLoraMessageFormatException(e);
        }
    }

    private String generateSampleData() {
        int temp = randomInt(-100, 450);
        int current = randomInt(0, 9999);
        int voltage = randomInt(0, 999);
        int impedance = randomInt(0, 9999);

        int[] randomValues = {temp, current, voltage, impedance};
        ByteBuffer buffer = ByteBuffer.allocate(randomValues.length * 2);

        for (int value : randomValues) {
            buffer.putShort((short) value);
        }

        //        DeviceMessage
        return Base64.getEncoder().encodeToString(buffer.array());
    }

    public int randomInt(int min, int max) {
        return (new Random().nextInt(max - min + 1) + min);
    }
}
