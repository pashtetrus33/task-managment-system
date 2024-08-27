package ru.learning.task_mgt_system.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a task.
 * Contains information about the task including title, description, status, priority, author, and assignee.
 */
@Entity(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    /**
     * Unique identifier of the task.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title of the task.
     * Cannot be empty.
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Description of the task.
     * Maximum length is 1000 characters.
     */
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * Status of the task.
     * Defined by the {@link Status} enumeration.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    /**
     * Priority of the task.
     * Defined by the {@link Priority} enumeration.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    /**
     * Date and time when the task was created.
     * Automatically set when the entity is created.
     */
    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    /**
     * Date and time when the task was last updated.
     * Automatically updated when the entity is changed.
     */
    @UpdateTimestamp
    private Instant updatedAt;

    /**
     * Author of the task.
     * Many-to-one relationship with the {@link User} entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /**
     * Assignee of the task.
     * Many-to-one relationship with the {@link User} entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    /**
     * List of comments associated with the task.
     * One-to-many relationship with the {@link Comment} entity.
     */
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();
}