package wid.bmsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wid.bmsbackend.dto.BatteryModelRequest;
import wid.bmsbackend.dto.DeviceCategoryResponse;
import wid.bmsbackend.entity.BatteryModel;
import wid.bmsbackend.entity.DeviceCategory;
import wid.bmsbackend.repository.DeviceCategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceCategoryService {
    private final DeviceCategoryRepository deviceCategoryRepository;
    private final BatteryModelService batteryModelService;

    @Transactional(readOnly = true)
    public List<DeviceCategoryResponse> findAll() {
        return deviceCategoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream().map(DeviceCategoryResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public void addCategoryWithBatteryModel(BatteryModelRequest request) {
        BatteryModel batteryModel = batteryModelService.addBatteryModel(request);
        DeviceCategory deviceCategory = request.toEntityCategory(batteryModel);
        deviceCategoryRepository.save(deviceCategory);
    }

    @Transactional
    public void updateCategoryWithBatteryModel(Long id, BatteryModelRequest request) {
        DeviceCategory deviceCategory = deviceCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Device Category Not Found"));
        BatteryModel byModelName = batteryModelService.findByModelName(request.getModelName());
        BatteryModel batteryModel = byModelName != null ? batteryModelService.updateBatteryModel(byModelName.getId(), request) : batteryModelService.addBatteryModel(request);
        deviceCategory.updateDeviceCategory(request.getName(), batteryModel);
        deviceCategoryRepository.save(deviceCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        deviceCategoryRepository.deleteById(id);
    }
}
