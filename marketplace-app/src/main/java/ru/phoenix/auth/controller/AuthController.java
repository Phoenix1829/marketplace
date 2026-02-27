package ru.phoenix.auth.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.phoenix.auth.dto.AuthResponse;
import ru.phoenix.auth.dto.LoginRequest;
import ru.phoenix.auth.dto.RegisterRequest;
import ru.phoenix.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return new AuthResponse(token);
    }

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
    }
}