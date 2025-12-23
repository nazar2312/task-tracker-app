package com.projects.tasks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projects.tasks.domain.entities.Task;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByTaskListId(UUID taskListId);

    Optional<Task> findTaskByIdAndTaskListId(UUID id, UUID taskListId);

    Optional<Task> deleteTaskByIdAndTaskListId(UUID id, UUID taskListId);

}
