package ru.learning.task_mgt_system.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.learning.task_mgt_system.service.UserService;
import ru.learning.task_mgt_system.web.dto.UserListResponse;
import ru.learning.task_mgt_system.web.dto.UserResponse;

/**
 * Unit tests for the UserController class.
 *
 * This test class verifies the behavior of the UserController by testing its methods
 * for handling user-related operations. It ensures that the controller interacts correctly
 * with the UserService and returns the appropriate HTTP responses.
 */
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    /**
     * Sets up the test environment by initializing mock objects.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the getAll method of UserController.
     *
     * This test verifies that the getAll method returns a ResponseEntity with
     * status code 200 OK and the correct UserListResponse when pagination parameters
     * are provided.
     */
    @Test
    void getAll_ShouldReturnUserListResponse() {
        UserListResponse userListResponse = new UserListResponse();
        when(userService.getAll(anyInt(), anyInt())).thenReturn(userListResponse);

        ResponseEntity<UserListResponse> response = userController.getAll(0, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userListResponse, response.getBody());
    }

    /**
     * Tests the getById method of UserController.
     *
     * This test verifies that the getById method returns a ResponseEntity with
     * status code 200 OK and the correct UserResponse when a user ID is provided.
     */
    @Test
    void getById_ShouldReturnUserResponse() {
        UserResponse userResponse = new UserResponse();
        when(userService.getById(anyLong())).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.getById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
    }
}