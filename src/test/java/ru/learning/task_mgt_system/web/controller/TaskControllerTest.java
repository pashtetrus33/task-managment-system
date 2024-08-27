package ru.learning.task_mgt_system.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.learning.task_mgt_system.model.Priority;
import ru.learning.task_mgt_system.model.Status;
import ru.learning.task_mgt_system.service.TaskService;
import ru.learning.task_mgt_system.web.dto.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TaskController class.
 *
 * This test class verifies the behavior of the TaskController by testing various
 * methods for handling tasks. It ensures that the controller interacts correctly
 * with the TaskService and returns the appropriate HTTP responses.
 */
public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    private TaskListResponse emptyTaskListResponse;
    private TaskResponseWithComments taskResponseWithComments;
    private TaskResponse taskResponse;
    private TaskRequest taskRequest;
    private int defaultPage;
    private int defaultSize;
    private Status status;
    private Priority priority;
    private Long authorId;
    private Long assigneeId;

    /**
     * Sets up the test environment by initializing mock objects and setting default values.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        defaultPage = 0;
        defaultSize = 10;
        status = Status.COMPLETED;
        priority = Priority.HIGH;
        authorId = 1L;
        assigneeId = 1L;

        emptyTaskListResponse = new TaskListResponse();
        taskResponseWithComments = new TaskResponseWithComments();
        taskResponse = new TaskResponse();
        taskRequest = new TaskRequest();
    }

    /**
     * Tests the filterBy method of TaskController.
     *
     * This test verifies that the filterBy method returns a ResponseEntity with
     * status code 200 OK and the correct TaskListResponse when a TaskFilter is provided.
     */
    @Test
    void testFilterBy() {
        TaskFilter filter = new TaskFilter(); // Set filter criteria
        when(taskService.filterBy(any(TaskFilter.class))).thenReturn(emptyTaskListResponse);

        ResponseEntity<TaskListResponse> response = taskController.filterBy(filter);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyTaskListResponse, response.getBody());
        verify(taskService, times(1)).filterBy(filter);
    }

    /**
     * Tests the getAll method of TaskController.
     *
     * This test verifies that the getAll method returns a ResponseEntity with
     * status code 200 OK and the correct TaskListResponse when default pagination
     * parameters are used.
     */
    @Test
    void testGetAll() {
        when(taskService.getAll(defaultPage, defaultSize)).thenReturn(emptyTaskListResponse);

        ResponseEntity<TaskListResponse> response = taskController.getAll(defaultPage, defaultSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyTaskListResponse, response.getBody());
        verify(taskService, times(1)).getAll(defaultPage, defaultSize);
    }

    /**
     * Tests the getById method of TaskController.
     *
     * This test verifies that the getById method returns a ResponseEntity with
     * status code 200 OK and the correct TaskResponseWithComments when a task ID is provided.
     */
    @Test
    void testGetById() {
        Long id = 1L;
        when(taskService.getById(id)).thenReturn(taskResponseWithComments);

        ResponseEntity<TaskResponseWithComments> response = taskController.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskResponseWithComments, response.getBody());
        verify(taskService, times(1)).getById(id);
    }

    /**
     * Tests the create method of TaskController.
     *
     * This test verifies that the create method returns a ResponseEntity with
     * status code 201 Created and the correct TaskResponse when a TaskRequest is provided.
     */
    @Test
    void testCreate() {
        when(taskService.create(any(TaskRequest.class))).thenReturn(taskResponse);

        ResponseEntity<TaskResponse> response = taskController.create(taskRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(taskResponse, response.getBody());
        verify(taskService, times(1)).create(taskRequest);
    }

    /**
     * Tests the update method of TaskController.
     *
     * This test verifies that the update method returns a ResponseEntity with
     * status code 200 OK and the correct TaskResponse when a task ID and TaskRequest
     * are provided.
     */
    @Test
    void testUpdate() {
        Long id = 1L;
        when(taskService.update(eq(id), any(TaskRequest.class))).thenReturn(taskResponse);

        ResponseEntity<TaskResponse> response = taskController.update(id, taskRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskResponse, response.getBody());
        verify(taskService, times(1)).update(eq(id), any(TaskRequest.class));
    }

    /**
     * Tests the delete method of TaskController.
     *
     * This test verifies that the delete method returns a ResponseEntity with
     * status code 204 No Content when a task ID is provided.
     */
    @Test
    void testDelete() {
        Long id = 1L;
        doNothing().when(taskService).delete(id);

        ResponseEntity<Void> response = taskController.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskService, times(1)).delete(id);
    }

    /**
     * Tests the getByStatus method of TaskController.
     *
     * This test verifies that the getByStatus method returns a ResponseEntity with
     * status code 200 OK and the correct TaskListResponse when a status and pagination
     * parameters are provided.
     */
    @Test
    void testGetByStatus() {
        when(taskService.getByStatus(status.name(), defaultPage, defaultSize)).thenReturn(emptyTaskListResponse);

        ResponseEntity<TaskListResponse> response = taskController.getByStatus(status.name(), defaultPage, defaultSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyTaskListResponse, response.getBody());
        verify(taskService, times(1)).getByStatus(status.name(), defaultPage, defaultSize);
    }

    /**
     * Tests the getByPriority method of TaskController.
     *
     * This test verifies that the getByPriority method returns a ResponseEntity with
     * status code 200 OK and the correct TaskListResponse when a priority and pagination
     * parameters are provided.
     */
    @Test
    void testGetByPriority() {
        when(taskService.getByPriority(priority.name(), defaultPage, defaultSize)).thenReturn(emptyTaskListResponse);

        ResponseEntity<TaskListResponse> response = taskController.getByPriority(priority.name(), defaultPage, defaultSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyTaskListResponse, response.getBody());
        verify(taskService, times(1)).getByPriority(priority.name(), defaultPage, defaultSize);
    }

    /**
     * Tests the getByAuthorId method of TaskController.
     *
     * This test verifies that the getByAuthorId method returns a ResponseEntity with
     * status code 200 OK and the correct TaskListResponse when an author ID and pagination
     * parameters are provided.
     */
    @Test
    void testGetByAuthorId() {
        when(taskService.getByAuthorId(authorId, defaultPage, defaultSize)).thenReturn(emptyTaskListResponse);

        ResponseEntity<TaskListResponse> response = taskController.getByAuthorId(authorId, defaultPage, defaultSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyTaskListResponse, response.getBody());
        verify(taskService, times(1)).getByAuthorId(authorId, defaultPage, defaultSize);
    }

    /**
     * Tests the getByAssigneeId method of TaskController.
     *
     * This test verifies that the getByAssigneeId method returns a ResponseEntity with
     * status code 200 OK and the correct TaskListResponse when an assignee ID and pagination
     * parameters are provided.
     */
    @Test
    void testGetByAssigneeId() {
        when(taskService.getByAssigneeId(assigneeId, defaultPage, defaultSize)).thenReturn(emptyTaskListResponse);

        ResponseEntity<TaskListResponse> response = taskController.getByAssigneeId(assigneeId, defaultPage, defaultSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyTaskListResponse, response.getBody());
        verify(taskService, times(1)).getByAssigneeId(assigneeId, defaultPage, defaultSize);
    }

    /**
     * Tests the getByStatusAndPriority method of TaskController.
     *
     * This test verifies that the getByStatusAndPriority method returns a ResponseEntity with
     * status code 200 OK and the correct TaskListResponse when a status, priority, and pagination
     * parameters are provided.
     */
    @Test
    void testGetByStatusAndPriority() {
        when(taskService.getByStatusAndPriority(status.name(), priority.name(), defaultPage, defaultSize)).thenReturn(emptyTaskListResponse);

        ResponseEntity<TaskListResponse> response = taskController.getByStatusAndPriority(status.name(), priority.name(), defaultPage, defaultSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyTaskListResponse, response.getBody());
        verify(taskService, times(1)).getByStatusAndPriority(status.name(), priority.name(), defaultPage, defaultSize);
    }

    /**
     * Tests the getByStatusAndPriorityAndAuthorId method of TaskController.
     *
     * This test verifies that the getByStatusAndPriorityAndAuthorId method returns a ResponseEntity with
     * status code 200 OK and the correct TaskListResponse when a status, priority, author ID, and pagination
     * parameters are provided.
     */
    @Test
    void testGetByStatusAndPriorityAndAuthorId() {
        when(taskService.getByStatusAndPriorityAndAuthorId(status.name(), priority.name(), authorId, defaultPage, defaultSize)).thenReturn(emptyTaskListResponse);

        ResponseEntity<TaskListResponse> response = taskController.getByStatusAndPriorityAndAuthorId(status.name(), priority.name(), authorId, defaultPage, defaultSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyTaskListResponse, response.getBody());
        verify(taskService, times(1)).getByStatusAndPriorityAndAuthorId(status.name(), priority.name(), authorId, defaultPage, defaultSize);
    }

    /**
     * Tests the getByStatusAndPriorityAndAssigneeId method of TaskController.
     *
     * This test verifies that the getByStatusAndPriorityAndAssigneeId method returns a ResponseEntity with
     * status code 200 OK and the correct TaskListResponse when a status, priority, assignee ID, and pagination
     * parameters are provided.
     */
    @Test
    void testGetByStatusAndPriorityAndAssigneeId() {
        when(taskService.getByStatusAndPriorityAndAssigneeId(status.name(), priority.name(), assigneeId, defaultPage, defaultSize)).thenReturn(emptyTaskListResponse);

        ResponseEntity<TaskListResponse> response = taskController.getByStatusAndPriorityAndAssigneeId(status.name(), priority.name(), assigneeId, defaultPage, defaultSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyTaskListResponse, response.getBody());
        verify(taskService, times(1)).getByStatusAndPriorityAndAssigneeId(status.name(), priority.name(), assigneeId, defaultPage, defaultSize);
    }
}