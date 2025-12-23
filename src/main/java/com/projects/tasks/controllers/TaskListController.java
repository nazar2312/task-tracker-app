package com.projects.tasks.controllers;

import com.projects.tasks.domain.dto.TaskListDto;
import com.projects.tasks.domain.entities.TaskList;
import com.projects.tasks.mappers.TaskListMapper;
import com.projects.tasks.services.TaskListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/task-lists")
public class TaskListController {

    private final TaskListService service;
    private final TaskListMapper mapper;

    public TaskListController(TaskListService service, TaskListMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<TaskListDto> listTaskLists() {
        return service.listTaskLists()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<TaskListDto> createTaskList(@RequestBody TaskListDto taskListDto) {
        TaskList taskList = mapper.fromDto(taskListDto);
        service.createTaskList(taskList);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/{task_list_id}")
    public Optional<TaskListDto> getTaskList(
            @PathVariable("task_list_id") UUID taskListId){

        return service.getTaskList(taskListId).map(mapper::toDto);
    }

    @PutMapping(path = "/{task_list_id}")
    public TaskListDto fullUpdateTaskList(
            @PathVariable("task_list_id") UUID taskListId,
            @RequestBody TaskListDto newTaskList){

        TaskList updatedTaskList = service.updateTaskList(
                taskListId,
                mapper.fromDto(newTaskList));
        return mapper.toDto(updatedTaskList);
    }

    @DeleteMapping(path = "/{task_list_id}")
    public TaskListDto delete(@PathVariable("task_list_id") UUID id){

        return mapper.toDto(service.deleteTaskList(id));
    }
}
