package wid.bmsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wid.bmsbackend.entity.BatteryModel;

@Repository
public interface BatteryModelRepository extends JpaRepository<BatteryModel, Long> {
    BatteryModel findByModelName(String modelName);
}
