package wid.bmsbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wid.bmsbackend.entity.DeviceMessage;

import java.time.LocalDateTime;

public interface DeviceMessageRepository extends JpaRepository<DeviceMessage, Long>, CustomDeviceMessageRepository {
    Page<DeviceMessage> findByEuiAndCreatedDateBetweenOrderByCreatedDateDesc(String eui, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<DeviceMessage> findByEuiOrderByCreatedDateDesc(String eui, Pageable pageable);
}
