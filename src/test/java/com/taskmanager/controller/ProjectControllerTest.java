package com.taskmanager.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCreateProject() throws Exception{
        String projectJson= """
                {
                "name" : "Test Project",
                "description" : "JUnit test project",
                "status" : "ACTIVE"
                }
                """;
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Project"));

    }

    @Test
    public void projectNotFound() throws Exception{
        mockMvc.perform(get("/api/projects/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetProjectById() throws Exception {
        // First create a project
        String projectJson = """
                {
                "name" : "Test Project for Get",
                "description" : "Testing get by id",
                "status" : "ACTIVE"
                }
                """;
        String response = mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Extract ID from response
        JsonNode jsonNode = objectMapper.readTree(response);
        long projectId = jsonNode.get("id").asLong();

        // Now get the project by extracted ID
        mockMvc.perform(get("/api/projects/" + projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Project for Get"));
    }

    @Test
    public void testGetAllProjects() throws Exception {
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testDeleteProject() throws Exception {
        // Create a project first
        String projectJson = """
                {
                "name" : "Test Project for Delete",
                "description" : "Testing delete",
                "status" : "ACTIVE"
                }
                """;
        String response = mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Extract ID from response
        JsonNode jsonNode = objectMapper.readTree(response);
        long projectId = jsonNode.get("id").asLong();

        // Delete it
        mockMvc.perform(delete("/api/projects/" + projectId))
                .andExpect(status().isNoContent());

        // Verify it's gone
        mockMvc.perform(get("/api/projects/" + projectId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testBadRequest() throws Exception {
        String invalidJson = """
                {
                "name" : "",
                "description" : "Invalid project",
                "status" : "ACTIVE"
                }
                """;
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
