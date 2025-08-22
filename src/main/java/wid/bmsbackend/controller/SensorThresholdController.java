package wid.bmsbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wid.bmsbackend.dto.SensorThresholdSettingDto;
import wid.bmsbackend.service.SensorThresholdSettingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/threshold")
public class SensorThresholdController {
    private final SensorThresholdSettingService sensorThresholdSettingService;

    @GetMapping()
    public SensorThresholdSettingDto getSensorThresholdSetting() {
        return sensorThresholdSettingService.getThresholdDto();
    }
    @PutMapping()
    public void updateThreshold(@Valid @RequestBody SensorThresholdSettingDto sensorThresholdSettingDto) {
        sensorThresholdSettingService.updateThreshold(sensorThresholdSettingDto);
    }
}
