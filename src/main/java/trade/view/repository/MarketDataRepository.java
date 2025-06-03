package trade.view.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trade.view.entity.MarketData;
import java.util.List;
import java.util.Optional;

public interface MarketDataRepository extends JpaRepository<MarketData, Long> {
    List<MarketData> findBySymbol(String symbol);
    Optional<MarketData> findFirstBySymbolOrderByFetchedAtDesc(String symbol);
}