package trade.view.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import trade.view.dto.UserDto;
import trade.view.dto.UserRequest;
import trade.view.entity.User;
import trade.view.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    public Optional<User> findByTelegramId(Long tid) {
        return userRepo.findByTelegramId(tid);
    }

    @Transactional
    public User create(User user) {
        if (user.getTelegramId() == null)
            throw new IllegalArgumentException("telegramId is required");
        return userRepo.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    private UserDto toDto(User u) {
        return new UserDto(
                u.getId(),
                u.getUsername(),
                u.getTelegramId(),
                u.getRole(),
                u.getCreatedAt(),
                u.getUpdatedAt()
        );
    }

    // Получить всех пользователей как DTO
    public List<UserDto> findAllDto() {
        return userRepo.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Получить одного пользователя по ID как DTO
    public UserDto findDtoById(Long id) {
        return userRepo.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
    }

    // Создать пользователя из DTO
    @Transactional
    public UserDto createFromDto(UserRequest req) {
        User u = new User();
        u.setUsername(req.username());
        u.setTelegramId(req.telegramId());
        u.setRole(req.role());
        User saved = userRepo.save(u);
        return toDto(saved);
    }

    // Обновить пользователя из DTO
    @Transactional
    public UserDto updateFromDto(Long id, UserRequest req) {
        User existing = userRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
        existing.setUsername(req.username());
        existing.setTelegramId(req.telegramId());
        existing.setRole(req.role());
        User updated = userRepo.save(existing);
        return toDto(updated);
    }

    // Удалить пользователя
    @Transactional
    public void delete(Long id) {
        if (!userRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepo.deleteById(id);
    }
}