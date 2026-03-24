package com.taskmanager.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "project")
public class Project {
    //TODO: Add field for last Updated on to track last modified time of project entity and set it to 'now' when updated.
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    private String description;

    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;
}


