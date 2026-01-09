package com.projects.tasks.services;

import com.projects.tasks.domain.entities.TaskList;
import com.projects.tasks.repositories.TaskListRepository;
import com.projects.tasks.services.impl.TaskListServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.projects.tasks.TestDataUtil;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskListServiceUnitTests {

    private TaskList taskList;
    private TaskList secondTaskList;

    @Mock
    private TaskListRepository repository;

    @InjectMocks
    private TaskListServiceImpl service;

    //Set up new task list before each test;
    @BeforeEach
    void setUp(){

        taskList = TestDataUtil.createTaskList();
        secondTaskList = TestDataUtil.createSecondTaskList();
    }

    @Test
    void createTaskList_shouldThrowException_whenIdIsPresent(){

        taskList.setId(UUID.randomUUID());

        assertThrows(IllegalArgumentException.class,
                () -> service.createTaskList(taskList));
    }

    @Test
    void createTaskList_shouldThrowException_whenTitleIsNotPresent() {

        taskList.setTitle("");

        assertThrows(IllegalArgumentException.class,
                () -> service.createTaskList(taskList));
    }

    @Test
    void createTaskList_shouldReturnTaskList_whenAllValuesAreCorrect() {

        when(repository.save(any(TaskList.class)))
                .thenReturn(taskList);

        TaskList savedTaskList = service.createTaskList(taskList);

        assertEquals(taskList.getTitle(), savedTaskList.getTitle());

        verify(repository, times(1)).save(any(TaskList.class));

    }


    @Test
    void getTaskList_shouldThrowException_whenTaskListIsNotFound() {

        when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.getTaskList(UUID.randomUUID()));

    }

    @Test
    void getTaskList_shouldReturnTaskList_whenAllValuesAreCorrect() {

        when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.of(taskList));

        TaskList returnedTaskList = service.getTaskList(UUID.randomUUID());

        assertEquals(taskList.getId(), returnedTaskList.getId());
        assertEquals(taskList.getTitle(), returnedTaskList.getTitle());

    }

    @Test
    void updateTaskList_shouldThrowException_whenTaskListIsNotFound() {

        UUID id = UUID.randomUUID();

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.updateTaskList(id, secondTaskList));

        verify(repository, never()).save(any(TaskList.class));
    }

    @Test
    void updateTaskList_shouldThrowException_whenIdsAreDifferent() {

        taskList.setId(UUID.randomUUID());
        secondTaskList.setId(UUID.randomUUID());

        when(repository.findById(taskList.getId()))
                .thenReturn(Optional.of(taskList));

        assertThrows(IllegalArgumentException.class,
                () -> service.updateTaskList(taskList.getId(), secondTaskList));

        verify(repository, never()).save(any(TaskList.class));
    }

    @Test
    void updateTaskList_shouldReturnTaskList_whenAllConditionsAreMet() {

        when(repository.findById(taskList.getId()))
                .thenReturn(Optional.of(taskList));

        when(repository.save(any(TaskList.class)))
                .thenReturn(secondTaskList);

        TaskList returnedTaskList = service.updateTaskList(taskList.getId(), secondTaskList);

        //Verify that taskList was updated correctly;
        assertNotEquals(returnedTaskList.getTitle(), taskList.getTitle());
        assertNotEquals(returnedTaskList.getDescription(), taskList.getDescription());

        verify(repository, times(1)).save(any(TaskList.class));

    }

    @Test
    void deleteTaskList_shouldThrowException_whenTaskListIsNotFound() {

        when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.deleteTaskList(UUID.randomUUID()));

        verify(repository, never()).deleteById(any(UUID.class));

    }

    @Test
    void deleteTaskList_shouldReturnDeletedTaskList_whenAllConditionsAreMet() {

        when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.of(taskList));

        TaskList deletedTaskList = service.deleteTaskList(UUID.randomUUID());

        verify(repository, times(1))
                .deleteById(any(UUID.class));

        assertEquals(deletedTaskList.getTitle(), taskList.getTitle());
    }

}
