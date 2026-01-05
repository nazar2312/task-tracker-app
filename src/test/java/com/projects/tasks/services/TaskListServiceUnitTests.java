package com.projects.tasks.services;

import com.projects.tasks.domain.entities.TaskList;
import com.projects.tasks.repositories.TaskListRepository;
import com.projects.tasks.services.impl.TaskListServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.projects.tasks.TestDataUtil;

import java.time.LocalDateTime;
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
    void testCreateTaskList(){

        when(repository.save(any(TaskList.class)))
                .thenReturn(new TaskList(
                        UUID.randomUUID(),
                        taskList.getTitle(),
                        taskList.getDescription(),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        null
                ));

        taskList.setId(null);
        TaskList result = service.createTaskList(taskList);


        //Assert that service returns exactly the same object that was created in DB.
        assertNotEquals(taskList.getId(), result.getId());
        assertEquals(taskList.getTitle(), result.getTitle());
        assertEquals(taskList.getDescription(), result.getDescription());


    }

    @Test
    void testGetTaskList(){

        when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.of(taskList));

        Optional<TaskList> result = service.getTaskList(UUID.randomUUID());

        //Assert that result is not found
        assertFalse(result.isEmpty());

        //Assert that task list is match the task list that was created
        assertEquals(result.get().getTitle(), taskList.getTitle());
        assertEquals(result.get().getDescription(), taskList.getDescription());

    }

    @Test
    void testUpdateTaskList(){

        when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.of(taskList));

        TaskList updatedTaskList = service.updateTaskList(taskList.getId(), secondTaskList);

        verify(repository, times(1))
                .save(any(TaskList.class));

        assertEquals(updatedTaskList.getTitle(), secondTaskList.getTitle());
        assertEquals(updatedTaskList.getId(), taskList.getId());

    }

    @Test
    void testDeleteTaskList(){

        when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.of(taskList));

        TaskList deletedTaskList = service.deleteTaskList(taskList.getId());

        assertEquals(deletedTaskList.getId(), taskList.getId());

        verify(repository).deleteById(taskList.getId());


    }

}
