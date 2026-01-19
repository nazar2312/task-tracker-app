package com.projects.tasks.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.tasks.TestDataUtil;
import com.projects.tasks.domain.dto.TaskDto;
import com.projects.tasks.domain.entities.Task;
import com.projects.tasks.domain.entities.TaskList;
import com.projects.tasks.repositories.TaskListRepository;
import com.projects.tasks.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskIntegrationTests {

    private Task task;
    private Task toUpdate;
    private TaskList taskList;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        this.task = TestDataUtil.createTask();

        this.taskList = taskListRepository.save(TestDataUtil.createTaskList());
        this.task.setTaskList(taskList);

        this.toUpdate = TestDataUtil.createTaskToUpdate();

    }

    @Test
    void createTask_shouldReturnStatusCreated_whenTaskCreated() throws Exception {

        mockMvc.perform(post("/task-lists/" + taskList.getId() + "/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task))
        ).andExpect(status().isCreated());

        Task savedTask = taskRepository.findByTaskListId(taskList.getId()).get(0);

        assertEquals(savedTask.getTitle(), task.getTitle());
        assertNotNull(savedTask.getId());

    }

    @Test
    void createTask_shouldReturnStatusNotFound_whenTaskListIsNotFound() throws Exception {

        UUID nonExisting = UUID.randomUUID();

        mockMvc.perform(post("/task-lists/" + nonExisting + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task))
                ).andExpect(status().isNotFound());

        //Assert that task wasn't saved and id wasn't assigned;
        assertNull(task.getId());
    }

    @Test
    void getTaskById_shouldReturnTask_whenAllCriteriaAreMet() throws Exception {

        taskRepository.save(task);

        mockMvc.perform(get("/task-lists/" + taskList.getId() + "/tasks/" + task.getId())
        ).andExpect(status().isOk());
    }

    @Test
    void getTaskById_shouldReturnStatusNotFound_whenTaskIsNotFound() throws Exception {

        UUID nonExisting = UUID.randomUUID();

        mockMvc.perform(get("/task-lists/" + nonExisting + "/tasks/" + nonExisting)
        ).andExpect(status().isNotFound());

    }

    @Test
    void updateTask_shouldReturnStatusOk_whenTaskSuccessfullyUpdated() throws Exception {

        taskRepository.save(task);

        mockMvc.perform(patch("/task-lists/" + taskList.getId() + "/tasks/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toUpdate))
        ).andExpect(status().isOk());

        Task updated = taskRepository.findById(task.getId()).get();

        assertEquals(updated.getId(), task.getId());
        assertEquals(updated.getTitle(), toUpdate.getTitle());
    }

    @Test
    void updateTask_shouldReturnStatusNotFound_whenTaskIsNotFound() throws Exception {

        UUID nonExisting = UUID.randomUUID();

        mockMvc.perform(patch("/task-lists/" + nonExisting + "/tasks/" + nonExisting)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toUpdate))
        ).andExpect(status().isNotFound());

    }

    @Test
    void updateTask_shouldReturnStatusBadRequest_whenAttemptingToChangeId() throws Exception {

        toUpdate.setId(UUID.randomUUID());
        taskRepository.save(task);

        mockMvc.perform(patch("/task-lists/" + taskList.getId() + "/tasks/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toUpdate))
        ).andExpect(status().isBadRequest());

    }

    @Test
    void deleteTask_shouldReturnStatusOk_whenTaskDeleted() throws Exception {

        taskRepository.save(task);

        mockMvc.perform(delete("/task-lists/" + taskList.getId() + "/tasks/" + task.getId()))
                        .andExpect(status().isNoContent());


        assertEquals(Optional.empty(), taskRepository.findByTaskListIdAndId(taskList.getId(),task.getId()));
    }

    @Test
    void deleteTask_shouldReturnStatusNotFound_whenTaskIsNotFound() throws Exception {

        UUID nonExisting = UUID.randomUUID();

        mockMvc.perform(delete("/task-lists/" + nonExisting + "/tasks/" + nonExisting))
                .andExpect(status().isNotFound());

    }













}
