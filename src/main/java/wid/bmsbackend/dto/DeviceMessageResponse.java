package wid.bmsbackend.dto;

import lombok.Builder;
import wid.bmsbackend.entity.DeviceMessage;

import java.time.LocalDateTime;

@Builder
public record DeviceMessageResponse(
        String eui,
        Long msgLogId,
        double temp,
        double current,
        double voltage,
        double impedance,
        Long id,
        DecisionStatus status,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate) {
    public static DeviceMessageResponse of(DeviceMessage deviceMessage) {
        return DeviceMessageResponse.builder()
                .eui(deviceMessage.getEui())
                .msgLogId(deviceMessage.getMsgLogId())
                .temp(deviceMessage.getTemp())
                .current(deviceMessage.getCurrent())
                .voltage(deviceMessage.getVoltage())
                .impedance(deviceMessage.getImpedance())
                .id(deviceMessage.getId())
                .createdDate(deviceMessage.getCreatedDate())
                .lastModifiedDate(deviceMessage.getLastModifiedDate())
                .status(deviceMessage.getStatus())
                .build();
    }
}