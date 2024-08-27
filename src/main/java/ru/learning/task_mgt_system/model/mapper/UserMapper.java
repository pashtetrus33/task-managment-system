package ru.learning.task_mgt_system.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import ru.learning.task_mgt_system.model.Task;
import ru.learning.task_mgt_system.model.User;
import ru.learning.task_mgt_system.web.dto.UserListResponse;
import ru.learning.task_mgt_system.web.dto.UserResponse;

import java.util.List;

/**
 * Mapper for converting between {@link User} entities and their corresponding Data Transfer Objects (DTOs).
 * <p>
 * This interface uses MapStruct to generate implementations for mapping between the {@link User} entity and
 * various DTOs: {@link UserResponse} and {@link UserListResponse}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    /**
     * Maps a {@link User} entity to a {@link UserResponse} DTO.
     * <p>
     * This method converts a {@link User} entity to a {@link UserResponse} DTO, mapping fields from the entity
     * to the DTO.
     *
     * @param user the {@link User} entity to be mapped.
     * @return the corresponding {@link UserResponse} DTO.
     */
    UserResponse userToUserResponse(User user);

    /**
     * Converts a {@link Page} of {@link User} entities to a {@link UserListResponse} DTO.
     * <p>
     * This method converts a page of {@link User} entities to a {@link UserListResponse} DTO, including a list
     * of users and pagination information.
     *
     * @param userPage the {@link Page} of {@link User} entities to be converted.
     * @return the corresponding {@link UserListResponse} DTO with user data and pagination details.
     */
    default UserListResponse userListToUserListResponse(Page<User> userPage) {
        UserListResponse response = new UserListResponse();
        response.setUsers(userPage.getContent().stream()
                .map(this::userToUserResponse)
                .toList());

        response.setTotalElements(userPage.getTotalElements());
        response.setTotalPages(userPage.getTotalPages());
        response.setCurrentPage(userPage.getNumber());
        response.setPageSize(userPage.getSize());
        return response;
    }

    /**
     * Counts the number of tasks in a list.
     * <p>
     * This method counts the number of {@link Task} objects in a list and returns the count as a {@link Long}.
     *
     * @param tasks the list of {@link Task} objects.
     * @return the count of tasks in the list.
     */
    default Long countTasks(List<Task> tasks) {
        return (long) tasks.size();
    }
}