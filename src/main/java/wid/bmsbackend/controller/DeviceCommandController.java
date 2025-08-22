package wid.bmsbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wid.bmsbackend.dto.ApiResponse;
import wid.bmsbackend.dto.DeviceCommandRequest;
import wid.bmsbackend.dto.DeviceCommandResponse;
import wid.bmsbackend.dto.SearchOptions;
import wid.bmsbackend.service.DeviceCommandService;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/device-commands")
@CrossOrigin(origins = "*")
@Slf4j
public class DeviceCommandController {
    private final DeviceCommandService deviceCommandService;

    @PostMapping
    public void createDeviceCommand(@RequestBody @Valid DeviceCommandRequest deviceCommandRequest){
        deviceCommandService.createDeviceCommand(deviceCommandRequest);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DeviceCommandResponse>>> findAllWithSearchOptions(Pageable pageable, SearchOptions searchOptions) {
        return ResponseEntity.ok(ApiResponse.success(deviceCommandService.findAllWithSearchOptions(pageable, searchOptions)));
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportDeviceCommandLog(Pageable pageable) {
        try (ByteArrayInputStream excelStream = deviceCommandService.downloadFile(pageable)) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=device_commands.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(excelStream));
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
