package ru.learning.task_mgt_system.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.learning.task_mgt_system.model.Task;
import ru.learning.task_mgt_system.web.dto.TaskFilter;

/**
 * Provides static methods for creating JPA {@link Specification} objects used to filter {@link Task} entities.
 * This interface offers filter specifications based on the author ID, assignee ID, and search query.
 */
public interface TaskSpecification {

    /**
     * Creates a {@link Specification} for filtering {@link Task} entities based on the provided {@link TaskFilter}.
     * The specification combines filtering criteria using logical AND.
     *
     * @param taskFilter The {@link TaskFilter} containing filter criteria.
     * @return A {@link Specification} for filtering {@link Task} entities.
     */
    static Specification<Task> withFilter(TaskFilter taskFilter) {
        return Specification.where(byAuthorId(taskFilter.getAuthorId()))
                .and(byAssigneeId(taskFilter.getAssigneeId()))
                .and(bySearchQuery(taskFilter.getSearchQuery()));
    }

    /**
     * Creates a {@link Specification} that filters {@link Task} entities based on the author ID.
     * Returns null if the author ID is not specified.
     *
     * @param authorId The ID of the author to filter by.
     * @return A {@link Specification} for filtering {@link Task} entities by author ID.
     */
    static Specification<Task> byAuthorId(Long authorId) {
        return (root, query, criteriaBuilder) -> {
            if (authorId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("author").get("id"), authorId);
        };
    }

    /**
     * Creates a {@link Specification} that filters {@link Task} entities based on the assignee ID.
     * Returns null if the assignee ID is not specified.
     *
     * @param assigneeId The ID of the assignee to filter by.
     * @return A {@link Specification} for filtering {@link Task} entities by assignee ID.
     */
    static Specification<Task> byAssigneeId(Long assigneeId) {
        return (root, query, criteriaBuilder) -> {
            if (assigneeId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("assignee").get("id"), assigneeId);
        };
    }

    /**
     * Creates a {@link Specification} that filters {@link Task} entities based on a search query.
     * The search is performed on the title and description fields using a case-insensitive match.
     * Returns null if the search query is not specified or is blank.
     *
     * @param searchQuery The search query used to filter tasks.
     * @return A {@link Specification} for filtering {@link Task} entities by search query.
     */
    static Specification<Task> bySearchQuery(String searchQuery) {
        return (root, query, criteriaBuilder) -> {
            if (searchQuery == null || searchQuery.isBlank()) {
                return null;
            }

            String searchPattern = "%" + searchQuery.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern)
            );
        };
    }
}