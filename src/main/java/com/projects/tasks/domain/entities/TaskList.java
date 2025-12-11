package com.projects.tasks.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

//Annotate class as entity and specify table name
@Entity
@Table(name = "task_lists")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskList {

    //Annotate variable as id column and assign auto-generation for UUID
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    //Create relation and map for taskList in Task.
    @OneToMany(mappedBy = "taskList", cascade = {
            CascadeType.REMOVE, CascadeType.PERSIST
    })
    private List<Task> tasks;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "created", nullable = false, updatable = false)
    private LocalDateTime created;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;




}
