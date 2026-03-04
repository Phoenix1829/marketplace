package ru.phoenix.security.permission;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.phoenix.exception.AccessDeniedException;
import ru.phoenix.user.entity.Role;
import ru.phoenix.user.entity.User;
import ru.phoenix.user.repository.UserRepository;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final UserRepository userRepository;

    public PermissionServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean canDeleteProduct(String currentEmail, String ownerEmail) {

        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow();

        return switch (currentUser.getRole()) {
            case SENIOR_ADMIN -> true;
            case ADMIN -> true;
            case USER -> currentEmail.equals(ownerEmail);
        };
    }

    @Override
    public boolean isSeniorAdmin(String email) {
        return userRepository.findByEmail(email)
                .map(user -> user.getRole() == Role.SENIOR_ADMIN)
                .orElse(false);
    }

    @Override
    public void checkCanPromote() {

        String email = getCurrentEmail();
        User currentUser = getUserByEmail(email);

        if (currentUser.getRole() != Role.SENIOR_ADMIN) {
            throw new AccessDeniedException(
                    "Only SENIOR_ADMIN can promote users"
            );
        }
    }

    @Override
    public void checkCanDemote(User currentUser, User targetUser) {

        if (currentUser.getRole() != Role.SENIOR_ADMIN) {
            throw new AccessDeniedException(
                    "Only SENIOR_ADMIN can demote users"
            );
        }

        if (targetUser.getRole() == Role.SENIOR_ADMIN) {
            throw new AccessDeniedException(
                    "Cannot demote SENIOR_ADMIN"
            );
        }

        if (currentUser.getId().equals(targetUser.getId())) {
            throw new AccessDeniedException(
                    "You cannot demote yourself"
            );
        }

        if (targetUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException(
                    "User is not an ADMIN"
            );
        }
    }

    private String getCurrentEmail() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow();
    }
}