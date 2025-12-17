package com.projects.tasks.services.impl;

import com.projects.tasks.domain.entities.TaskList;
import com.projects.tasks.repositories.TaskListRepository;
import com.projects.tasks.services.TaskListService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskListServiceImpl implements TaskListService {

    private TaskListRepository repository;

    public TaskListServiceImpl (TaskListRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TaskList> listTaskLists(){
        return repository.findAll();
    }



}
