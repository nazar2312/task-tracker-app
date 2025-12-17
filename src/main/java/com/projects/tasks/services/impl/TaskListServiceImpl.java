package com.projects.tasks.services.impl;

import com.projects.tasks.domain.entities.TaskList;
import com.projects.tasks.mappers.TaskListMapper;
import com.projects.tasks.repositories.TaskListRepository;
import com.projects.tasks.services.TaskListService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        ));

    }


}
