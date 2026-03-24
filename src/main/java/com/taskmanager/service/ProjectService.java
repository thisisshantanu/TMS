package com.taskmanager.service;

import com.taskmanager.dto.ProjectRequestDto;
import com.taskmanager.dto.ProjectResponseDto;
import com.taskmanager.exception.ProjectNotFoundException;
import com.taskmanager.model.Project;
import org.springframework.stereotype.Service;
import com.taskmanager.repository.IProjectRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final IProjectRepository projectRepository;

    public ProjectService(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectResponseDto createProject(ProjectRequestDto request){
        Project project = mapToEntity(request);
        project = projectRepository.save(project);

        return mapToResponse(project);
    }

    public ProjectResponseDto getProjectById(Long id){
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
        return mapToResponse(project);
    }

    public List<ProjectResponseDto> getAllProjects(){
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ProjectResponseDto updateProject(Long id, ProjectRequestDto request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
        updateProjectFromDto(project, request);
        project = projectRepository.save(project);
        return mapToResponse(project);
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    private ProjectResponseDto mapToResponse(Project project) {
        ProjectResponseDto responseDto = new ProjectResponseDto();
        responseDto.setId(project.getId());
        responseDto.setName(project.getName());
        responseDto.setDescription(project.getDescription());
        responseDto.setCreatedDate(project.getCreatedDate());
        responseDto.setStatus(project.getStatus());
        return responseDto;
    }

    private Project mapToEntity(ProjectRequestDto request) {
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus());
        project.setCreatedDate(LocalDateTime.now());
        return project;
    }

    private void updateProjectFromDto(Project project, ProjectRequestDto request) {
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus());
    }
}
