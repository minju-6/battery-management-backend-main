package wid.bmsbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wid.bmsbackend.dto.ApiResponse;
import wid.bmsbackend.dto.DeviceMessageResponse;
import wid.bmsbackend.dto.SearchOptions;
import wid.bmsbackend.service.DeviceMessageService;
import wid.bmsbackend.service.DownloadFileService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/device-messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class DeviceMessageController {
    private final DeviceMessageService deviceMessageService;
    private final DownloadFileService downloadFileService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DeviceMessageResponse>>> findAll(Pageable pageable, SearchOptions searchOptions) {
        return ResponseEntity.ok(ApiResponse.success(deviceMessageService.findAll(pageable, searchOptions)));
    }

    @GetMapping("/{eui}")
    public ResponseEntity<ApiResponse<Page<DeviceMessageResponse>>> findByEui(@PathVariable String eui, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        var result = ResponseEntity.ok(ApiResponse.success(deviceMessageService.findByEui(eui, startDate, endDate, pageable)));
        return result;
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportDeviceMessageLog(@PageableDefault(sort="id", direction = Sort.Direction.DESC) Pageable pageable, SearchOptions searchOptions) {
        try (ByteArrayInputStream excelStream = downloadFileService.downloadFile(pageable, searchOptions)) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=device_messages.xlsx");

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