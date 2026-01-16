package com.projects.tasks.services;

import com.projects.tasks.TestDataUtil;
import com.projects.tasks.domain.entities.Task;
import com.projects.tasks.domain.entities.TaskList;
import com.projects.tasks.repositories.TaskListRepository;
import com.projects.tasks.repositories.TaskRepository;
import com.projects.tasks.services.impl.TaskServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskUnitTests {

    private Task task;
    private TaskList taskList;
    private Task taskToUpdate;

    @Mock
    private TaskRepository repository;
    @Mock
    private TaskListRepository taskListRepository;

    @InjectMocks
    private TaskServiceImpl service;

    @BeforeEach
    public void setUp() {

        this.task = TestDataUtil.createTask();
        this.taskList = TestDataUtil.createTaskList();
        this.taskToUpdate = TestDataUtil.createTaskToUpdate();

        task.setTaskList(taskList);
    }

    @Test
    void createTask_shouldSaveTask_whenTaskListExists () {

        //Return saved task when calling repository.
        when(repository.save(any(Task.class)))
                .thenReturn(task);

        //return taskList when calling findById method with any UUID.
        when(taskListRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(taskList));

        Task savedTask = service.createTask(UUID.randomUUID(), task);

        //verify that service returns exact save task that was saved in db;
        assertEquals(task.getId(), savedTask.getId());
        assertEquals(task.getTitle(), savedTask.getTitle());
        assertEquals(task.getTaskList(), savedTask.getTaskList());

        //verify that service calls save() no more that one time per call;
        verify(repository, times(1))
                .save(any(Task.class));

    }

    @Test
    void createTask_shouldThrowException_whenTaskListNotFound() {


        //return empty optional when taskList is not found;
        when(taskListRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        //verify that service throws exception when taskList does not exist;
        assertThrows(EntityNotFoundException.class,
                () -> service.createTask(UUID.randomUUID(), task)
        );

        //verify that save() is never called when exception occurs;
        verify(repository, never()).save(any());

    }

    @Test
    void getTaskById_shouldReturnTask_whenTaskExists(){

        when(repository.findByTaskListIdAndId(taskList.getId(), task.getId()))
                .thenReturn(Optional.of(task));

        Task response = service.getTaskById(taskList.getId(), task.getId());

        assertEquals(response.getId(), task.getId());
        assertEquals(response.getTitle(), task.getTitle());
        assertEquals(response.getTaskList(), task.getTaskList());

        verify(repository, times(1))
                .findByTaskListIdAndId(taskList.getId(), task.getId());

    }

    @Test
    void getTaskById_shouldThrowException_whenTaskIsNotFound() {

        when(repository.findByTaskListIdAndId(taskList.getId(), task.getId()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.getTaskById(taskList.getId(), task.getId()));
    }

    @Test
    void updateTask_shouldReturnUpdatedTask_whenAllValuesAreCorrect() {

        UUID id = UUID.randomUUID();
        task.setId(id);

        when(repository.findByTaskListIdAndId(taskList.getId(), task.getId()))
                .thenReturn(Optional.of(task));

        when(repository.save(any(Task.class)))
                .thenReturn(taskToUpdate);

        Task response = service.updateTask(taskList.getId(), task.getId(), taskToUpdate);

        verify(repository, times(1)).save(any(Task.class));

        assertEquals(response.getTitle(), taskToUpdate.getTitle());
        assertEquals(response.getDescription(), taskToUpdate.getDescription());
        assertEquals(response.getTaskList(), taskToUpdate.getTaskList());

    }

    @Test
    void updateTask_shouldThrowException_whenTaskToUpdateIsNotFound() {

        when(repository.findByTaskListIdAndId(taskList.getId(), task.getId()))
                .thenReturn(Optional.empty());


        assertThrows(EntityNotFoundException.class,
                () -> service.updateTask(taskList.getId(), task.getId(), taskToUpdate));

        verify(repository, times(1))
                .findByTaskListIdAndId(taskList.getId(), task.getId());

        verify(repository, never()).save(any(Task.class));
    }

    @Test
    void updateTask_shouldThrowException_whenAttemptedToChangeId() {

        task.setId(UUID.randomUUID());
        taskToUpdate.setId(UUID.randomUUID());

        when(repository.findByTaskListIdAndId(taskList.getId(), task.getId()))
                .thenReturn(Optional.of(task));

        assertThrows(IllegalArgumentException.class,
                () -> service.updateTask(taskList.getId(), task.getId(), taskToUpdate));

        verify(repository, never()).save(any());

    }

    @Test
    void deleteTask_shouldThrowException_whenTaskIsNotFound() {

        when(repository.findByTaskListIdAndId(taskList.getId(), task.getId()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.deleteTask(taskList.getId(), task.getId()));
    }


    @Test
    void deleteTask_shouldReturnDeletedTask_whenSuccessful() {

        when(repository.findByTaskListIdAndId(taskList.getId(), task.getId()))
                .thenReturn(Optional.of(task));

        when(repository.deleteByTaskListIdAndId(taskList.getId(), task.getId()))
                .thenReturn(task);

        Task deletedTask = service.deleteTask(taskList.getId(), task.getId());

        assertEquals(deletedTask.getId(), task.getId());
        assertEquals(deletedTask.getTitle(), task.getTitle());
        assertEquals(deletedTask.getTaskList(), task.getTaskList());
    }


}
