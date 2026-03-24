package com.taskmanager.dto;

import lombok.Data;
import com.taskmanager.model.ProjectStatus;

import java.time.LocalDateTime;


@Data
public class ProjectResponseDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdDate;
    private ProjectStatus status;
}
