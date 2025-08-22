package wid.bmsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wid.bmsbackend.entity.BatteryCell;

import java.util.List;
import java.util.Optional;

public interface BatteryCellRepository extends JpaRepository<BatteryCell, Long> {
    Optional<BatteryCell> findByEui(String eui);
    List<BatteryCell> findByDeviceCategoryId(Long categoryId);
    void deleteAllByDeviceCategoryId(Long categoryId);
}