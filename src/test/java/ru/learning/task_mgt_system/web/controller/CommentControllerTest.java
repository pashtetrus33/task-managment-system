package ru.learning.task_mgt_system.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import ru.learning.task_mgt_system.service.CommentService;
import ru.learning.task_mgt_system.web.dto.CommentListResponse;
import ru.learning.task_mgt_system.web.dto.CommentRequest;
import ru.learning.task_mgt_system.web.dto.CommentResponse;

/**
 * Unit tests for the CommentController class.
 *
 * This test class verifies the behavior of the CommentController by mocking
 * the CommentService and checking the responses from the controller methods.
 * It uses Mockito for mocking dependencies and Spring's @WithMockUser
 * annotation to simulate authenticated user roles for each test.
 */
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initializes the mocks before each test
    }

    /**
     * Tests the getById method of CommentController.
     *
     * This test verifies that the getById method returns a CommentResponse
     * with status code 200 OK when a valid comment ID is provided.
     * It uses @WithMockUser to simulate a user with the role "USER".
     */
    @Test
    @WithMockUser(roles = "USER")
    void getById_ShouldReturnCommentResponse() {
        CommentResponse commentResponse = new CommentResponse();
        when(commentService.getById(1L)).thenReturn(commentResponse);  // Mocking the service response

        ResponseEntity<CommentResponse> response = commentController.getById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());  // Asserting the status code
        assertEquals(commentResponse, response.getBody());  // Asserting the response body
    }

    /**
     * Tests the create method of CommentController.
     *
     * This test verifies that the create method returns a CommentResponse
     * with status code 201 Created when a valid CommentRequest is provided.
     * It uses @WithMockUser to simulate a user with the role "USER".
     */
    @Test
    @WithMockUser(roles = "USER")
    void create_ShouldReturnCreatedCommentResponse() {
        CommentRequest request = new CommentRequest();
        CommentResponse response = new CommentResponse();
        when(commentService.create(request)).thenReturn(response);  // Mocking the service response

        ResponseEntity<CommentResponse> result = commentController.create(request);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());  // Asserting the status code
        assertEquals(response, result.getBody());  // Asserting the response body
    }

    /**
     * Tests the update method of CommentController.
     *
     * This test verifies that the update method returns a CommentResponse
     * with status code 200 OK when a valid comment ID and CommentRequest
     * are provided. It uses @WithMockUser to simulate a user with the role "USER".
     */
    @Test
    @WithMockUser(roles = "USER")
    void update_ShouldReturnUpdatedCommentResponse() {
        CommentRequest request = new CommentRequest();
        CommentResponse response = new CommentResponse();
        when(commentService.update(1L, request)).thenReturn(response);  // Mocking the service response

        ResponseEntity<CommentResponse> result = commentController.update(1L, request);
        assertEquals(HttpStatus.OK, result.getStatusCode());  // Asserting the status code
        assertEquals(response, result.getBody());  // Asserting the response body
    }

    /**
     * Tests the delete method of CommentController.
     *
     * This test verifies that the delete method returns status code 204 No Content
     * when a valid comment ID is provided. It ensures that the commentService's
     * delete method is called without throwing exceptions.
     */
    @Test
    @WithMockUser(roles = "USER")
    void delete_ShouldReturnNoContent() {
        doNothing().when(commentService).delete(1L);  // Mocking the service method to do nothing

        ResponseEntity<Void> response = commentController.delete(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());  // Asserting the status code
    }

    /**
     * Tests the getByTaskId method of CommentController.
     *
     * This test verifies that the getByTaskId method returns a CommentListResponse
     * with status code 200 OK when a valid task ID is provided, along with pagination
     * parameters. It uses @WithMockUser to simulate a user with the role "USER".
     */
    @Test
    @WithMockUser(roles = "USER")
    void getByTaskId_ShouldReturnCommentListResponse() {
        CommentListResponse response = new CommentListResponse();
        when(commentService.getByTaskId(1L, 0, 10)).thenReturn(response);  // Mocking the service response

        ResponseEntity<CommentListResponse> result = commentController.getByTaskId(1L, 0, 10);
        assertEquals(HttpStatus.OK, result.getStatusCode());  // Asserting the status code
        assertEquals(response, result.getBody());  // Asserting the response body
    }

    /**
     * Tests the getByAuthorId method of CommentController.
     *
     * This test verifies that the getByAuthorId method returns a CommentListResponse
     * with status code 200 OK when a valid author ID is provided, along with pagination
     * parameters. It uses @WithMockUser to simulate a user with the role "USER".
     */
    @Test
    @WithMockUser(roles = "USER")
    void getByAuthorId_ShouldReturnCommentListResponse() {
        CommentListResponse response = new CommentListResponse();
        when(commentService.getByAuthorId(1L, 0, 10)).thenReturn(response);  // Mocking the service response

        ResponseEntity<CommentListResponse> result = commentController.getByAuthorId(1L, 0, 10);
        assertEquals(HttpStatus.OK, result.getStatusCode());  // Asserting the status code
        assertEquals(response, result.getBody());  // Asserting the response body
    }
}