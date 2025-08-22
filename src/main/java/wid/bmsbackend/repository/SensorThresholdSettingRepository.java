package wid.bmsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wid.bmsbackend.entity.SensorThresholdSetting;

public interface SensorThresholdSettingRepository extends JpaRepository<SensorThresholdSetting, Long> {
}