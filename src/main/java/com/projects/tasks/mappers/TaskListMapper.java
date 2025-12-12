package com.projects.tasks.mappers;

import com.projects.tasks.domain.dto.TaskListDto;
import com.projects.tasks.domain.entities.TaskList;

public interface TaskListMapper {

    TaskList fromDto(TaskListDto taskListDto);

    TaskListDto toDto(TaskList taskList);

}
