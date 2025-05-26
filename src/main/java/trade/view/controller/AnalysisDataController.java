package trade.view.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import trade.view.dto.AnalysisDataDto;
import trade.view.dto.AnalysisDataRequest;
import trade.view.service.AnalysisDataService;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/analysis-data")
@RequiredArgsConstructor
public class AnalysisDataController {

    private final AnalysisDataService svc;

    @GetMapping
    public List<AnalysisDataDto> list(@RequestParam(required = false) String symbol) {
        if (symbol == null) {
            log.info("GET /api/analysis-data");
            return svc.findAll();
        }
        log.info("GET /api/analysis-data?symbol={}", symbol);
        return svc.findBySymbol(symbol);
    }

    @GetMapping("/{id}")
    public AnalysisDataDto get(@PathVariable Long id) {
        log.info("GET /api/analysis-data/{}", id);
        return svc.findOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnalysisDataDto create(@Valid @RequestBody AnalysisDataRequest req) {
        log.info("POST /api/analysis-data {}", req.symbol());
        return svc.create(req);
    }

    @PutMapping("/{id}")
    public AnalysisDataDto update(@PathVariable Long id,
                                  @Valid @RequestBody AnalysisDataRequest req) {
        log.info("PUT /api/analysis-data/{} {}", id, req.symbol());
        return svc.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("DELETE /api/analysis-data/{}", id);
        svc.delete(id);
    }
}
