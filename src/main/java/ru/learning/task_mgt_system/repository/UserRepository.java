package ru.learning.task_mgt_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.learning.task_mgt_system.model.User;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations and custom queries
 * on {@link User} entities. Provides a method to find users by email.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user.
     * @return an {@link Optional} containing the user with the specified email, or an empty {@link Optional} if no user is found.
     */
    Optional<User> findByEmail(String email);
}