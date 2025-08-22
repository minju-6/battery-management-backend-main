package wid.bmsbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import wid.bmsbackend.dto.DefaultSensorThresholdSetting;
import wid.bmsbackend.dto.SensorThresholdSettingDto;
import wid.bmsbackend.entity.SensorThresholdSetting;
import wid.bmsbackend.repository.SensorThresholdSettingRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class SensorThresholdSettingServiceTest {
    SensorThresholdSettingService sut;
    SensorThresholdSettingRepository repository;
    DefaultSensorThresholdSetting defaultSetting;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock();
        defaultSetting = DefaultSensorThresholdSetting.builder()
                .currentLow(40.0)
                .currentHigh(100.0)
                .impedanceHigh(10.0)
                .impedanceLow(2.0)
                .tempHigh(50.0)
                .tempLow(10.0)
                .voltageHigh(24)
                .voltageLow(20)
                .build();
        sut = new SensorThresholdSettingService(defaultSetting, repository);
    }

    @Test
    @DisplayName("SensorThresholdSetting 이 존재하지 않는 경우 기본값을 반환한다.")
    void getThreshold_whenSensorThresholdSettingDoesNotExist_returnDefault() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        when(repository.save(Mockito.any())).thenReturn(createSensorThresholdSettingFromDefault(defaultSetting));
//        when(repository.findById(1L)).thenReturn(Optional.of(createSensorThresholdSetting()));
        assertThresholdValues(
                SensorThresholdSettingDto.fromEntity(sut.getThreshold()),
                50.0, 10.0, 24.0, 20.0, 10.0, 2.0, 100.0, 40.0
        );
    }
    @Test
    @DisplayName("SensorThresholdSetting 이 존재하는 경우 값을 해당 오브젝트를 반환한다")
    void getThreshold_whenSensorThresholdSettingExists_returnSensorThresholdSetting() {
        SensorThresholdSetting sensorThresholdSetting = createSensorThresholdSetting();
        when(repository.findById(1L)).thenReturn(Optional.of(sensorThresholdSetting));
        assertThresholdValues(
                SensorThresholdSettingDto.fromEntity(sut.getThreshold()),
                50.0, 10.0, 24.0, 20.0, 10.0, 2.0, 100.0, 40.0
        );
    }

    private static void assertThresholdValues(SensorThresholdSettingDto threshold, double tempHigh, double tempLow, double voltageHigh, double voltageLow, double impedanceHigh, double impedanceLow, double currentHigh, double currentLow) {
        assertThat(threshold).isNotNull();
        assertThat(threshold.tempHigh()).isEqualTo(tempHigh);
        assertThat(threshold.tempLow()).isEqualTo(tempLow);
        assertThat(threshold.voltageHigh()).isEqualTo(voltageHigh);
        assertThat(threshold.voltageLow()).isEqualTo(voltageLow);
        assertThat(threshold.impedanceHigh()).isEqualTo(impedanceHigh);
        assertThat(threshold.impedanceLow()).isEqualTo(impedanceLow);
        assertThat(threshold.currentHigh()).isEqualTo(currentHigh);
        assertThat(threshold.currentLow()).isEqualTo(currentLow);
    }


    static SensorThresholdSetting createSensorThresholdSettingFromDefault(DefaultSensorThresholdSetting ds) {
        return SensorThresholdSetting.builder()
                .id(1L)
                .currentLow(ds.getCurrentLow())
                .currentHigh(ds.getCurrentHigh())
                .impedanceHigh(ds.getImpedanceHigh())
                .impedanceLow(ds.getImpedanceLow())
                .tempHigh(ds.getTempHigh())
                .tempLow(ds.getTempLow())
                .voltageHigh(ds.getVoltageHigh())
                .voltageLow(ds.getVoltageLow())
                .build();
    }

    static SensorThresholdSetting createSensorThresholdSetting() {
        return SensorThresholdSetting.builder()
                .id(1L)
                .currentLow(40.0)
                .currentHigh(100.0)
                .impedanceHigh(10.0)
                .impedanceLow(2.0)
                .tempHigh(50.0)
                .tempLow(10.0)
                .voltageHigh(24)
                .voltageLow(20)
                .build();
    }

}