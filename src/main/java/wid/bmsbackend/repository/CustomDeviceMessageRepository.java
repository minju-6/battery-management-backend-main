package wid.bmsbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wid.bmsbackend.dto.SearchOptions;
import wid.bmsbackend.entity.DeviceMessage;

public interface CustomDeviceMessageRepository {
    Page<DeviceMessage> findAllWithSearchOptions(Pageable pageable, SearchOptions deviceMessageSearchOptions);
}
