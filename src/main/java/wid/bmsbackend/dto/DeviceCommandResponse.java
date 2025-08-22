package wid.bmsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wid.bmsbackend.entity.DeviceCommand;
import wid.bmsbackend.entity.DeviceCommandType;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceCommandResponse {
    private String eui;
    private DeviceCommandType command;
    private LocalDateTime createdDate;

    public static DeviceCommandResponse of(DeviceCommand deviceCommand) {
        return DeviceCommandResponse.builder()
                .eui(deviceCommand.getEui())
                .command(deviceCommand.getCommand())
                .createdDate(deviceCommand.getCreatedDate())
                .build();
    }
}
