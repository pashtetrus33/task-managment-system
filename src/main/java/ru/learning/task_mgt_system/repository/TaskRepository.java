package ru.learning.task_mgt_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.learning.task_mgt_system.model.Priority;
import ru.learning.task_mgt_system.model.Status;
import ru.learning.task_mgt_system.model.Task;

/**
 * Repository interface for performing CRUD operations and custom queries
 * on {@link Task} entities. Provides support for pagination and specification-based querying.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    /**
     * Finds tasks by their status.
     *
     * @param status   the status of the tasks.
     * @param pageable the pagination information.
     * @return a page of tasks with the specified status.
     */
    Page<Task> findByStatus(Status status, Pageable pageable);

    /**
     * Finds tasks by their priority.
     *
     * @param priority the priority of the tasks.
     * @param pageable the pagination information.
     * @return a page of tasks with the specified priority.
     */
    Page<Task> findByPriority(Priority priority, Pageable pageable);

    /**
     * Finds tasks by the author's ID.
     *
     * @param authorId the ID of the author.
     * @param pageable the pagination information.
     * @return a page of tasks authored by the specified author.
     */
    Page<Task> findByAuthorId(Long authorId, Pageable pageable);

    /**
     * Finds tasks by the assignee's ID.
     *
     * @param assigneeId the ID of the assignee.
     * @param pageable   the pagination information.
     * @return a page of tasks assigned to the specified assignee.
     */
    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);

    /**
     * Finds tasks by both status and priority.
     *
     * @param status   the status of the tasks.
     * @param priority the priority of the tasks.
     * @param pageable the pagination information.
     * @return a page of tasks with the specified status and priority.
     */
    Page<Task> findByStatusAndPriority(Status status, Priority priority, Pageable pageable);

    /**
     * Finds tasks by status, priority, and author's ID.
     *
     * @param status   the status of the tasks.
     * @param priority the priority of the tasks.
     * @param authorId the ID of the author.
     * @param pageable the pagination information.
     * @return a page of tasks with the specified status, priority, and authored by the specified author.
     */
    Page<Task> findByStatusAndPriorityAndAuthorId(Status status, Priority priority, Long authorId, Pageable pageable);

    /**
     * Finds tasks by status, priority, and assignee's ID.
     *
     * @param status     the status of the tasks.
     * @param priority   the priority of the tasks.
     * @param assigneeId the ID of the assignee.
     * @param pageable   the pagination information.
     * @return a page of tasks with the specified status, priority, and assigned to the specified assignee.
     */
    Page<Task> findByStatusAndPriorityAndAssigneeId(Status status, Priority priority, Long assigneeId, Pageable pageable);
}