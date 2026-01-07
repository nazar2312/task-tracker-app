package com.projects.tasks;

import com.projects.tasks.domain.entities.Task;
import com.projects.tasks.domain.entities.TaskList;
import com.projects.tasks.domain.entities.TaskPriority;
import com.projects.tasks.domain.entities.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/*
    Class that helps to create test data samples;
 */

public final class TestDataUtil {

    public static TaskList createTaskList(){

        return new TaskList(
                UUID.randomUUID(),
                "TaskList Title for test",
                "TaskList Description for test",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
    }
    public static TaskList createSecondTaskList(){

        return new TaskList(
                UUID.randomUUID(),
                "Second TaskList Title for test",
                "Second TaskList Description for test",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
    }

    public static Task createTask() {
        //Task list needs to be assigned individually for every test;
        return new Task(
                null,
                "Title",
                "Description",
                LocalDateTime.of(2004, 1, 1 , 1 , 1),
                TaskStatus.OPEN,
                TaskPriority.LOW,
                LocalDateTime.of(2004, 1, 1 , 1 , 1),
                LocalDateTime.of(2004, 1, 1 , 1 , 1),
                null
        );
    }
    public static Task createTaskToUpdate() {
        //Task list needs to be assigned individually for every test;
        return new Task(
                null,
                "UpdatedTitle",
                "Updated Description",
                LocalDateTime.of(1234, 1, 1 , 1 , 1),
                TaskStatus.OPEN,
                TaskPriority.LOW,
                LocalDateTime.of(1233, 1, 1 , 1 , 1),
                LocalDateTime.of(2004, 1, 1 , 1 , 1),
                null
        );
    }










}
