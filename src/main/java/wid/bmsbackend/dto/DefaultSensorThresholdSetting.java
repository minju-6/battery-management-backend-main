package wid.bmsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import wid.bmsbackend.entity.SensorThresholdSetting;

@Component
@ConfigurationProperties(prefix = "sensor.threshold")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class DefaultSensorThresholdSetting extends SensorThresholdSetting {
}
