package ru.learning.task_mgt_system.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.learning.task_mgt_system.service.UserService;
import ru.learning.task_mgt_system.web.dto.UserListResponse;
import ru.learning.task_mgt_system.web.dto.UserResponse;

/**
 * Controller for managing users.
 * Provides methods for retrieving user information.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Retrieves a paginated list of all users.
     *
     * @param page the page number for pagination (default is 0).
     * @param size the page size for pagination (default is 10).
     * @return a paginated list of all users.
     */
    @Operation(
            summary = "Get all users",
            description = "Retrieves a paginated list of all users.",
            parameters = {
                    @Parameter(name = "page", description = "The page number for pagination", example = "0"),
                    @Parameter(name = "size", description = "The page size for pagination", example = "10")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
            @ApiResponse(responseCode = "400", description = "Bad request due to invalid parameters")
    })
    @GetMapping
    public ResponseEntity<UserListResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAll(page, size));
    }

    /**
     * Retrieves a user by its identifier.
     *
     * @param id the identifier of the user.
     * @return the user information.
     */
    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a user by its identifier.",
            parameters = {
                    @Parameter(name = "id", description = "The identifier of the user", example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }
}