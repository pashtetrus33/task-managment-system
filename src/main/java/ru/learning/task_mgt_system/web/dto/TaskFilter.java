package ru.learning.task_mgt_system.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.learning.task_mgt_system.validation.TaskFilterValid;

/**
 * DTO for filtering tasks.
 * Used to specify criteria for retrieving tasks based on various filters.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TaskFilterValid
@Builder
public class TaskFilter {
    /**
     * The number of tasks to return per page.
     * Must be specified for pagination.
     */
    private Integer size;

    /**
     * The page number to retrieve.
     * Must be specified for pagination.
     */
    private Integer page;

    /**
     * The search query to filter tasks.
     * Can be used to search for tasks based on a text query.
     */
    private String searchQuery;

    /**
     * The ID of the author of the tasks to filter by.
     * If specified, only tasks authored by this user will be returned.
     */
    private Long authorId;

    /**
     * The ID of the assignee of the tasks to filter by.
     * If specified, only tasks assigned to this user will be returned.
     */
    private Long assigneeId;
}