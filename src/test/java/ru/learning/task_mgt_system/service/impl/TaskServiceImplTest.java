package ru.learning.task_mgt_system.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.learning.task_mgt_system.exception.EntityNotFoundException;
import ru.learning.task_mgt_system.model.*;
import ru.learning.task_mgt_system.model.mapper.TaskMapper;
import ru.learning.task_mgt_system.repository.TaskRepository;
import ru.learning.task_mgt_system.service.UserService;
import ru.learning.task_mgt_system.web.dto.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link TaskServiceImpl} class.
 * <p>
 * This class tests the various functionalities of the {@link TaskServiceImpl},
 * including task creation, retrieval by different criteria, and handling
 * exceptions when a task is not found.
 */
class TaskServiceImplTest {

    /**
     * Injects a mock of {@link TaskServiceImpl} into this test class.
     */
    @InjectMocks
    private TaskServiceImpl taskService;

    /**
     * Mocks the {@link TaskRepository} to simulate database interactions.
     */
    @Mock
    private TaskRepository taskRepository;

    /**
     * Mocks the {@link UserService} to simulate user-related operations.
     */
    @Mock
    private UserService userService;

    /**
     * Mocks the {@link TaskMapper} to simulate mapping between entities and DTOs.
     */
    @Mock
    private TaskMapper taskMapper;

    private Task task;
    private TaskRequest taskRequest;
    private TaskResponse taskResponse;
    private TaskListResponse taskListResponse;
    private TaskResponseWithComments taskResponseWithComments;

    /**
     * Initializes mocks and test data before each test method execution.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        initializeTestData();
    }

    /**
     * Initializes the test data used in various test cases.
     */
    private void initializeTestData() {
        task = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .status(Status.PENDING)
                .priority(Priority.HIGH)
                .build();

        taskRequest = new TaskRequest();
        taskRequest.setTitle("Test Task");
        taskRequest.setDescription("Test Description");
        taskRequest.setStatus("PENDING");
        taskRequest.setPriority("HIGH");

        taskResponse = new TaskResponse();
        taskResponse.setId(1L);
        taskResponse.setTitle("Test Task");
        taskResponse.setDescription("Test Description");
        taskResponse.setStatus("PENDING");
        taskResponse.setPriority("HIGH");

        taskResponseWithComments = new TaskResponseWithComments();
        taskResponseWithComments.setId(1L);
        taskResponseWithComments.setTitle("Test Task");
        taskResponseWithComments.setDescription("Test Description");
        taskResponseWithComments.setStatus("PENDING");
        taskResponseWithComments.setPriority("HIGH");

        taskListResponse = new TaskListResponse();
        taskListResponse.setTasks(List.of(taskResponse));
    }

    /**
     * Tests the creation of a task using {@link TaskServiceImpl#create(TaskRequest)}.
     * <p>
     * Ensures that the task is created and saved correctly in the repository.
     */
    @Test
    void createTask() {
        when(userService.createOrRetrieveUser()).thenReturn(new User());
        when(userService.getById(anyLong())).thenReturn(new UserResponse());
        when(taskMapper.taskRequestToTask(any(TaskRequest.class))).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.taskToResponse(any(Task.class))).thenReturn(taskResponse);

        TaskResponse response = taskService.create(taskRequest);

        assertNotNull(response);
        assertEquals("Test Task", response.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    /**
     * Tests the retrieval of a task by its ID using {@link TaskServiceImpl#getById(Long)}.
     * <p>
     * Ensures that the task is returned correctly when found.
     */
    @Test
    void getById_TaskFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(taskMapper.taskToResponseWithComments(any(Task.class))).thenReturn(taskResponseWithComments);

        TaskResponseWithComments response = taskService.getById(1L);

        assertNotNull(response);
        verify(taskRepository, times(1)).findById(anyLong());
    }

