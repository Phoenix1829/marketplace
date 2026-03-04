package ru.phoenix.security.permission;

import ru.phoenix.user.entity.User;

public interface PermissionService {

    boolean canDeleteProduct(String currentEmail, String ownerEmail);

    boolean isSeniorAdmin(String email);

    void checkCanPromote();

    void checkCanDemote(User currentUser, User targetUser);
}