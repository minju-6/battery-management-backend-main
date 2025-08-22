package wid.bmsbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wid.bmsbackend.dto.SearchOptions;
import wid.bmsbackend.entity.DeviceCommand;

public interface CustomDeviceCommandRepository {
    Page<DeviceCommand> findAllWithSearchOptions(Pageable pageable, SearchOptions deviceComamndSearchOptions);

}
