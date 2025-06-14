package trade.view.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trade.view.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByTelegramId(Long telegramId);
}