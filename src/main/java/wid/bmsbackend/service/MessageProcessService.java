package wid.bmsbackend.service;

import org.springframework.messaging.Message;

public interface MessageProcessService {

    void processMessage(Message<String> message);
}
