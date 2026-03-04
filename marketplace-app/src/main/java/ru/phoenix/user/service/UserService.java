package ru.phoenix.user.service;

import ru.phoenix.user.dto.CreateUserRequest;
import ru.phoenix.user.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    Page<UserResponse> getAllUsers(Pageable pageable);

    void promoteToAdmin(Long userId);

    void demoteAdmin(Long userId);
}