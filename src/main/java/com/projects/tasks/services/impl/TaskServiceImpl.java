package com.projects.tasks.services.impl;

import com.projects.tasks.domain.entities.Task;
import com.projects.tasks.domain.entities.TaskList;
import com.projects.tasks.repositories.TaskListRepository;
import com.projects.tasks.repositories.TaskRepository;
import com.projects.tasks.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    public Task getTaskById(UUID taskListId, UUID taskId) {

        return repository.findByTaskListIdAndId(taskListId, taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task is not found"));
    }


    @Transactional
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

    @Transactional
    @Override
    public Task updateTask(UUID taskListId, UUID taskIdToUpdate, Task task) {

        Task taskToUpdate;

        if(repository.findByTaskListIdAndId(taskListId, taskIdToUpdate).isEmpty())
            throw new EntityNotFoundException("Task to update is not found!");

        taskToUpdate = repository.findByTaskListIdAndId(taskListId, taskIdToUpdate).get();

        if(task.getId() != null)
            throw new IllegalArgumentException("Task id cannot be changed/updated!");


        taskToUpdate.setTitle(task.getTitle());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setDueDate(task.getDueDate());
        taskToUpdate.setStatus(task.getStatus());
        taskToUpdate.setPriority(task.getPriority());
        taskToUpdate.setUpdated(LocalDateTime.now());

        return repository.save(taskToUpdate);
    }

    @Transactional
    @Override
    public Task deleteTask(UUID taskListId, UUID taskId) {

        Optional<Task> taskToDelete = repository.findByTaskListIdAndId(taskListId, taskId);

        if(taskToDelete.isEmpty())
            throw new EntityNotFoundException("Task to delete is not found!");

        int result = repository.deleteByTaskListIdAndId(taskListId, taskId);

        if(result != 1) throw new IllegalArgumentException();
        else return taskToDelete.get();
    }
}
