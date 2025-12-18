package com.projects.tasks.services;

import com.projects.tasks.controllers.TaskListController;
import com.projects.tasks.domain.dto.TaskListDto;
import com.projects.tasks.domain.entities.TaskList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskListService {

    List<TaskList> listTaskLists();

    TaskList createTaskList(TaskList taskList);

    Optional<TaskList> getTaskList(UUID id);

    TaskList updateTaskList(UUID id, TaskList taskList);

}
