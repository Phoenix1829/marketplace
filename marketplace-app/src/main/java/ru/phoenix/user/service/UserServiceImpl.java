package ru.phoenix.user.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.phoenix.common.metrics.MetricsService;
import ru.phoenix.exception.AccessDeniedException;
import ru.phoenix.exception.ResourceNotFoundException;
import ru.phoenix.exception.UserAlreadyExistsException;
import ru.phoenix.security.permission.PermissionService;
import ru.phoenix.user.dto.CreateUserRequest;
import ru.phoenix.user.dto.UserResponse;
import ru.phoenix.user.entity.Role;
import ru.phoenix.user.entity.User;
import ru.phoenix.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionService permissionService;
    private final MetricsService metricsService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, PermissionService permissionService, MetricsService metricsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionService = permissionService;
        this.metricsService = metricsService;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getEmail(),
                encodedPassword,
                Role.USER
        );

        User saved = userRepository.save(user);

        metricsService.userCreated();

        return new UserResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getRole(),
                saved.getCreatedAt()
        );
    }

    @CacheEvict(value = {"users", "user"}, allEntries = true)
    @Override
    public void promoteToAdmin(Long userId) {

        permissionService.checkCanPromote();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setRole(Role.ADMIN);

        userRepository.save(user);

        metricsService.userPromoted();
    }

    @CacheEvict(value = {"users", "user"}, allEntries = true)
    @Override
    public void demoteAdmin(Long userId) {

        User targetUser = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        String currentEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow();

        permissionService.checkCanDemote(currentUser, targetUser);

        targetUser.setRole(Role.USER);

        userRepository.save(targetUser);

        metricsService.userDemoted();
    }

    @Cacheable(value = "users",
            key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {

        return userRepository.findAll(pageable)
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getRole(),
                        user.getCreatedAt()
                ));
    }
}