package com.projects.tasks.mappers.impl;

import com.projects.tasks.domain.dto.TaskListDto;
import com.projects.tasks.domain.entities.Task;
import com.projects.tasks.domain.entities.TaskList;
import com.projects.tasks.domain.entities.TaskStatus;
import com.projects.tasks.mappers.TaskListMapper;
import com.projects.tasks.mappers.TaskMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskListMapperImpl implements TaskListMapper {

    /*
        Inject dependency through constructor;
     */
    private final TaskMapper taskMapper;
    public TaskListMapperImpl(TaskMapper taskMapper){
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskList fromDto(TaskListDto taskListDto) {
        return new TaskList(
                taskListDto.id(),
                taskListDto.title(),
                taskListDto.description(),
                null,
                null,
                /*
                    Making sure that TaskListDto is not null, then convert every taskDto
                    to Task by using taskMapper and put back in the List
                 */
                Optional.ofNullable(taskListDto.tasks())
                        .map(tasks -> tasks.stream()
                                .map(taskMapper::fromDto)
                                .toList()
                        ).orElse(null)
        );
    }

    @Override
    public TaskListDto toDto(TaskList taskList) {
        return new TaskListDto(
                taskList.getId(),
                taskList.getTitle(),
                taskList.getDescription(),
                /*
                    Make sure that taskList is not null, then check it's size;
                 */
                Optional.ofNullable(taskList.getTasks())
                        .map(List::size)
                        .orElse(0),
                calculateProgress(taskList.getTasks()),
                Optional.ofNullable(taskList.getTasks())
                        .map(tasks -> tasks.stream()
                                .map(taskMapper::toDto)
                                .toList()
                        ).orElse(null)
        );
    }

    /*
       Finding closed tasks number and divide it by task list size to find the progress;
     */
    public Double calculateProgress(List<Task> tasks){

        if(tasks == null){
            return null;
        }
        long closedTasks = tasks.stream().filter(
                task -> TaskStatus.CLOSED == task.getStatus()
        ).count();

        return (double) closedTasks / tasks.size();
    }
}
