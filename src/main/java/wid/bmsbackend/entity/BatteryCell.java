package wid.bmsbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wid.bmsbackend.dto.BatteryCellRequest;

import java.util.Objects;

@Entity
@Table(name = "battery_cells")
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BatteryCell extends BaseEntity{
    @Column(unique = true)
    private String eui;
    private String name;
    private double temp;
    private double current;
    private double voltage;
    private double impedance;
    @ManyToOne
    private DeviceCategory deviceCategory;
    @ManyToOne
    private BatteryModel batteryModel;

    public void updateBattery(BatteryCellRequest batteryCellRequest) {
        this.eui = batteryCellRequest.getEui();
    }

    public void updateCategory(DeviceCategory deviceCategory) {
        this.deviceCategory = deviceCategory;
        this.batteryModel = deviceCategory.getBatteryModel();
    }

    public void updateSensorData( double temp, double voltage, double impedance, double current) {
        this.temp = temp;
        this.voltage = voltage;
        this.impedance = impedance;
        this.current = current;
    }
}
