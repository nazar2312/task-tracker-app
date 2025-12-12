package com.projects.tasks.mappers;

import com.projects.tasks.domain.dto.TaskDto;
import com.projects.tasks.domain.entities.Task;

public interface TaskMapper {

    Task fromDto(TaskDto taskDto);

    TaskDto toDto(Task task);

}
