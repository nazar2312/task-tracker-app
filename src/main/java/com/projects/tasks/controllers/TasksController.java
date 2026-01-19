package com.projects.tasks.controllers;

import com.projects.tasks.domain.dto.TaskDto;
import com.projects.tasks.domain.entities.Task;
import com.projects.tasks.mappers.TaskMapper;
import com.projects.tasks.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/task-lists/{task_list_id}/tasks")
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

    @GetMapping
    public List<TaskDto> findAll(@PathVariable("task_list_id") UUID id) {

        return service.listAllTasks(id)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(
            @PathVariable("task_list_id") UUID id,
            @RequestBody TaskDto task) {

        service.createTask(id, mapper.fromDto(task));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TaskDto> findById(
            @PathVariable("task_list_id") UUID taskListId,
            @PathVariable("id") UUID id) {

        TaskDto response = mapper.toDto(service.getTaskById(taskListId, id));

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable("task_list_id") UUID taskListId,
            @PathVariable("id") UUID id,
            @RequestBody TaskDto taskDto){

        Task response = service.updateTask(taskListId, id, mapper.fromDto(taskDto));

        return ResponseEntity.status(HttpStatus.OK).body(mapper.toDto(response));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<TaskDto> deleteTaskById(
            @PathVariable("task_list_id") UUID taskListId,
            @PathVariable("id") UUID taskId) {

        Task response = service.deleteTask(taskListId, taskId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(mapper.toDto(response));
    }






}
