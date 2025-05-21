package trade.view.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "market_data")
public class MarketData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private String source;

    @Type(value = JsonBinaryType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> data;

    @Column
    private BigDecimal price;

    @Column
    private BigDecimal volume;

    @Column(name = "change_pct")
    private BigDecimal changePct;

    @Column(name = "fetched_at")
    private Instant fetchedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist() { createdAt = updatedAt = Instant.now(); }

    @PreUpdate
    void preUpdate() { updatedAt = Instant.now(); }
}