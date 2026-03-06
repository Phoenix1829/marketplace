package ru.phoenix.auth.service;

import ru.phoenix.auth.dto.LoginRequest;
import ru.phoenix.auth.dto.RegisterRequest;
import ru.phoenix.common.metrics.MetricsService;
import ru.phoenix.exception.UserAlreadyExistsException;
import ru.phoenix.user.entity.Role;
import ru.phoenix.user.entity.User;
import ru.phoenix.user.repository.UserRepository;
import ru.phoenix.security.jwt.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MetricsService metricsService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService, MetricsService metricsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.metricsService = metricsService;
    }

    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        metricsService.loginSuccess();

        return jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()

        );
    }

    public void register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                Role.USER
        );

        userRepository.save(user);

        metricsService.userCreated();
    }
}