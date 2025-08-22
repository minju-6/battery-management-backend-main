package wid.bmsbackend.service;

import org.springframework.messaging.Message;
import wid.bmsbackend.entity.LoraMessage;

public interface LoraMessageConverter {

    LoraMessage convert(Message<String> message);
}
