package ru.learning.task_mgt_system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.learning.task_mgt_system.exception.EntityNotFoundException;
import ru.learning.task_mgt_system.model.Priority;
import ru.learning.task_mgt_system.model.Status;
import ru.learning.task_mgt_system.model.Task;
import ru.learning.task_mgt_system.model.User;
import ru.learning.task_mgt_system.model.mapper.TaskMapper;
import ru.learning.task_mgt_system.repository.TaskRepository;
import ru.learning.task_mgt_system.repository.TaskSpecification;
import ru.learning.task_mgt_system.service.TaskService;
import ru.learning.task_mgt_system.service.UserService;
import ru.learning.task_mgt_system.web.dto.*;

import java.text.MessageFormat;
import java.util.Optional;

/**
 * Implementation of {@link TaskService} for managing tasks in the task management system.
 * This class provides methods for creating, retrieving, updating, deleting, and filtering tasks.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskMapper taskMapper;

    /**
     * Creates a new task with the details provided in the {@link TaskRequest} DTO.
     * Retrieves or creates the author and validates the assignee.
     *
     * @param taskRequest the DTO containing the details of the task to create.
     * @return {@link TaskResponse} DTO containing the details of the created task.
     */
    @CacheEvict(value = "databaseEntities", allEntries = true)
    @Override
    public TaskResponse create(TaskRequest taskRequest) {

        User author = userService.createOrRetrieveUser();

        userService.getById(taskRequest.getAssigneeId());

        String description = Optional.ofNullable(taskRequest.getDescription()).orElse("");

        Task task = taskMapper.taskRequestToTask(taskRequest);
        task.setAuthor(author);
        task.setDescription(description);

        Task savedTask = taskRepository.save(task);
        log.info("Successfully created task with ID {}.", savedTask.getId());
        return taskMapper.taskToResponse(savedTask);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task to retrieve.
     * @return {@link TaskResponseWithComments} DTO containing the details of the task with its comments.
     * @throws EntityNotFoundException if no task with the specified ID is found.
     */
    @Override
    @Cacheable("databaseEntityById")
    public TaskResponseWithComments getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task with ID {} not found.", id);
                    return new EntityNotFoundException(MessageFormat.format("Task with ID {0} not found.", id));
                });
        return taskMapper.taskToResponseWithComments(task);
    }

    /**
     * Retrieves all tasks with pagination.
     *
     * @param page the page number to retrieve (0-based index).
     * @param size the number of tasks per page.
     * @return {@link TaskListResponse} DTO containing a list of all tasks.
     */
    @Cacheable("databaseEntities")
    @Override
    public TaskListResponse getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching all tasks. Page: {}, Size: {}.", page, size);
        return taskMapper.taskListToTaskListResponse(taskRepository.findAll(pageable));
    }

    /**
     * Retrieves tasks filtered by status with pagination.
     *
     * @param status the status to filter tasks by.
     * @param page the page number to retrieve (0-based index).
     * @param size the number of tasks per page.
     * @return {@link TaskListResponse} DTO containing a list of tasks with the specified status.
     */
    @Override
    public TaskListResponse getByStatus(String status, int page, int size) {
        Status st = parseEnum(Status.class, status);
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching tasks by status {}. Page: {}, Size: {}.", status, page, size);
        return taskMapper.taskListToTaskListResponse(taskRepository.findByStatus(st, pageable));
    }

    /**
     * Retrieves tasks filtered by priority with pagination.
     *
     * @param priority the priority to filter tasks by.
     * @param page the page number to retrieve (0-based index).
     * @param size the number of tasks per page.
     * @return {@link TaskListResponse} DTO containing a list of tasks with the specified priority.
     */
    @Override
    public TaskListResponse getByPriority(String priority, int page, int size) {
        Priority pr = parseEnum(Priority.class, priority);
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching tasks by priority {}. Page: {}, Size: {}.", priority, page, size);
        return taskMapper.taskListToTaskListResponse(taskRepository.findByPriority(pr, pageable));
    }

    /**
     * Retrieves tasks filtered by author ID with pagination.
     *
     * @param authorId the ID of the author to filter tasks by.
     * @param page the page number to retrieve (0-based index).
     * @param size the number of tasks per page.
     * @return {@link TaskListResponse} DTO containing a list of tasks authored by the specified author.
     * @throws EntityNotFoundException if no user with the specified author ID is found.
     */
    @Override
    public TaskListResponse getByAuthorId(Long authorId, int page, int size) {
        userService.getById(authorId);
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching tasks by author ID {}. Page: {}, Size: {}.", authorId, page, size);
        return taskMapper.taskListToTaskListResponse(taskRepository.findByAuthorId(authorId, pageable));
    }

    /**
     * Retrieves tasks filtered by assignee ID with pagination.
     *
     * @param assigneeId the ID of the assignee to filter tasks by.
     * @param page the page number to retrieve (0-based index).
     * @param size the number of tasks per page.
     * @return {@link TaskListResponse} DTO containing a list of tasks assigned to the specified assignee.
     * @throws EntityNotFoundException if no user with the specified assignee ID is found.
     */
    @Override
    public TaskListResponse getByAssigneeId(Long assigneeId, int page, int size) {
        userService.getById(assigneeId);
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching tasks by assignee ID {}. Page: {}, Size: {}.", assigneeId, page, size);
        return taskMapper.taskListToTaskListResponse(taskRepository.findByAssigneeId(assigneeId, pageable));
    }

    /**
     * Retrieves tasks filtered by both status and priority with pagination.
     *
     * @param status the status to filter tasks by.
     * @param priority the priority to filter tasks by.
     * @param page the page number to retrieve (0-based index).
     * @param size the number of tasks per page.
     * @return {@link TaskListResponse} DTO containing a list of tasks with the specified status and priority.
     */
    @Override
    public TaskListResponse getByStatusAndPriority(String status, String priority, int page, int size) {
        Status st = parseEnum(Status.class, status);
        Priority pr = parseEnum(Priority.class, priority);
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching tasks by status {} and priority {}. Page: {}, Size: {}.", status, priority, page, size);
        return taskMapper.taskListToTaskListResponse(taskRepository.findByStatusAndPriority(st, pr, pageable));
    }

    /**
     * Retrieves tasks filtered by status, priority, and author ID with pagination.
     *
     * @param status the status to filter tasks by.
     * @param priority the priority to filter tasks by.
     * @param authorId ID of the author to filter tasks by.
     * @param page the page number to retrieve (0-based index).
     * @param size the number of tasks per page.
     * @return {@link TaskListResponse} DTO containing a list of tasks with the specified status, priority, and author ID.
     * @throws EntityNotFoundException if no user with the specified author ID is found.
     */
    @Override
    public TaskListResponse getByStatusAndPriorityAndAuthorId(String status, String priority, Long authorId, int page, int size) {
        userService.getById(authorId);
        Status st = parseEnum(Status.class, status);
        Priority pr = parseEnum(Priority.class, priority);
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching tasks by status {}, priority {}, and author ID {}. Page: {}, Size: {}.", status, priority, authorId, page, size);
        return taskMapper.taskListToTaskListResponse(taskRepository.findByStatusAndPriorityAndAuthorId(st, pr, authorId, pageable));
    }

    /**
     * Retrieves tasks filtered by status, priority, and assignee ID with pagination.
     *
     * @param status the status to filter tasks by.
     * @param priority the priority to filter tasks by.
     * @param assigneeId ID of the assignee to filter tasks by.
     * @param page the page number to retrieve (0-based index).
     * @param size the number of tasks per page.
     * @return {@link TaskListResponse} DTO containing a list of tasks with the specified status, priority, and assignee ID.
     * @throws EntityNotFoundException if no user with the specified assignee ID is found.
     */
    @Override
    public TaskListResponse getByStatusAndPriorityAndAssigneeId(String status, String priority, Long assigneeId, int page, int size) {
        userService.getById(assigneeId);
        Status st = parseEnum(Status.class, status);
        Priority pr = parseEnum(Priority.class, priority);
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching tasks by status {}, priority {}, and assignee ID {}. Page: {}, Size: {}.", status, priority, assigneeId, page, size);
        return taskMapper.taskListToTaskListResponse(taskRepository.findByStatusAndPriorityAndAssigneeId(st, pr, assigneeId, pageable));
    }

    /**
     * Filters tasks based on a {@link TaskFilter} with pagination.
     *
     * @param filter the filter criteria for querying tasks.
     * @return {@link TaskListResponse} DTO containing a list of tasks that match the filter criteria.
     */
    @Override
    public TaskListResponse filterBy(TaskFilter filter) {
        log.info("Filtering tasks with filter {}. Page: {}, Size: {}.", filter, filter.getPage(), filter.getSize());
        return taskMapper.taskListToTaskListResponse(taskRepository.findAll(TaskSpecification.withFilter(filter), PageRequest.of(
                filter.getPage(), filter.getSize())));
    }

    /**
     * Updates an existing task with the details provided in the {@link TaskRequest} DTO.
     *
     * @param id the ID of the task to update.
     * @param taskRequest the DTO containing the updated details of the task.
     * @return {@link TaskResponse} DTO containing the details of the updated task.
     * @throws EntityNotFoundException if no task with the specified ID is found.
     */
    @Caching(evict = {
            @CacheEvict(value = "databaseEntities", allEntries = true),
            @CacheEvict(value = "databaseEntityById", allEntries = true)
    })
    @Override
    public TaskResponse update(Long id, TaskRequest taskRequest) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task not found with id: {}", id);
                    return new EntityNotFoundException(MessageFormat.format("Task not found with id: {0}", id));
                });

        existingTask.setTitle(taskRequest.getTitle());
        existingTask.setDescription(Optional.ofNullable(taskRequest.getDescription()).orElse(""));
        existingTask.setStatus(parseEnum(Status.class, taskRequest.getStatus()));
        existingTask.setPriority(parseEnum(Priority.class, taskRequest.getPriority()));

        Task updatedTask = taskRepository.save(existingTask);
        log.info("Successfully updated task with ID {}.", id);
        return taskMapper.taskToResponse(updatedTask);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete.
     * @throws EntityNotFoundException if no task with the specified ID is found.
     */
    @Caching(evict = {
            @CacheEvict(value = "databaseEntities", allEntries = true),
            @CacheEvict(value = "databaseEntityById", allEntries = true)
    })
    @Override
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            log.error("Task not found with id: {}", id);
            throw new EntityNotFoundException(MessageFormat.format("Task not found with id: {0}.", id));
        }
        taskRepository.deleteById(id);
        log.info("Successfully deleted task with ID {}.", id);
    }

    /**
     * Parses a string value to an enum of the specified type.
     *
     * @param enumType the enum class to parse the value to.
     * @param value the string value to parse.
     * @param <T> the type of the enum.
     * @return the parsed enum value.
     * @throws EntityNotFoundException if the string value does not match any enum value.
     */
    private <T extends Enum<T>> T parseEnum(Class<T> enumType, String value) {
        try {
            return Enum.valueOf(enumType, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid value for {}: {}", enumType.getSimpleName(), value);
            throw new EntityNotFoundException(MessageFormat.format("Invalid value for {0}: {1}.", enumType.getSimpleName(), value));
        }
    }
}