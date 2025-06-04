package trade.view.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trade.view.entity.Asset;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findAllByUserId(Long userId);
    boolean existsByUserIdAndSymbol(Long userId, String symbol);
    List<Asset> findByUserId(Long userId);
    Optional<Asset> findByUserIdAndSymbol(Long userId, String symbol);
}