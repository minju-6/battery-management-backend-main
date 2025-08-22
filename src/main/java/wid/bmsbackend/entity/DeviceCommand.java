package wid.bmsbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "device_command",
        indexes = @Index(
                name = "idx_eui_command_type", columnList = "eui, command_type"
        )
)
@Getter
@SuperBuilder
@RequiredArgsConstructor
public class DeviceCommand extends BaseEntity {
    @Column
    private String eui;
    @Enumerated(EnumType.STRING)
    @Column(name = "command_type")
    private DeviceCommandType command;

    public DeviceCommand(String eui, DeviceCommandType commandType) {
        this.eui = eui;
        this.command = commandType;
    }

    public void changeCommand(DeviceCommandType newCommand) {
        this.command = newCommand;
    }

}
