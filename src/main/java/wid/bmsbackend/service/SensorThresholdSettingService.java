package wid.bmsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wid.bmsbackend.dto.DefaultSensorThresholdSetting;
import wid.bmsbackend.dto.SensorThresholdSettingDto;
import wid.bmsbackend.entity.SensorThresholdSetting;
import wid.bmsbackend.repository.SensorThresholdSettingRepository;

@Service
@RequiredArgsConstructor
public class SensorThresholdSettingService {
    private final DefaultSensorThresholdSetting defaultSensorThresholdSetting;
    private final SensorThresholdSettingRepository sensorThresholdSettingRepository;

    public SensorThresholdSetting getThreshold() {
        return sensorThresholdSettingRepository.findById(1L).orElseGet(
                this::newDefaultThresholdSetting
        );
    }

    public SensorThresholdSettingDto getThresholdDto() {
        return SensorThresholdSettingDto.fromEntity(getThreshold());
    }


    @Transactional
    public void updateThreshold(SensorThresholdSettingDto sensorThresholdSettingDto) {
        var sensorThresholdSetting = sensorThresholdSettingRepository.findById(1L).orElseGet(
                this::newDefaultThresholdSetting
        );
        sensorThresholdSetting.updateThreshold(
                sensorThresholdSettingDto.tempHigh(),
                sensorThresholdSettingDto.tempLow(),
                sensorThresholdSettingDto.voltageHigh(),
                sensorThresholdSettingDto.voltageLow(),
                sensorThresholdSettingDto.impedanceHigh(),
                sensorThresholdSettingDto.impedanceLow(),
                sensorThresholdSettingDto.currentHigh(),
                sensorThresholdSettingDto.currentLow()
        );
    }

    private SensorThresholdSetting newDefaultThresholdSetting() {
        var setting = SensorThresholdSetting.builder()
                .id(1L)
                .currentHigh(defaultSensorThresholdSetting.getCurrentHigh())
                .currentLow(defaultSensorThresholdSetting.getCurrentLow())
                .impedanceHigh(defaultSensorThresholdSetting.getImpedanceHigh())
                .impedanceLow(defaultSensorThresholdSetting.getImpedanceLow())
                .tempHigh(defaultSensorThresholdSetting.getTempHigh())
                .tempLow(defaultSensorThresholdSetting.getTempLow())
                .voltageHigh(defaultSensorThresholdSetting.getVoltageHigh())
                .voltageLow(defaultSensorThresholdSetting.getVoltageLow())
                .build();
        return sensorThresholdSettingRepository.save(setting);
    }
}
