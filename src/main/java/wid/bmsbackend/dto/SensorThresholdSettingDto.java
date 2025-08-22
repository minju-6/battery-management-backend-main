package wid.bmsbackend.dto;

import wid.bmsbackend.entity.SensorThresholdSetting;

import java.io.Serializable;
import java.time.LocalDateTime;

public record SensorThresholdSettingDto(
        double tempHigh, double tempLow,
        double voltageHigh, double voltageLow, double impedanceHigh,
        double impedanceLow, double currentHigh,
        double currentLow) implements Serializable {

    public SensorThresholdSetting toEntity() {
        return SensorThresholdSetting.builder()
                .currentHigh(currentHigh)
                .currentLow(currentLow)
                .impedanceHigh(impedanceHigh)
                .impedanceLow(impedanceLow)
                .tempHigh(tempHigh)
                .tempLow(tempLow)
                .voltageHigh(voltageHigh)
                .voltageLow(voltageLow)
                .build();
    }
    public static SensorThresholdSettingDto fromEntity(SensorThresholdSetting sensorThresholdSetting) {
        return new SensorThresholdSettingDto(
                sensorThresholdSetting.getTempHigh(),
                sensorThresholdSetting.getTempLow(),
                sensorThresholdSetting.getVoltageHigh(),
                sensorThresholdSetting.getVoltageLow(),
                sensorThresholdSetting.getImpedanceHigh(),
                sensorThresholdSetting.getImpedanceLow(),
                sensorThresholdSetting.getCurrentHigh(),
                sensorThresholdSetting.getCurrentLow()
        );
    }
}