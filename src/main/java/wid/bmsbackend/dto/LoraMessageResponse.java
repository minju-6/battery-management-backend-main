package wid.bmsbackend.dto;

import lombok.Builder;
import wid.bmsbackend.entity.LoRaTxInfo;
import wid.bmsbackend.entity.LoraMessage;
import wid.bmsbackend.entity.LoraRxInfo;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record LoraMessageResponse(
        String applicationID,
        String applicationName,
        String deviceName,
        String devEUI,
        List<LoraRxInfo> loraRxInfo,
        LoRaTxInfo loRaTxInfo,
        boolean adr,
        int dr,
        int fCnt,
        int fPort,
        String data,
        String objectJSON,
        Long id,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
    public static LoraMessageResponse of(LoraMessage message) {
        return LoraMessageResponse.builder()
                .applicationID(message.getApplicationID())
                .applicationName(message.getApplicationName())
                .deviceName(message.getDeviceName())
                .devEUI(message.getDevEUI())
                .loraRxInfo(message.getLoraRxInfo())
                .loRaTxInfo(message.getLoRaTxInfo())
                .adr(message.isAdr())
                .dr(message.getDr())
                .fCnt(message.getFCnt())
                .fPort(message.getFPort())
                .data(message.getData())
                .objectJSON(message.getObjectJSON())
                .id(message.getId())
                .createdDate(message.getCreatedDate())
                .lastModifiedDate(message.getLastModifiedDate())
                .build();
    }
}
