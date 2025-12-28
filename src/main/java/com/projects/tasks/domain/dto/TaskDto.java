package com.projects.tasks.domain.dto;

import com.projects.tasks.domain.entities.TaskPriority;
import com.projects.tasks.domain.entities.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/*
    TaskDto is transfer object between persistence layer and service layer.
    It excludes all unnecessary/vulnerable data.
    Java Record type used to provide constructors, getter/setter etc. for all instance variables
 */
public record TaskDto(
        UUID id,
        String title,
        String description,
        LocalDateTime dueDate,
        TaskPriority priority,
        TaskStatus status
) {

}