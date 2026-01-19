package com.projects.tasks.controllers;

import com.projects.tasks.domain.dto.TaskDto;
import com.projects.tasks.domain.entities.Task;
import com.projects.tasks.mappers.TaskMapper;
import com.projects.tasks.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/task-lists/{task_list_id}/tasks")
@Tag(
        name = "Tasks",
        description = "Endpoints for managing tasks within task lists"
)
public class TasksController {

    /*
        Inject dependencies;
     */
    private final TaskService service;
    private final TaskMapper mapper;

    public TasksController(TaskService service, TaskMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(
            description = "Get endpoint for task",
            summary = "Find and list all tasks in the database",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Failure",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping
    public List<TaskDto> findAll(@PathVariable("task_list_id") UUID id) {

        return service.listAllTasks(id)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Operation(
            description = "Create endpoint for task",
            summary = "Create task in the database",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "Failure",
                            responseCode = "400"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<TaskDto> createTask(
            @PathVariable("task_list_id") UUID id,
            @RequestBody TaskDto task) {

        TaskDto created = mapper.toDto(
                service.createTask(id, mapper.fromDto(task))
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            description = "Get endpoint for task",
            summary = "Find tasks by ID",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Failure",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping(path = "/{id}")
    public ResponseEntity<TaskDto> findById(
            @PathVariable("task_list_id") UUID taskListId,
            @PathVariable("id") UUID id) {

        TaskDto response = mapper.toDto(service.getTaskById(taskListId, id));

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @Operation(
            description = "Update endpoint for task",
            summary = "Update task by ID",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Failure",
                            responseCode = "404"
                    )
            }
    )
    @PatchMapping(path = "/{id}")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable("task_list_id") UUID taskListId,
            @PathVariable("id") UUID id,
            @RequestBody TaskDto taskDto){

        Task response = service.updateTask(taskListId, id, mapper.fromDto(taskDto));

        return ResponseEntity.status(HttpStatus.OK).body(mapper.toDto(response));
    }

    @Operation(
            description = "Delete endpoint for task",
            summary = "Delete task by ID",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Failure",
                            responseCode = "404"
                    )
            }
    )
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTaskById(
            @PathVariable("task_list_id") UUID taskListId,
            @PathVariable("id") UUID taskId) {

        service.deleteTask(taskListId, taskId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }






}
