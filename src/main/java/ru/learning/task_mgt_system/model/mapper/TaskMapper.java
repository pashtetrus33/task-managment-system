package ru.learning.task_mgt_system.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import ru.learning.task_mgt_system.model.Comment;
import ru.learning.task_mgt_system.model.Task;
import ru.learning.task_mgt_system.web.dto.TaskListResponse;
import ru.learning.task_mgt_system.web.dto.TaskRequest;
import ru.learning.task_mgt_system.web.dto.TaskResponse;
import ru.learning.task_mgt_system.web.dto.TaskResponseWithComments;

import java.util.List;

/**
 * Mapper for converting between {@link Task} entities and their corresponding Data Transfer Objects (DTOs).
 * <p>
 * This interface uses MapStruct to generate implementations for mapping between the {@link Task} entity and
 * various DTOs: {@link TaskRequest}, {@link TaskResponse}, {@link TaskListResponse}, and {@link TaskResponseWithComments}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class, CommentMapper.class})
public interface TaskMapper {

    /**
     * Maps a {@link Task} entity to a {@link TaskResponse} DTO.
     * <p>
     * This method converts a {@link Task} entity to a {@link TaskResponse} DTO, mapping fields from the entity
     * to the DTO, including author and assignee IDs.
     *
     * @param task the {@link Task} entity to be mapped.
     * @return the corresponding {@link TaskResponse} DTO.
     */
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "assignee.id", target = "assigneeId")
    TaskResponse taskToResponse(Task task);

    /**
     * Maps a {@link TaskRequest} DTO to a {@link Task} entity.
     * <p>
     * This method converts a {@link TaskRequest} DTO to a {@link Task} entity, mapping fields from the DTO
     * to the entity, including the assignee ID.
     *
     * @param taskRequest the {@link TaskRequest} DTO to be mapped.
     * @return the corresponding {@link Task} entity.
     */
    @Mapping(source = "assigneeId", target = "assignee.id")
    Task taskRequestToTask(TaskRequest taskRequest);

    /**
     * Converts a {@link Page} of {@link Task} entities to a {@link TaskListResponse} DTO.
     * <p>
     * This method converts a page of {@link Task} entities to a {@link TaskListResponse} DTO, including a list
     * of tasks and pagination information.
     *
     * @param taskPage the {@link Page} of {@link Task} entities to be converted.
     * @return the corresponding {@link TaskListResponse} DTO with task data and pagination details.
     */
    default TaskListResponse taskListToTaskListResponse(Page<Task> taskPage) {
        TaskListResponse response = new TaskListResponse();
        response.setTasks(taskPage.getContent().stream()
                .map(this::taskToResponse)
                .toList());

        response.setTotalElements(taskPage.getTotalElements());
        response.setTotalPages(taskPage.getTotalPages());
        response.setCurrentPage(taskPage.getNumber());
        response.setPageSize(taskPage.getSize());
        return response;
    }

    /**
     * Maps a {@link Task} entity to a {@link TaskResponseWithComments} DTO.
     * <p>
     * This method converts a {@link Task} entity to a {@link TaskResponseWithComments} DTO, including
     * the author ID, assignee ID, and associated comments.
     *
     * @param task the {@link Task} entity to be mapped.
     * @return the corresponding {@link TaskResponseWithComments} DTO.
     */
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "comments", target = "comments")
    TaskResponseWithComments taskToResponseWithComments(Task task);

    /**
     * Counts the number of comments in a list.
     * <p>
     * This method counts the number of {@link Comment} objects in a list and returns the count as a {@link Long}.
     *
     * @param comments the list of {@link Comment} objects.
     * @return the count of comments in the list.
     */
    default Long countComments(List<Comment> comments) {
        return (long) comments.size();
    }

    /**
     * Maps a {@link TaskResponseWithComments} DTO to a {@link Task} entity.
     * <p>
     * This method converts a {@link TaskResponseWithComments} DTO to a {@link Task} entity.
     *
     * @param taskResponseWithComments the {@link TaskResponseWithComments} DTO to be mapped.
     * @return the corresponding {@link Task} entity.
     */
    Task taskRequestResponseWithCommentsToTask(TaskResponseWithComments taskResponseWithComments);
}