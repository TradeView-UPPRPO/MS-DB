package trade.view.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import trade.view.dto.AnalysisDataDto;
import trade.view.dto.AnalysisDataRequest;
import trade.view.entity.AnalysisData;
import trade.view.repository.AnalysisDataRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnalysisDataService {

    private final AnalysisDataRepository repo;

    private AnalysisDataDto toDto(AnalysisData a) {
        return new AnalysisDataDto(
                a.getId(), a.getSymbol(), a.getAnalysis(),
                a.getCreatedAt(), a.getUpdatedAt());
    }

    public List<AnalysisDataDto> findAll() {
        return repo.findAll()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<AnalysisDataDto> findBySymbol(String symbol) {
        return repo.findBySymbol(symbol)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public AnalysisDataDto findOne(Long id) {
        return repo.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Analysis not found"));
    }

    @Transactional
    public AnalysisDataDto create(AnalysisDataRequest req) {
        AnalysisData a = new AnalysisData();
        a.setSymbol(req.symbol());
        a.setAnalysis(req.analysis());
        return toDto(repo.save(a));
    }

    @Transactional
    public AnalysisDataDto update(Long id, AnalysisDataRequest req) {
        AnalysisData a = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Analysis not found"));
        a.setSymbol(req.symbol());
        a.setAnalysis(req.analysis());
        return toDto(repo.save(a));
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Analysis not found");
        repo.deleteById(id);
    }
}
