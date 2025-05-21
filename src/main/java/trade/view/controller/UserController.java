package trade.view.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import trade.view.dto.UserDto;
import trade.view.dto.UserRequest;
import trade.view.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Получить всех пользователей
    @GetMapping
    public List<UserDto> listAll() {
        log.info("GET /api/users -> list all");
        var dtos = userService.findAllDto();
        log.debug("Found {} users", dtos.size());
        return dtos;
    }

    // Получить пользователя по ID
    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        log.info("GET /api/users/{} -> get by id", id);
        return userService.findDtoById(id);
    }

    // Создать нового пользователя
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserRequest request) {
        log.info("POST /api/users -> create {}", request);
        var dto = userService.createFromDto(request);
        log.debug("Created user {} with id={}", dto.username(), dto.id());
        return dto;
    }

    // Обновить пользователя
    @PutMapping("/{id}")
    public UserDto update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        log.info("PUT /api/users/{} -> update {}", id, request);
        var dto = userService.updateFromDto(id, request);
        log.debug("Updated user {} -> username={}, role={}", id, dto.username(), dto.role());
        return dto;
    }

    // Удалить пользователя
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("DELETE /api/users/{} -> delete", id);
        userService.delete(id);
        log.debug("Deleted user {}", id);
    }
}