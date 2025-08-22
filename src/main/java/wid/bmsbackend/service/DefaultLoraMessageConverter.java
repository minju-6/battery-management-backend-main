package wid.bmsbackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import wid.bmsbackend.entity.LoraMessage;
import wid.bmsbackend.exception.InvalidLoraMessageFormatException;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultLoraMessageConverter implements LoraMessageConverter {
    private final ObjectMapper objectMapper;

    @Override
    public LoraMessage convert(Message<String> message) {
        String body = message.getPayload();
        try {
            log.debug("Converting message body to LoraMessage: {}", body);
            return objectMapper.readValue(body, LoraMessage.class);
        } catch (JsonProcessingException e) {
            log.error("Error while converting message body to LoraMessage: {}", e.getMessage());
            throw new InvalidLoraMessageFormatException(e);
        }
    }
}