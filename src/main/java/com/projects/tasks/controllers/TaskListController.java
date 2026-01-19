package com.projects.tasks.controllers;

import com.projects.tasks.domain.dto.TaskListDto;
import com.projects.tasks.domain.entities.TaskList;
import com.projects.tasks.mappers.TaskListMapper;
import com.projects.tasks.services.TaskListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Reader;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/task-lists")
@Tag(
        name = "Task List",
        description = "Endpoints for managing task lists"
)
public class TaskListController {

    private final TaskListService service;
    private final TaskListMapper mapper;

    public TaskListController(TaskListService service, TaskListMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(
            description = "Get endpoint for task lists",
            summary = "Find and list all task lists in the database",
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
    public List<TaskListDto> listTaskLists() {
        return service.listTaskLists()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Operation(
            description = "Post endpoint for task lists",
            summary = "Create task lists in the database",
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
    public ResponseEntity<TaskListDto> createTaskList(@RequestBody TaskListDto taskListDto) {

        TaskList taskList = mapper.fromDto(taskListDto);
        service.createTaskList(taskList);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            description = "Get endpoint for task lists",
            summary = "Find task lists by ID",
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
    @GetMapping(path = "/{task_list_id}")
    public ResponseEntity<TaskListDto> getTaskList(
            @PathVariable("task_list_id") UUID taskListId){

        TaskListDto body = mapper.toDto(service.getTaskList(taskListId));

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @Operation(
            description = "Update endpoint for task lists",
            summary = "Update task lists by ID",
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
    @PutMapping(path = "/{task_list_id}")
    public ResponseEntity<TaskListDto> fullUpdateTaskList(
            @PathVariable("task_list_id") UUID taskListId,
            @RequestBody TaskListDto newTaskList){

        TaskListDto body = mapper.toDto(service.updateTaskList(taskListId, mapper.fromDto(newTaskList)));

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @Operation(
            description = "Delete endpoint for task lists",
            summary = "Delete task list by ID",
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
    @DeleteMapping(path = "/{task_list_id}")
    public ResponseEntity<TaskListDto> delete(@PathVariable("task_list_id") UUID id){

        TaskListDto body = mapper.toDto(service.deleteTaskList(id));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(body);
    }
}
