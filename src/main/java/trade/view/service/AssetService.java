package trade.view.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import trade.view.dto.AssetDto;
import trade.view.dto.AssetRequest;
import trade.view.entity.Asset;
import trade.view.entity.User;
import trade.view.repository.AssetRepository;
import trade.view.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AssetService {

    private final AssetRepository assetRepo;
    private final UserRepository userRepo;

    public AssetService(AssetRepository assetRepo, UserRepository userRepo) {
        this.assetRepo  = assetRepo;
        this.userRepo = userRepo;
    }

    // Список активов конкретного пользователя
    public List<Asset> findAllByUser(Long userId) {
        return assetRepo.findAllByUserId(userId);
    }

    // Получить один актив
    public Asset findOne(Long id) {
        return assetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found: " + id));
    }

    // Создать новый актив у пользователя
    @Transactional
    public Asset create(Long userId, Asset asset) {
        // Проверяем, что пользователь есть
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        // Ставим связь и сохраняем
        asset.setUser(user);
        return assetRepo.save(asset);
    }

    private AssetDto toDto(Asset a) {
        return new AssetDto(
                a.getId(),
                a.getUser().getId(),
                a.getSymbol(),
                a.getType(),
                a.getParameters(),
                a.getCreatedAt(),
                a.getUpdatedAt()
        );
    }

    // Список всех активов пользователя как DTO
    public List<AssetDto> findAllByUserDto(Long userId) {
        // Проверить, что пользователь существует
        if (!userRepo.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return assetRepo.findByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Получить один актив как DTO, проверив принадлежность
    public AssetDto findOneDto(Long userId, Long assetId) {
        Asset a = assetRepo.findById(assetId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Asset not found"));
        if (!a.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Asset not found for this user");
        }
        return toDto(a);
    }

    // Создать актив из DTO
    @Transactional
    public AssetDto createFromDto(Long userId, AssetRequest req) {
        User u = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
        Asset a = new Asset();
        a.setUser(u);
        a.setSymbol(req.symbol());
        a.setType(req.type());
        a.setParameters(req.parameters());
        Asset saved = assetRepo.save(a);
        return toDto(saved);
    }

    // Обновить актив из DTO
    @Transactional
    public AssetDto updateFromDto(Long userId, Long assetId, AssetRequest req) {
        Asset existing = assetRepo.findById(assetId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Asset not found"));
        if (!existing.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Asset not found for this user");
        }
        existing.setSymbol(req.symbol());
        existing.setType(req.type());
        existing.setParameters(req.parameters());
        Asset updated = assetRepo.save(existing);
        return toDto(updated);
    }

    // Удалить актив
    @Transactional
    public void delete(Long assetId) {
        if (!assetRepo.existsById(assetId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Asset not found");
        }
        assetRepo.deleteById(assetId);
    }
}