package ru.learning.task_mgt_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.learning.task_mgt_system.model.Comment;

/**
 * Repository interface for performing CRUD operations and custom queries
 * on {@link Comment} entities. Provides support for pagination.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Finds comments associated with a specific task.
     *
     * @param taskId the ID of the task whose comments are to be retrieved.
     * @param pageable the pagination information.
     * @return a {@link Page} of comments associated with the specified task.
     */
    Page<Comment> findByTaskId(Long taskId, Pageable pageable);

    /**
     * Finds comments authored by a specific user.
     *
     * @param authorId the ID of the author whose comments are to be retrieved.
     * @param pageable the pagination information.
     * @return a {@link Page} of comments authored by the specified user.
     */
    Page<Comment> findByAuthorId(Long authorId, Pageable pageable);

    /**
     * Finds comments associated with a specific task and authored by a specific user.
     *
     * @param taskId the ID of the task whose comments are to be retrieved.
     * @param authorId the ID of the author whose comments are to be retrieved.
     * @param pageable the pagination information.
     * @return a {@link Page} of comments associated with the specified task and authored by the specified user.
     */
    Page<Comment> findByTaskIdAndAuthorId(Long taskId, Long authorId, Pageable pageable);
}