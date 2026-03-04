package ru.phoenix.user.controller;

import ru.phoenix.user.dto.UserResponse;
import ru.phoenix.user.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @PatchMapping("/{id}/promote")
    public void promoteToAdmin(@PathVariable Long id) {
        userService.promoteToAdmin(id);
    }

    @PatchMapping("/{id}/demote")
    public void demoteAdmin(@PathVariable Long id) {
        userService.demoteAdmin(id);
    }
}