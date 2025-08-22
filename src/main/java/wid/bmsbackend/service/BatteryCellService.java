package wid.bmsbackend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wid.bmsbackend.dto.BatteryCellRequest;
import wid.bmsbackend.dto.BatteryCellResponse;
import wid.bmsbackend.dto.SensingValue.SensingValueWrapper;
import wid.bmsbackend.entity.BatteryCell;
import wid.bmsbackend.entity.BatteryModel;
import wid.bmsbackend.entity.DeviceCategory;
import wid.bmsbackend.repository.BatteryCellRepository;
import wid.bmsbackend.repository.BatteryModelRepository;
import wid.bmsbackend.repository.DeviceCategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BatteryCellService {
    private final BatteryCellRepository batteryCellRepository;
    private final DeviceCategoryRepository deviceCategoryRepository;
    private final BatteryModelRepository batteryModelRepository;

    @Transactional(readOnly = true)
    public BatteryCell findByEui(String eui) {
        return batteryCellRepository.findByEui(eui).orElseThrow(() -> new EntityNotFoundException("Battery cell not found"));
    }

    @Transactional(readOnly = true)
    public List<BatteryCellResponse> findAll() {
        return batteryCellRepository.findAll().stream().map((bc) -> {
            BatteryModel batteryModel = batteryModelRepository.findById(bc.getBatteryModel().getId()).orElseThrow(() -> new EntityNotFoundException("배터리 모델이 등록되지 않았음"));
            SensingValueWrapper sensingValues = batteryModel.getSensingValue(bc);

            return BatteryCellResponse.of(
                    bc.getId(),
                    bc.getEui(),
                    bc.getName(),
                    bc.getDeviceCategory(),
                    sensingValues.temp(),
                    sensingValues.current(),
                    sensingValues.voltage(),
                    sensingValues.impedance(),
                    sensingValues.status(),
                    bc.getCreatedDate()
            );
        }
        ).collect(Collectors.toList());
    }

    public void addBatteryCell(BatteryCellRequest batteryCellRequest) {
        DeviceCategory deviceCategory = deviceCategoryRepository.findById(batteryCellRequest.getDeviceCategory())
                .orElseThrow(() -> new RuntimeException("Device category not found"));
        BatteryModel batteryModel = batteryModelRepository.findById(batteryCellRequest.getBatteryModelId()).orElseThrow(() -> new RuntimeException("Battery Model not found"));
        List<BatteryCell> batteryCells = batteryCellRequest.createBatteryCell(deviceCategory, batteryModel);
        batteryCellRepository.saveAll(batteryCells);
    }

    public void updateBatteryCell(Long id, BatteryCellRequest batteryCellRequest) {
        DeviceCategory deviceCategory = deviceCategoryRepository.findById(batteryCellRequest.getDeviceCategory())
                .orElseThrow(() -> new RuntimeException("Device category not found"));
        BatteryCell batteryCell = batteryCellRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Battery cell not found"));
        batteryCell.updateBattery(batteryCellRequest);
        batteryCell.updateCategory(deviceCategory);
        batteryCellRepository.save(batteryCell);
    }

    public void deleteBatteryCell(Long id) {
        batteryCellRepository.deleteById(id);
    }

    public void deleteAllByCategory(Long categoryId) {
        batteryCellRepository.deleteAllByDeviceCategoryId(categoryId);
    }

}