    /**
     * Tests the retrieval of a task by its ID when the task is not found.
     * <p>
     * Ensures that an {@link EntityNotFoundException} is thrown with the appropriate message.
     */
    @Test
    void getById_TaskNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> taskService.getById(1L));
        assertEquals("Task with ID 1 not found.", exception.getMessage());
    }

    /**
     * Tests the retrieval of all tasks using {@link TaskServiceImpl#getAll(int, int)}.
     * <p>
     * Ensures that all tasks are retrieved and mapped to a {@link TaskListResponse}.
     */
    @Test
    void getAllTasks() {
        Pageable pageable = PageRequest.of(0, 10);
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());
        when(taskMapper.taskListToTaskListResponse(any(Page.class))).thenReturn(taskListResponse);

        TaskListResponse response = taskService.getAll(0, 10);

        assertNotNull(response);
        verify(taskRepository, times(1)).findAll(any(Pageable.class));
    }

    /**
     * Tests the retrieval of tasks by status using {@link TaskServiceImpl#getByStatus(String, int, int)}.
     * <p>
     * Ensures that tasks with the specified status are retrieved and mapped to a {@link TaskListResponse}.
     */
    @Test
    void getByStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        when(taskRepository.findByStatus(any(Status.class), any(Pageable.class))).thenReturn(Page.empty());
        when(taskMapper.taskListToTaskListResponse(any(Page.class))).thenReturn(taskListResponse);

        TaskListResponse response = taskService.getByStatus("PENDING", 0, 10);

        assertNotNull(response);
        verify(taskRepository, times(1)).findByStatus(any(Status.class), any(Pageable.class));
    }

    /**
     * Tests the retrieval of tasks by priority using {@link TaskServiceImpl#getByPriority(String, int, int)}.
     * <p>
     * Ensures that tasks with the specified priority are retrieved and mapped to a {@link TaskListResponse}.
     */
    @Test
    void getByPriority() {
        Pageable pageable = PageRequest.of(0, 10);
        when(taskRepository.findByPriority(any(Priority.class), any(Pageable.class))).thenReturn(Page.empty());
        when(taskMapper.taskListToTaskListResponse(any(Page.class))).thenReturn(taskListResponse);

        TaskListResponse response = taskService.getByPriority("HIGH", 0, 10);

        assertNotNull(response);
        verify(taskRepository, times(1)).findByPriority(any(Priority.class), any(Pageable.class));
    }

    /**
     * Tests the retrieval of tasks by author ID using {@link TaskServiceImpl#getByAuthorId(Long, int, int)}.
     * <p>
     * Ensures that tasks authored by the specified user are retrieved and mapped to a {@link TaskListResponse}.
     */
    @Test
    void getByAuthorId() {
        Pageable pageable = PageRequest.of(0, 10);
        when(userService.getById(anyLong())).thenReturn(new UserResponse());
        when(taskRepository.findByAuthorId(anyLong(), any(Pageable.class))).thenReturn(Page.empty());
        when(taskMapper.taskListToTaskListResponse(any(Page.class))).thenReturn(taskListResponse);

        TaskListResponse response = taskService.getByAuthorId(1L, 0, 10);

        assertNotNull(response);
        verify(taskRepository, times(1)).findByAuthorId(anyLong(), any(Pageable.class));
    }

    /**
     * Tests the retrieval of tasks by assignee ID using {@link TaskServiceImpl#getByAssigneeId(Long, int, int)}.
     * <p>
     * Ensures that tasks assigned to the specified user are retrieved and mapped to a {@link TaskListResponse}.
     */
    @Test
    void getByAssigneeId() {
        Pageable pageable = PageRequest.of(0, 10);
        when(userService.getById(anyLong())).thenReturn(new UserResponse());
        when(taskRepository.findByAssigneeId(anyLong(), any(Pageable.class))).thenReturn(Page.empty());
        when(taskMapper.taskListToTaskListResponse(any(Page.class))).thenReturn(taskListResponse);

        TaskListResponse response = taskService.getByAssigneeId(1L, 0, 10);

        assertNotNull(response);
        verify(taskRepository, times(1)).findByAssigneeId(anyLong(), any(Pageable.class));
    }

    /**
     * Tests the retrieval of tasks by both status and priority using {@link TaskServiceImpl#getByStatusAndPriority(String, String, int, int)}.
     * <p>
     * Ensures that tasks with the specified status and priority are retrieved and mapped to a {@link TaskListResponse}.
     */
    @Test
    void getByStatusAndPriority() {
        Pageable pageable = PageRequest.of(0, 10);
        when(taskRepository.findByStatusAndPriority(any(Status.class), any(Priority.class), any(Pageable.class))).thenReturn(Page.empty());
        when(taskMapper.taskListToTaskListResponse(any(Page.class))).thenReturn(taskListResponse);

        TaskListResponse response = taskService.getByStatusAndPriority("PENDING", "HIGH", 0, 10);

        assertNotNull(response);
        verify(taskRepository, times(1)).findByStatusAndPriority(any(Status.class), any(Priority.class), any(Pageable.class));
    }
}