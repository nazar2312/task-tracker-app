package com.projects.tasks.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.tasks.TestDataUtil;
import com.projects.tasks.domain.entities.TaskList;
import com.projects.tasks.repositories.TaskListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskListIntegrationTests {

    private TaskList taskList;
    private TaskList toUpdate;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        taskListRepository.deleteAll();

        this.taskList = TestDataUtil.createTaskList();
        this.toUpdate = TestDataUtil.createSecondTaskList();
    }

    @Test
    void createTaskList_shouldReturnHttpStatus201_WhenSuccessfullyCreated() throws Exception {

        mockMvc.perform(post("/task-lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskList)))
                .andExpect(status().isCreated());

        assertEquals(1, taskListRepository.count());
        assertNotNull(taskListRepository.findAll().get(0).getId());

    }

    @Test
    void createTaskList_shouldReturnStatus400_WhenIdIsPresent() throws Exception {

        taskList.setId(UUID.randomUUID());

        mockMvc.perform(post("/task-lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskList)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTaskList_shouldReturnStatus400_WhenTitleIsNotPresent() throws Exception {

        taskList.setTitle(null);

        mockMvc.perform(post("/task-lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskList)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void getTaskList_shouldReturnHttpStatusOk_whenTaskListIsFound() throws Exception {

        TaskList saved = taskListRepository.save(taskList);

        mockMvc.perform(get("/task-lists/" + saved.getId() ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId().toString()));

    }

    @Test
    void getTaskList_shouldReturnStatusNotFound_whenTaskListIsNotPresent() throws Exception {

        mockMvc.perform(get("/task-lists/" + UUID.randomUUID() ))
                .andExpect(status().isNotFound());

    }

    @Test
    void fullUpdateTaskList_shouldReturnStatusOk_whenUpdated() throws Exception {

        taskListRepository.save(taskList);

        mockMvc.perform(put("/task-lists/" + taskList.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toUpdate))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskList.getId().toString()))
                .andExpect(jsonPath("$.title").value(toUpdate.getTitle()));

        TaskList updatedTaskList = taskListRepository.findById(taskList.getId()).get();

        assertEquals(updatedTaskList.getTitle(), toUpdate.getTitle());
        assertEquals(updatedTaskList.getDescription(), toUpdate.getDescription());

    }

    @Test
    void fullUpdateTaskList_shouldReturnStatusBadRequest_whenAttemptingToChangeId() throws Exception {

        taskListRepository.save(taskList);
        toUpdate.setId(UUID.randomUUID());

        mockMvc.perform(put("/task-lists/" + taskList.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toUpdate))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void fullUpdateTaskList_shouldReturnStatusNotFound_whenTaskListIsNotFound() throws Exception {

        UUID unexistingId = UUID.randomUUID();

        mockMvc.perform(put("/task-lists/" + unexistingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toUpdate))
        ).andExpect(status().isNotFound());
    }

    @Test
    void deleteTaskList_shouldReturnStatusOk_whenDeleted() throws Exception {

        taskListRepository.save(taskList);

        mockMvc.perform(delete("/task-lists/" + taskList.getId()))
                .andExpect(status().isNoContent());

        assertTrue(taskListRepository.findById(taskList.getId()).isEmpty());
    }

    @Test
    void deleteTaskList_shouldReturnStatusNotFound_whenTaskListIsNotFound() throws Exception {

        UUID unexistingId = UUID.randomUUID();

        mockMvc.perform(delete("/task-lists/" + unexistingId))
                .andExpect(status().isNotFound());
    }







}
