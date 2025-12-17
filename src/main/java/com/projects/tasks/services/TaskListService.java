package com.projects.tasks.services;

import com.projects.tasks.domain.dto.TaskListDto;
import com.projects.tasks.domain.entities.TaskList;

import java.util.List;

public interface TaskListService {

    List<TaskList> listTaskLists();

    TaskList createTaskList(TaskList taskList);
}
