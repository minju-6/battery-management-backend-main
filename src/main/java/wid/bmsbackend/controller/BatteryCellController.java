package wid.bmsbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wid.bmsbackend.dto.ApiResponse;
import wid.bmsbackend.dto.BatteryCellRequest;
import wid.bmsbackend.dto.BatteryCellResponse;
import wid.bmsbackend.service.BatteryCellService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/battery-cells")
@CrossOrigin(origins = "*")
public class BatteryCellController  {
    private final BatteryCellService batteryCellService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BatteryCellResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(batteryCellService.findAll()));
    }

    @PostMapping("/add")
    public void addBatteryCell(@RequestBody @Valid BatteryCellRequest batteryCellRequest) {
        batteryCellService.addBatteryCell(batteryCellRequest);
    }

    @PutMapping("/{id}")
    public void updateBatteryCell(@PathVariable Long id, @RequestBody @Valid BatteryCellRequest batteryCellRequest) {
        batteryCellService.updateBatteryCell(id, batteryCellRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteBatteryCell(@PathVariable Long id) {
        batteryCellService.deleteBatteryCell(id);
    }

    @DeleteMapping("/category/{categoryId}")
    public void deleteAll(@PathVariable Long categoryId){
        batteryCellService.deleteAllByCategory(categoryId);
    }

}