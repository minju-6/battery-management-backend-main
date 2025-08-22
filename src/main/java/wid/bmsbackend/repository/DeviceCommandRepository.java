package wid.bmsbackend.repository;

import org.antlr.v4.runtime.ListTokenSource;
import org.springframework.data.jpa.repository.JpaRepository;
import wid.bmsbackend.entity.DeviceCommand;
import wid.bmsbackend.entity.DeviceCommandType;

import java.util.List;
import java.util.Optional;

public interface DeviceCommandRepository extends JpaRepository<DeviceCommand, Long>, CustomDeviceCommandRepository {
    Optional<DeviceCommand> findFirstByEuiAndCommandNotInOrderByCreatedDate(String eui, List<DeviceCommandType> command);
}
