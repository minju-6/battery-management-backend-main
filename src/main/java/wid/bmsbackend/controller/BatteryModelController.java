package wid.bmsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wid.bmsbackend.dto.BatteryModelRequest;
import wid.bmsbackend.dto.BatteryModelResponse;
import wid.bmsbackend.entity.BatteryModel;
import wid.bmsbackend.service.BatteryModelService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/battery-models")
@CrossOrigin(origins = "*")
public class BatteryModelController {
    private final BatteryModelService batteryModelService;

    @GetMapping
    public List<BatteryModelResponse> findAll() {
        return batteryModelService.findAll();
    }

    @GetMapping("/find")
    public BatteryModel findByName(String modelName) {
        return batteryModelService.findByModelName(modelName);
    }

    @PostMapping()
    public BatteryModel addBatteryModel(@RequestBody BatteryModelRequest request) {
        return batteryModelService.addBatteryModel(request);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody BatteryModelRequest request) {
        batteryModelService.updateBatteryModel(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        batteryModelService.deleteBatteryModel(id);
    }
}
