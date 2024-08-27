package ru.learning.task_mgt_system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.learning.task_mgt_system.exception.EntityNotFoundException;
import ru.learning.task_mgt_system.model.Comment;
import ru.learning.task_mgt_system.model.Task;
import ru.learning.task_mgt_system.model.User;
import ru.learning.task_mgt_system.model.mapper.CommentMapper;
import ru.learning.task_mgt_system.model.mapper.TaskMapper;
import ru.learning.task_mgt_system.repository.CommentRepository;
import ru.learning.task_mgt_system.repository.TaskRepository;
import ru.learning.task_mgt_system.service.CommentService;
import ru.learning.task_mgt_system.service.TaskService;
import ru.learning.task_mgt_system.service.UserService;
import ru.learning.task_mgt_system.web.dto.CommentListResponse;
import ru.learning.task_mgt_system.web.dto.CommentRequest;
import ru.learning.task_mgt_system.web.dto.CommentResponse;

import java.text.MessageFormat;

/**
 * Implementation of the {@link CommentService} interface. Provides methods to manage comments including
 * creation, retrieval, updating, and deletion.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;
    private final CommentMapper commentMapper;
    private final TaskMapper taskMapper;

    /**
     * Creates a new comment based on the provided request details.
     *
     * @param commentRequest the details of the comment to be created.
     * @return {@link CommentResponse} DTO containing the details of the created comment.
     */
    @Override
    public CommentResponse create(CommentRequest commentRequest) {
        User author = userService.createOrRetrieveUser();

        // Verify that the associated task exists
        taskService.getById(commentRequest.getTaskId());

        Comment comment = commentMapper.commentRequestToComment(commentRequest);
        comment.setAuthor(author);
        Comment savedComment = commentRepository.save(comment);

        log.info("Successfully created comment with ID {} for task ID {}.", savedComment.getId(), commentRequest.getTaskId());
        return commentMapper.commentToCommentResponse(savedComment);
    }

    /**
     * Retrieves a comment by its ID.
     *
     * @param id the ID of the comment to retrieve.
     * @return {@link CommentResponse} DTO containing the details of the retrieved comment.
     * @throws EntityNotFoundException if the comment with the specified ID is not found.
     */
    @Override
    public CommentResponse getById(Long id) {
        return commentMapper.commentToCommentResponse(commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Comment not found with id: {}", id);
                    return new EntityNotFoundException(MessageFormat.format("Comment not found with id: {0}.", id));
                }));
    }

    /**
     * Retrieves a paginated list of comments associated with a specific task.
     *
     * @param taskId the ID of the task for which comments are to be retrieved.
     * @param page   the page number to retrieve (0-based index).
     * @param size   the number of comments per page.
     * @return {@link CommentListResponse} DTO containing a list of comments associated with the specified task.
     */
    @Override
    public CommentListResponse getByTaskId(Long taskId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching comments for task ID {}. Page: {}, Size: {}.", taskId, page, size);
        return commentMapper.commentListToCommentListResponse(commentRepository.findByTaskId(taskId, pageable));
    }

    /**
     * Retrieves a paginated list of comments authored by a specific user.
     *
     * @param authorId the ID of the author whose comments are to be retrieved.
     * @param page     the page number to retrieve (0-based index).
     * @param size     the number of comments per page.
     * @return {@link CommentListResponse} DTO containing a list of comments authored by the specified user.
     */
    @Override
    public CommentListResponse getByAuthorId(Long authorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching comments for author ID {}. Page: {}, Size: {}.", authorId, page, size);
        return commentMapper.commentListToCommentListResponse(commentRepository.findByAuthorId(authorId, pageable));
    }

    /**
     * Updates an existing comment based on the provided request details.
     *
     * @param id             the ID of the comment to update.
     * @param commentRequest the details to update the comment with.
     * @return {@link CommentResponse} DTO containing the details of the updated comment.
     * @throws EntityNotFoundException if the comment or the associated task is not found.
     */
    @Override
    public CommentResponse update(Long id, CommentRequest commentRequest) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Comment not found with id: {}", id);
                    return new EntityNotFoundException(MessageFormat.format("Comment not found with id: {0}.", id));
                });

        // Verify that the associated task exists
        taskService.getById(commentRequest.getTaskId());

        // Setting new values
        existingComment.setText(commentRequest.getText());
        existingComment.setTask(taskMapper.taskRequestResponseWithCommentsToTask(taskService.getById(commentRequest.getTaskId())));

        Comment updatedComment = commentRepository.save(existingComment);
        log.info("Successfully updated comment with ID {}.", id);
        return commentMapper.commentToCommentResponse(updatedComment);
    }

    /**
     * Deletes a comment by its ID.
     *
     * @param id the ID of the comment to delete.
     * @throws EntityNotFoundException if the comment with the specified ID is not found.
     */
    @Override
    public void delete(Long id) {
        if (!commentRepository.existsById(id)) {
            log.error("Comment not found with id: {}", id);
            throw new EntityNotFoundException(MessageFormat.format("Comment not found with id: {0}.", id));
        }
        commentRepository.deleteById(id);
        log.info("Successfully deleted comment with ID {}.", id);
    }
}