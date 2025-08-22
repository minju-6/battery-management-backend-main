package wid.bmsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wid.bmsbackend.entity.DeviceCategory;

public interface DeviceCategoryRepository extends JpaRepository<DeviceCategory, Long> {
}
