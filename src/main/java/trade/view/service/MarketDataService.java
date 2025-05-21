package trade.view.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import trade.view.dto.MarketDataDto;
import trade.view.dto.MarketDataRequest;
import trade.view.entity.MarketData;
import trade.view.repository.MarketDataRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MarketDataService {
    private final MarketDataRepository repo;
    public MarketDataService(MarketDataRepository repo) { this.repo = repo; }

    private MarketDataDto toDto(MarketData m) {
        return new MarketDataDto(
                m.getId(), m.getSymbol(), m.getSource(), m.getData(),
                m.getPrice(), m.getVolume(), m.getChangePct(), m.getFetchedAt(),
                m.getCreatedAt(), m.getUpdatedAt()
        );
    }

    public List<MarketDataDto> findAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<MarketDataDto> findBySymbol(String symbol) {
        return repo.findBySymbol(symbol).stream().map(this::toDto).collect(Collectors.toList());
    }

    public MarketDataDto findOne(Long id) {
        return repo.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MarketData not found"));
    }

    @Transactional
    public MarketDataDto create(MarketDataRequest req) {
        MarketData m = new MarketData();
        m.setSymbol(req.symbol()); m.setSource(req.source()); m.setData(req.data());
        m.setPrice(req.price()); m.setVolume(req.volume()); m.setChangePct(req.changePct());
        m.setFetchedAt(req.fetchedAt());
        MarketData saved = repo.save(m);
        return toDto(saved);
    }

    @Transactional
    public MarketDataDto update(Long id, MarketDataRequest req) {
        MarketData m = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MarketData not found"));
        m.setSymbol(req.symbol()); m.setSource(req.source()); m.setData(req.data());
        m.setPrice(req.price()); m.setVolume(req.volume()); m.setChangePct(req.changePct());
        m.setFetchedAt(req.fetchedAt());
        MarketData updated = repo.save(m);
        return toDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "MarketData not found");
        repo.deleteById(id);
    }
}