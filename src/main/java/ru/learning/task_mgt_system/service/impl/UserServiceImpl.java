package ru.learning.task_mgt_system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import ru.learning.task_mgt_system.exception.EntityNotFoundException;
import ru.learning.task_mgt_system.model.User;
import ru.learning.task_mgt_system.model.mapper.UserMapper;
import ru.learning.task_mgt_system.repository.UserRepository;
import ru.learning.task_mgt_system.service.UserService;
import ru.learning.task_mgt_system.web.dto.UserInfo;
import ru.learning.task_mgt_system.web.dto.UserListResponse;
import ru.learning.task_mgt_system.web.dto.UserResponse;

import java.text.MessageFormat;
import java.util.Optional;

/**
 * Implementation of the {@link UserService} interface for managing users.
 * This service handles user creation, retrieval, and information management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Creates a new user or retrieves an existing user based on the current authentication context.
     * This method ensures that a user exists for the current session.
     *
     * @return {@link User} the created or retrieved user.
     * @throws EntityNotFoundException if user information is missing or cannot be retrieved.
     */
    @Override
    public User createOrRetrieveUser() {
        // Retrieve current user information from authentication context
        UserInfo currentUserInfo = getCurrentUserInfo()
                .orElseThrow(() -> {
                    log.error("User information is missing.");
                    return new EntityNotFoundException("User information is missing.");
                });

        // Find user by email, or create a new one if not found
        String email = currentUserInfo.email();
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    // If user is not found, create a new user
                    String fullName = currentUserInfo.givenName() + " " + currentUserInfo.familyName();
                    log.info("User {} not found. Creating new user.", email);

                    User newUser = User.builder()
                            .email(email)
                            .fullName(fullName)
                            .build();

                    userRepository.save(newUser);
                    log.info("Successfully created user with email: {} and full name: {}", email, fullName);

                    return newUser;
                });
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve.
     * @return {@link UserResponse} DTO containing the details of the user.
     * @throws EntityNotFoundException if no user with the specified ID is found.
     */
    @Override
    public UserResponse getById(Long id) {
        log.info("Fetching user with id: {}", id);
        return userMapper.userToUserResponse(userRepository.findById(id)
                .orElseThrow(() -> {
                    String message = MessageFormat.format("User not found with id: {0}.", id);
                    log.error(message);
                    return new EntityNotFoundException(message);
                }));
    }

    /**
     * Retrieves a paginated list of all users.
     *
     * @param page the page number to retrieve (0-based index).
     * @param size the number of users per page.
     * @return {@link UserListResponse} DTO containing a list of all users.
     */
    @Override
    public UserListResponse getAll(int page, int size) {
        log.info("Fetching all users with pagination: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return userMapper.userListToUserListResponse(userRepository.findAll(pageable));
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to retrieve.
     * @return {@link Optional<User>} an Optional containing the user if found, or an empty Optional if not.
     */
    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves information about the current authenticated user.
     *
     * @return {@link Optional<UserInfo>} an Optional containing the user info if the user is authenticated, or an empty Optional if not.
     */
    @Override
    public Optional<UserInfo> getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            String email = (String) jwt.getClaims().get("preferred_username");
            log.info("Current user info retrieved: email={}", email);
            String givenName = (String) jwt.getClaims().get("given_name");
            String familyName = (String) jwt.getClaims().get("family_name");

            return Optional.of(new UserInfo(email, givenName, familyName));
        }
        log.warn("No authentication found or user info is missing.");
        return Optional.empty();
    }
}