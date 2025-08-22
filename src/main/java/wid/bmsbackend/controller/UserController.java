package wid.bmsbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import wid.bmsbackend.dto.UserRequest;
import wid.bmsbackend.dto.UserResponse;
import wid.bmsbackend.entity.User;
import wid.bmsbackend.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Page<UserResponse> findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping("/add")
    public void addUser(@RequestBody @Valid UserRequest userRequest) {
        userService.addUser(userRequest);
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody @Valid UserRequest userRequest) {
        userService.updateUser(id, userRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
