package ru.learning.task_mgt_system.service;

import ru.learning.task_mgt_system.web.dto.CommentListResponse;
import ru.learning.task_mgt_system.web.dto.CommentRequest;
import ru.learning.task_mgt_system.web.dto.CommentResponse;

/**
 * Service interface for managing comments in the task management system.
 * Provides methods for creating, retrieving, updating, and deleting comments.
 */
public interface CommentService {

    /**
     * Creates a new comment based on the provided request details.
     *
     * @param commentRequest the details of the comment to be created.
     * @return {@link CommentResponse} DTO containing the details of the created comment.
     */
    CommentResponse create(CommentRequest commentRequest);

    /**
     * Retrieves a comment by its ID.
     *
     * @param id the ID of the comment to retrieve.
     * @return {@link CommentResponse} DTO containing the details of the retrieved comment.
     */
    CommentResponse getById(Long id);

    /**
     * Retrieves a paginated list of comments associated with a specific task.
     *
     * @param taskId the ID of the task for which comments are to be retrieved.
     * @param page   the page number to retrieve (0-based index).
     * @param size   the number of comments per page.
     * @return {@link CommentListResponse} DTO containing a list of comments associated with the specified task.
     */
    CommentListResponse getByTaskId(Long taskId, int page, int size);

    /**
     * Retrieves a paginated list of comments authored by a specific user.
     *
     * @param authorId the ID of the author whose comments are to be retrieved.
     * @param page     the page number to retrieve (0-based index).
     * @param size     the number of comments per page.
     * @return {@link CommentListResponse} DTO containing a list of comments authored by the specified user.
     */
    CommentListResponse getByAuthorId(Long authorId, int page, int size);

    /**
     * Updates an existing comment based on the provided request details.
     *
     * @param id            the ID of the comment to update.
     * @param commentRequest the details to update the comment with.
     * @return {@link CommentResponse} DTO containing the details of the updated comment.
     */
    CommentResponse update(Long id, CommentRequest commentRequest);

    /**
     * Deletes a comment by its ID.
     *
     * @param id the ID of the comment to delete.
     */
    void delete(Long id);
}