package trade.view.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "assets",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "symbol"}))
public class Asset {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String symbol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetType type;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String,Object> parameters;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist()  { createdAt = updatedAt = Instant.now(); }

    @PreUpdate
    void preUpdate()   { updatedAt = Instant.now(); }

    // getters/setters…
}
