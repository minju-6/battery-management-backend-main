package wid.bmsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wid.bmsbackend.dto.BatteryModelRequest;
import wid.bmsbackend.dto.BatteryModelResponse;
import wid.bmsbackend.entity.BatteryModel;
import wid.bmsbackend.repository.BatteryModelRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatteryModelService {
    private final BatteryModelRepository batteryModelRepository;

    @Transactional(readOnly = true)
    public List<BatteryModelResponse> findAll() {
        return batteryModelRepository.findAll(Sort.by(Sort.Direction.ASC, "modelName")).stream().map(BatteryModelResponse::of).toList();
    }

    @Transactional(readOnly = true)
    public BatteryModel findByModelName(String modelName){
        return batteryModelRepository.findByModelName(modelName);
    }

    @Transactional
    public BatteryModel addBatteryModel(BatteryModelRequest batteryModelRequest){
        BatteryModel existingModel = batteryModelRepository.findByModelName(batteryModelRequest.getModelName());
        return existingModel != null ? existingModel : batteryModelRepository.save(batteryModelRequest.toEntityBatteryModel());
    }

    @Transactional
    public BatteryModel updateBatteryModel(Long modelId, BatteryModelRequest request){
        BatteryModel batteryModel = batteryModelRepository.findById(modelId).orElseThrow(() -> new RuntimeException("Battery Model Not Found"));
        batteryModel.updateBatteryModel(request);
        return batteryModelRepository.save(batteryModel);
    }

    @Transactional
    public void deleteBatteryModel(Long id) {
        batteryModelRepository.deleteById(id);
    }
}
