package ru.learning.task_mgt_system.service;

import ru.learning.task_mgt_system.web.dto.*;

/**
 * Service interface for managing tasks in the task management system.
 * Provides methods for creating, retrieving, updating, filtering, and deleting tasks.
 */
public interface TaskService {

    /**
     * Creates a new task based on the provided request details.
     *
     * @param taskRequest the details of the task to be created.
     * @return {@link TaskResponse} DTO containing the details of the created task.
     */
    TaskResponse create(TaskRequest taskRequest);

    /**
     * Retrieves a task by its ID, including its comments.
     *
     * @param id the ID of the task to retrieve.
     * @return {@link TaskResponseWithComments} DTO containing the details of the task with comments.
     */
    TaskResponseWithComments getById(Long id);

    /**
     * Retrieves a paginated list of all tasks.
     *
     * @param page the page number to retrieve (0-based index).
     * @param size the number of tasks per page.
     * @return {@link TaskListResponse} DTO containing a list of tasks.
     */
    TaskListResponse getAll(int page, int size);

    /**
     * Retrieves a paginated list of tasks filtered by their status.
     *
     * @param status the status of the tasks to retrieve.
     * @param page   the page number to retrieve (0-based index).
     * @param size   the number of tasks per page.
     * @return {@link TaskListResponse} DTO containing a list of tasks with the specified status.
     */
    TaskListResponse getByStatus(String status, int page, int size);

    /**
     * Retrieves a paginated list of tasks filtered by their priority.
     *
     * @param priority the priority of the tasks to retrieve.
     * @param page     the page number to retrieve (0-based index).
     * @param size     the number of tasks per page.
     * @return {@link TaskListResponse} DTO containing a list of tasks with the specified priority.
     */
    TaskListResponse getByPriority(String priority, int page, int size);

    /**
     * Retrieves a paginated list of tasks filtered by the author's ID.
     *
     * @param authorId the ID of the author whose tasks are to be retrieved.
     * @param page     the page number to retrieve (0-based index).
     * @param size     the number of tasks per page.
     * @return {@link TaskListResponse} DTO containing a list of tasks authored by the specified author.
     */
    TaskListResponse getByAuthorId(Long authorId, int page, int size);

    /**
     * Retrieves a paginated list of tasks filtered by the assignee's ID.
     *
     * @param assigneeId the ID of the assignee whose tasks are to be retrieved.
     * @param page       the page number to retrieve (0-based index).
     * @param size       the number of tasks per page.
     * @return {@link TaskListResponse} DTO containing a list of tasks assigned to the specified assignee.
     */
    TaskListResponse getByAssigneeId(Long assigneeId, int page, int size);

    /**
     * Retrieves a paginated list of tasks filtered by both status and priority.
     *
     * @param status   the status of the tasks to retrieve.
     * @param priority the priority of the tasks to retrieve.
     * @param page     the page number to retrieve (0-based index).
     * @param size     the number of tasks per page.
     * @return {@link TaskListResponse} DTO containing a list of tasks with the specified status and priority.
     */
    TaskListResponse getByStatusAndPriority(String status, String priority, int page, int size);

    /**
     * Retrieves a paginated list of tasks filtered by status, priority, and author ID.
     *
     * @param status    the status of the tasks to retrieve.
     * @param priority  the priority of the tasks to retrieve.
     * @param authorId  the ID of the author whose tasks are to be retrieved.
     * @param page      the page number to retrieve (0-based index).
     * @param size      the number of tasks per page.
     * @return {@link TaskListResponse} DTO containing a list of tasks with the specified status, priority, and author ID.
     */
    TaskListResponse getByStatusAndPriorityAndAuthorId(String status, String priority, Long authorId, int page, int size);

    /**
     * Retrieves a paginated list of tasks filtered by status, priority, and assignee ID.
     *
     * @param status    the status of the tasks to retrieve.
     * @param priority  the priority of the tasks to retrieve.
     * @param assigneeId the ID of the assignee whose tasks are to be retrieved.
     * @param page      the page number to retrieve (0-based index).
     * @param size      the number of tasks per page.
     * @return {@link TaskListResponse} DTO containing a list of tasks with the specified status, priority, and assignee ID.
     */
    TaskListResponse getByStatusAndPriorityAndAssigneeId(String status, String priority, Long assigneeId, int page, int size);

    /**
     * Retrieves a paginated list of tasks based on the specified filter criteria.
     *
     * @param filter the filter criteria to apply.
     * @return {@link TaskListResponse} DTO containing a list of tasks that match the filter criteria.
     */
    TaskListResponse filterBy(TaskFilter filter);

    /**
     * Updates an existing task based on the provided request details.
     *
     * @param id          the ID of the task to update.
     * @param taskRequest the details to update the task with.
     * @return {@link TaskResponse} DTO containing the details of the updated task.
     */
    TaskResponse update(Long id, TaskRequest taskRequest);

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete.
     */
    void delete(Long id);
}