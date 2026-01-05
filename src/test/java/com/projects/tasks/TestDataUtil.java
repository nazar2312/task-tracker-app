package com.projects.tasks;

import com.projects.tasks.domain.entities.TaskList;

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








}
