package ru.learning.task_mgt_system.service;

import ru.learning.task_mgt_system.exception.EntityNotFoundException;
import ru.learning.task_mgt_system.model.User;
import ru.learning.task_mgt_system.web.dto.UserInfo;
import ru.learning.task_mgt_system.web.dto.UserListResponse;
import ru.learning.task_mgt_system.web.dto.UserResponse;

import java.util.Optional;

/**
 * Service interface for managing users in the task management system.
 * This interface provides methods for user creation, retrieval, and management.
 */
public interface UserService {

    /**
     * Creates a new user or retrieves an existing user based on the current authentication context.
     * This method is used to ensure that a user exists for the current session.
     *
     * @return {@link User} the created or retrieved user.
     */
    User createOrRetrieveUser();

    /**
     * Retrieves a paginated list of all users.
     *
     * @param page the page number to retrieve (0-based index).
     * @param size the number of users per page.
     * @return {@link UserListResponse} DTO containing a list of all users.
     */
    UserListResponse getAll(int page, int size);

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve.
     * @return {@link UserResponse} DTO containing the details of the user.
     * @throws EntityNotFoundException if no user with the specified ID is found.
     */
    UserResponse getById(Long id);

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to retrieve.
     * @return {@link Optional<User>} an Optional containing the user if found, or an empty Optional if not.
     */
    Optional<User> getByEmail(String email);

    /**
     * Retrieves information about the current authenticated user.
     *
     * @return {@link Optional<UserInfo>} an Optional containing the user info if the user is authenticated, or an empty Optional if not.
     */
    Optional<UserInfo> getCurrentUserInfo();
}