package wid.bmsbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wid.bmsbackend.dto.DeviceMessageResponse;
import wid.bmsbackend.dto.SearchOptions;
import wid.bmsbackend.entity.BatteryCell;
import wid.bmsbackend.entity.DeviceMessage;
import wid.bmsbackend.repository.BatteryCellRepository;
import wid.bmsbackend.repository.DeviceMessageRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceMessageService {
    private final DeviceMessageRepository deviceMessageRepository;
    private final BatteryCellRepository batteryCellRepository;

    @Transactional
    public void save(DeviceMessage deviceMessage) {
        deviceMessageRepository.save(deviceMessage);
    }

    @Transactional(readOnly = true)
    public Page<DeviceMessageResponse> findAll(Pageable pageable, SearchOptions searchOptions) {
        if (isCategorySearchCondition(searchOptions)) {
            List<BatteryCell> batteryCells = batteryCellRepository.findByDeviceCategoryId(searchOptions.getCategoryId());
            searchOptions.setEuiList(batteryCells.stream().map(BatteryCell::getEui).toList());
        }

        Page<DeviceMessage> allWithSearchOptions = deviceMessageRepository.findAllWithSearchOptions(pageable, searchOptions);

        return allWithSearchOptions.map((DeviceMessageResponse::of));
    }

    private static boolean isCategorySearchCondition(SearchOptions searchOptions) {
        return searchOptions.getCategoryId() != null && searchOptions.getCategoryId() > 0L;
    }

    @Transactional(readOnly = true)
    public Page<DeviceMessageResponse> findByEui(String eui, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime start;
        LocalDateTime end;
        if(startDate != null && endDate != null) {
            start = startDate.atStartOfDay();
            end = endDate.atStartOfDay().plusDays(1).plusSeconds(-1);
            return deviceMessageRepository.findByEuiAndCreatedDateBetweenOrderByCreatedDateDesc(eui, start, end, pageable)
                    .map((DeviceMessageResponse::of));
        }
        else {
            if (eui == null || eui.equals("null")) {
                return new PageImpl<>(new ArrayList<>());
            }

            return deviceMessageRepository.findByEuiOrderByCreatedDateDesc(eui, pageable)
                    .map((DeviceMessageResponse::of));
        }
    }
}