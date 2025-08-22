package wid.bmsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wid.bmsbackend.entity.BatteryModel;
import wid.bmsbackend.entity.DeviceCategory;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatteryModelRequest {
    private String modelName;
    private double tempHigh;
    private double tempLow;
    private double currentHigh;
    private double currentLow;
    private double voltageHigh;
    private double voltageLow;
    private double impedanceThreshold;
    private String name;

    public BatteryModel toEntityBatteryModel() {
        return BatteryModel.builder()
                .modelName(modelName)
                .tempHigh(tempHigh)
                .tempLow(tempLow)
                .currentHigh(currentHigh)
                .currentLow(currentLow)
                .voltageHigh(voltageHigh)
                .voltageLow(voltageLow)
                .impedanceThreshold(impedanceThreshold)
                .build();
    }

    public DeviceCategory toEntityCategory(BatteryModel batteryModel){
        return DeviceCategory.builder()
                .name(name)
                .batteryModel(batteryModel)
                .build();
    }
}
