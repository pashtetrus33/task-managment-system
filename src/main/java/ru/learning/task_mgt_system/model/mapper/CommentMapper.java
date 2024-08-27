package ru.learning.task_mgt_system.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import ru.learning.task_mgt_system.model.Comment;
import ru.learning.task_mgt_system.web.dto.CommentListResponse;
import ru.learning.task_mgt_system.web.dto.CommentRequest;
import ru.learning.task_mgt_system.web.dto.CommentResponse;

/**
 * Mapper for converting between {@link Comment} entities and their corresponding Data Transfer Objects (DTOs).
 * This mapper uses MapStruct to automatically generate implementations for the mapping methods.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = UserMapper.class)
public interface CommentMapper {

    /**
     * Maps a {@link Comment} entity to a {@link CommentResponse} DTO.
     *
     * @param comment the {@link Comment} entity to be mapped.
     * @return the corresponding {@link CommentResponse} DTO.
     */
    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "author.id", target = "authorId")
    CommentResponse commentToCommentResponse(Comment comment);

    /**
     * Maps a {@link CommentRequest} DTO to a {@link Comment} entity.
     *
     * @param commentRequest the {@link CommentRequest} DTO to be mapped.
     * @return the corresponding {@link Comment} entity.
     */
    @Mapping(source = "taskId", target = "task.id")
    Comment commentRequestToComment(CommentRequest commentRequest);

    /**
     * Converts a {@link Page} of {@link Comment} entities to a {@link CommentListResponse} DTO.
     *
     * @param commentPage the {@link Page} of {@link Comment} entities to be converted.
     * @return the corresponding {@link CommentListResponse} DTO containing paginated comment data.
     */
    default CommentListResponse commentListToCommentListResponse(Page<Comment> commentPage) {
        CommentListResponse response = new CommentListResponse();
        response.setComments(commentPage.getContent().stream()
                .map(this::commentToCommentResponse)
                .toList());

        response.setTotalElements(commentPage.getTotalElements());
        response.setTotalPages(commentPage.getTotalPages());
        response.setCurrentPage(commentPage.getNumber());
        response.setPageSize(commentPage.getSize());
        return response;
    }
}