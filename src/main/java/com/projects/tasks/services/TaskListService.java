package com.projects.tasks.services;

import com.projects.tasks.domain.entities.TaskList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskListService {

    List<TaskList> listTaskLists();

    void createTaskList(TaskList taskList);

    Optional<TaskList> getTaskList(UUID id);

    TaskList updateTaskList(UUID id, TaskList taskList);

    TaskList deleteTaskList(UUID uuid);

}
