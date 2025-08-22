package wid.bmsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wid.bmsbackend.entity.BatteryModel;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatteryModelResponse {
    private Long id;
    private String modelName;
    private double tempHigh;
    private double tempLow;
    private double voltageHigh;
    private double voltageLow;
    private double currentHigh;
    private double currentLow;
    private double impedanceThreshold;

    public static BatteryModelResponse of(BatteryModel batteryModel) {
        return BatteryModelResponse.builder()
                .id(batteryModel.getId())
                .modelName(batteryModel.getModelName())
                .tempHigh(batteryModel.getTempHigh())
                .tempLow(batteryModel.getTempLow())
                .voltageHigh(batteryModel.getVoltageHigh())
                .voltageLow(batteryModel.getVoltageLow())
                .currentHigh(batteryModel.getCurrentHigh())
                .currentLow(batteryModel.getCurrentLow())
                .impedanceThreshold(batteryModel.getImpedanceThreshold())
                .build();
    }
}
