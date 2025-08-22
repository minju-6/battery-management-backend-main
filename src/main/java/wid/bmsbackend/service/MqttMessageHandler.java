package wid.bmsbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MqttMessageHandler {
    private final List<MessageProcessService> messageProcessServices;

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<String> message) {
        log.info("Received message: {}", message);
        try {
            messageProcessServices.forEach(service -> service.processMessage(message));
        } catch (Exception e) {
            log.error("Error while processing message: {}", e.getMessage());
        }
    }
}
