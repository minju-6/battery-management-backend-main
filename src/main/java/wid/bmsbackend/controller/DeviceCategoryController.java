package wid.bmsbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wid.bmsbackend.dto.*;
import wid.bmsbackend.service.DeviceCategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/battery-zone")
@CrossOrigin(origins = "*")
public class DeviceCategoryController {
    private final DeviceCategoryService deviceCategoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeviceCategoryResponse>>> findAll(){
        return ResponseEntity.ok(ApiResponse.success(deviceCategoryService.findAll()));
    }

    @PostMapping("/add-with-model")
    public void addCategoryWithBatteryModel(@RequestBody @Valid BatteryModelRequest request) {
        deviceCategoryService.addCategoryWithBatteryModel(request);
    }

    @PutMapping("/{id}")
    public void updateCategory(@PathVariable("id") Long id, @RequestBody @Valid BatteryModelRequest request){
        deviceCategoryService.updateCategoryWithBatteryModel(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("id") Long id){
        deviceCategoryService.deleteCategory(id);
    }


}
