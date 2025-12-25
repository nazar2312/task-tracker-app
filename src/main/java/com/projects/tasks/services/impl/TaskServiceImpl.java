package com.projects.tasks.services.impl;

import com.projects.tasks.domain.entities.Task;
import com.projects.tasks.domain.entities.TaskList;
import com.projects.tasks.repositories.TaskListRepository;
import com.projects.tasks.repositories.TaskRepository;
import com.projects.tasks.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    /*
        Inject repository dependency;
     */
    private final TaskRepository repository;
    private final TaskListRepository taskListRepository;

    public TaskServiceImpl(
            TaskRepository repository,
            TaskListRepository taskListRepository){

        this.repository = repository;
        this.taskListRepository = taskListRepository;
    }

    @Override
    public List<Task> listAllTasks(UUID taskListId) {
        return repository.findByTaskListId(taskListId);
    }

    @Override
    public Optional<Task> getTaskById(UUID taskListId, UUID taskId) {

        Optional<Task> response = repository.findById(taskId);

        if(response.isEmpty())
            throw new EntityNotFoundException("Task does not exist");

        return response;
    }


    @Override
    public Task createTask(UUID taskListId, Task task) {

        Optional<TaskList> taskList = taskListRepository.findById(taskListId);

        if(taskList.isEmpty()) throw new EntityNotFoundException("Task list is not found!");

        Task taskToSave = new Task(
                null,
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getStatus(),
                task.getPriority(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                taskList.get()

        );

        return repository.save(taskToSave);
    }

    @Override
    public ResponseEntity<Task> updateTask(UUID taskListId, UUID taskIdToUpdate, Task task) {

        Task taskToUpdate;

        if(repository.findByTaskListIdAndId(taskListId, taskIdToUpdate).isEmpty())
            throw new EntityNotFoundException("Task to update is not found!");
        else
            taskToUpdate = repository.findByTaskListIdAndId(taskListId, taskIdToUpdate).get();


        if(taskToUpdate.getId().equals(task.getId()) || task.getId() == null)
            taskToUpdate.setId(taskToUpdate.getId());
        else
            throw new IllegalArgumentException("Task id cannot be updated!");


        taskToUpdate.setTitle(task.getTitle());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setDueDate(task.getDueDate());
        taskToUpdate.setStatus(task.getStatus());
        taskToUpdate.setPriority(task.getPriority());
        taskToUpdate.setUpdated(LocalDateTime.now());

        repository.save(taskToUpdate);

        return ResponseEntity.status(HttpStatus.OK).body(taskToUpdate);
    }

    @Override
    public ResponseEntity<Task> deleteTask(UUID taskListId, UUID taskIdToDelete) {

        Optional<Task> taskToDelete = repository.findByTaskListIdAndId(taskListId, taskIdToDelete);
        if(taskToDelete.isEmpty()) throw new EntityNotFoundException("Task to delete is not found!");

        repository.deleteTaskByIdAndTaskListId(taskListId, taskIdToDelete);

        return ResponseEntity.status(HttpStatus.OK).body(taskToDelete.get());

    }
}
