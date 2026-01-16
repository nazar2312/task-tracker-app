package com.projects.tasks.services.impl;

import com.projects.tasks.domain.entities.TaskList;
import com.projects.tasks.repositories.TaskListRepository;
import com.projects.tasks.services.TaskListService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository repository;

    public TaskListServiceImpl (TaskListRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TaskList> listTaskLists(){
        return repository.findAll();
    }

    @Override
    public TaskList createTaskList(TaskList taskList) {

        if(taskList.getId() != null)
            throw new IllegalArgumentException("Illegal argument! ID is NOT required!");

        if(taskList.getTitle() == null || taskList.getTitle().isBlank())
            throw new IllegalArgumentException("Incorrect values! Title must be provided!");

        return repository.save(
                new TaskList(
                        null,
                        taskList.getTitle(),
                        taskList.getDescription(),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        null
                )
        );

    }

    @Override
    public TaskList getTaskList(UUID id){

        Optional<TaskList> response = repository.findById(id);

        if(response.isEmpty()) throw new EntityNotFoundException("not found(((");

        return response.get();
    }

    @Transactional
    @Override
    public TaskList updateTaskList(UUID id, TaskList taskList){

        TaskList existingTaskList = repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Entity not found"));

        if(taskList.getId() != null && id != taskList.getId())
            throw new IllegalArgumentException("You cannot change an ID!");

        existingTaskList.setTitle(taskList.getTitle());
        existingTaskList.setDescription(taskList.getDescription());
        existingTaskList.setUpdated(LocalDateTime.now());
        existingTaskList.setTasks(taskList.getTasks());

        return repository.save(existingTaskList);
    }

    @Transactional
    @Override
    public TaskList deleteTaskList(UUID id) {

        Optional<TaskList> toDelete = repository.findById(id);

        if(toDelete.isEmpty())
            throw new EntityNotFoundException("Task list does not exists!");

        repository.deleteById(id);

        return toDelete.get();
    }




}
