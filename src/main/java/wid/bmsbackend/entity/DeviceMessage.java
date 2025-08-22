package wid.bmsbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import wid.bmsbackend.dto.DecisionStatus;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Entity
@Table(name = "device_messages", indexes = {
        @Index(name = "idx_device_message_eui_idx", columnList = "eui"),
        @Index(name = "idx_device_message_create_date_idx", columnList = "createdDate")
})
public class DeviceMessage extends BaseEntity{
    @Setter
    private String eui;
    @Setter
    private Long msgLogId;
    private double temp;
    private double current;
    private double voltage;
    private double impedance;
    @Setter
    @Enumerated(EnumType.STRING)
    private DecisionStatus status;

    @Override
    public String toString() {
        return "DeviceMessage{" +
                "temp=" + temp +
                ", current=" + current +
                ", voltage=" + voltage +
                ", impedance=" + impedance +
                '}';
    }
}