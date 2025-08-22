package wid.bmsbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Service;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Table(name = "device_categories")
public class DeviceCategory extends BaseEntity{
    private String name;

    @ManyToOne
    private BatteryModel batteryModel;

    public void updateDeviceCategory(String name, BatteryModel batteryModel){
        this.name = name;
        this.batteryModel = batteryModel;
    }

}
