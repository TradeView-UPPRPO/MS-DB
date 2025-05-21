package trade.view.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import trade.view.dto.MarketDataDto;
import trade.view.dto.MarketDataRequest;
import trade.view.service.MarketDataService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/market-data")
@Validated
public class MarketDataController {
    private final MarketDataService svc;
    public MarketDataController(MarketDataService svc) { this.svc = svc; }

    @GetMapping
    public List<MarketDataDto> listAll() {
        log.info("GET /api/market-data");
        return svc.findAll();
    }

    @GetMapping("/symbol/{symbol}")
    public List<MarketDataDto> bySymbol(@PathVariable String symbol) {
        log.info("GET /api/market-data/symbol/{}", symbol);
        return svc.findBySymbol(symbol);
    }

    @GetMapping("/{id}")
    public MarketDataDto getOne(@PathVariable Long id) {
        log.info("GET /api/market-data/{}", id);
        return svc.findOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MarketDataDto create(@Valid @RequestBody MarketDataRequest req) {
        log.info("POST /api/market-data {}", req);
        return svc.create(req);
    }

    @PutMapping("/{id}")
    public MarketDataDto update(@PathVariable Long id,
                                @Valid @RequestBody MarketDataRequest req) {
        log.info("PUT /api/market-data/{} {}", id, req);
        return svc.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("DELETE /api/market-data/{}", id);
        svc.delete(id);
    }
}