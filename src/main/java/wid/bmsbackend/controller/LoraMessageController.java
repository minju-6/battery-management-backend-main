package wid.bmsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wid.bmsbackend.dto.ApiResponse;
import wid.bmsbackend.dto.LoraMessageResponse;
import wid.bmsbackend.dto.SearchOptions;
import wid.bmsbackend.service.LoraMessageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lora-messages")
public class LoraMessageController {
    private final LoraMessageService loraMessageService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LoraMessageResponse>>> findAll(@PageableDefault() Pageable pageable, SearchOptions options) {
        return ResponseEntity.ok(ApiResponse.success(loraMessageService.findAll(pageable, options)));
    }
}
