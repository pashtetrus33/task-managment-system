package ru.learning.task_mgt_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Entity representing a comment.
 * Comments are associated with tasks and users.
 */
@Entity(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    /**
     * Unique identifier for the comment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The text of the comment.
     */
    @Column(nullable = false)
    private String text;

    /**
     * The creation date and time of the comment.
     * Automatically set when the entity is created.
     */
    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    /**
     * The author of the comment.
     * Many-to-one relationship with the {@link User} entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /**
     * The task to which the comment is related.
     * Many-to-one relationship with the {@link Task} entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
}