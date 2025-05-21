package trade.view.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import trade.view.dto.AssetDto;
import trade.view.dto.AssetRequest;
import trade.view.service.AssetService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users/{userId}/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    // Список активов пользователя
    @GetMapping
    public List<AssetDto> listByUser(@PathVariable Long userId) {
        log.info("GET /api/users/{}/assets -> list assets", userId);
        var dtos = assetService.findAllByUserDto(userId);
        log.debug("Found {} assets for user {}", dtos.size(), userId);
        return dtos;
    }

    // Получить конкретный актив
    @GetMapping("/{assetId}")
    public AssetDto getOne(@PathVariable Long userId, @PathVariable Long assetId) {
        log.info("GET /api/users/{}/assets/{} -> get", userId, assetId);
        return assetService.findOneDto(userId, assetId);
    }

    // Добавить новый актив
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssetDto create(@PathVariable Long userId, @Valid @RequestBody AssetRequest request) {
        log.info("POST /api/users/{}/assets -> create {}", userId, request);
        var dto = assetService.createFromDto(userId, request);
        log.debug("Created asset {} for user {}", dto.id(), userId);
        return dto;
    }

    // Обновить актив
    @PutMapping("/{assetId}")
    public AssetDto update(@PathVariable Long userId,
                           @PathVariable Long assetId,
                           @Valid @RequestBody AssetRequest request) {
        log.info("PUT /api/users/{}/assets/{} -> update {}", userId, assetId, request);
        var dto = assetService.updateFromDto(userId, assetId, request);
        log.debug("Updated asset {}: symbol={} for user {}", assetId, dto.symbol(), userId);
        return dto;
    }

    // Удалить актив
    @DeleteMapping("/{assetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId, @PathVariable Long assetId) {
        log.info("DELETE /api/users/{}/assets/{} -> delete", userId, assetId);
        assetService.delete(assetId);
        log.debug("Deleted asset {} for user {}", assetId, userId);
    }
}