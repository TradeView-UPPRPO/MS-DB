package trade.view.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import trade.view.dto.AssetDto;
import trade.view.dto.AssetRequest;
import trade.view.entity.Asset;
import trade.view.entity.MarketData;
import trade.view.entity.User;
import trade.view.repository.AssetRepository;
import trade.view.repository.MarketDataRepository;
import trade.view.repository.UserRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AssetService {

    private final AssetRepository assetRepo;
    private final UserRepository userRepo;
    private final MarketDataRepository mdRepo;

    public AssetService(AssetRepository assetRepo, UserRepository userRepo, MarketDataRepository mdRepo) {
        this.assetRepo  = assetRepo;
        this.userRepo = userRepo;
        this.mdRepo = mdRepo;
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
                a.getAmount(),
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
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        Map<String,Object> params = new HashMap<>(req.parameters());

        // Если цена не передана – берём последнюю из MarketData
        if (!params.containsKey("buyPrice")) {
            BigDecimal price = mdRepo
                    .findFirstBySymbolOrderByFetchedAtDesc(req.symbol())
                    .map(MarketData::getPrice)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Нет актуальной цены для %s".formatted(req.symbol())));
            params.put("buyPrice", price);
        }

        Asset a = new Asset();
        a.setUser(user);
        a.setSymbol(req.symbol());
        a.setType(req.type());
        a.setAmount(req.amount());
        a.setParameters(params);

        return toDto(assetRepo.save(a));
    }

    // Обновить актив из DTO
    @Transactional
    public AssetDto updateFromDto(Long userId, Long assetId, AssetRequest req) {
        Asset ex = assetRepo.findById(assetId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Asset not found"));
        if (!ex.getUser().getId().equals(userId))
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Asset not found for this user");

        Map<String,Object> params = new HashMap<>(req.parameters());

        if (!params.containsKey("buyPrice")) {          // подставляем и при PUT
            BigDecimal price = mdRepo
                    .findFirstBySymbolOrderByFetchedAtDesc(req.symbol())
                    .map(MarketData::getPrice)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Нет актуальной цены для %s".formatted(req.symbol())));
            params.put("buyPrice", price);
        }

        ex.setSymbol(req.symbol());
        ex.setAmount(req.amount());
        ex.setType  (req.type());
        ex.setParameters(params);

        return toDto(assetRepo.save(ex));
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