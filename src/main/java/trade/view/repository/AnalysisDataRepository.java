package trade.view.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trade.view.entity.AnalysisData;

import java.util.List;

public interface AnalysisDataRepository extends JpaRepository<AnalysisData, Long> {
    List<AnalysisData> findBySymbol(String symbol);
}
