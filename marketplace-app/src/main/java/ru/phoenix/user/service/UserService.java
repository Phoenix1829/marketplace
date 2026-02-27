package ru.phoenix.user.service;

import ru.phoenix.user.dto.CreateUserRequest;
import ru.phoenix.user.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    List<UserResponse> getAllUsers();
}