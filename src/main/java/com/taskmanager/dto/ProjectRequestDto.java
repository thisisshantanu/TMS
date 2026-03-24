package com.taskmanager.dto;

import com.taskmanager.model.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class ProjectRequestDto {
    @NotBlank(message = "Project nae is mandatory")
    @Size(min=3, max=100, message = "Name should be min. 3 and max. 100 characters")
    private String name;

    private String description;
    private ProjectStatus status= ProjectStatus.ACTIVE;
}
