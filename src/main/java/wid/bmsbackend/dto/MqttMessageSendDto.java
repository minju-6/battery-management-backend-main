package wid.bmsbackend.dto;

import lombok.Data;

@Data
public class MqttMessageSendDto {
    private boolean confirmed;
    private int fPort = 10;
    private String data;
}
