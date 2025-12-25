package com.projects.tasks.services;

import com.projects.tasks.domain.dto.TaskDto;
import com.projects.tasks.domain.entities.Task;
import com.projects.tasks.domain.entities.TaskList;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {

    List<Task> listAllTasks(UUID taskListId);

    Optional<Task> getTaskById(UUID taskListId, UUID taskId);

    Task createTask(UUID taskListId, Task task);

    ResponseEntity<Task> updateTask(UUID taskIdToUpdate, UUID taskListId, Task task);

    ResponseEntity<Task> deleteTask(UUID taskListId, UUID taskIdToDelete);


}
