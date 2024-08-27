package ru.learning.task_mgt_system.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a user.
 * Stores information about the user, including email, full name,
 * and tasks associated with the user as author and assignee.
 */
@Entity(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * Unique identifier of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Email of the user.
     * Must be unique and cannot be empty.
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Full name of the user.
     * Cannot be empty.
     */
    @Column(name = "full_name", nullable = false)
    private String fullName;

    /**
     * List of tasks authored by the user.
     * One-to-many relationship with the {@link Task} entity.
     */
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Task> authoredTasks = new ArrayList<>();

    /**
     * List of tasks assigned to the user.
     * One-to-many relationship with the {@link Task} entity.
     */
    @OneToMany(mappedBy = "assignee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Task> assignedTasks = new ArrayList<>();
}