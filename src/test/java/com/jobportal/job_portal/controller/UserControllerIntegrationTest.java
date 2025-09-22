package com.jobportal.job_portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.job_portal.dto.UserDTO;
import com.jobportal.job_portal.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO createUserDTO(Long id, String email) {
        UserDTO user = new UserDTO();
        user.setId(id);
        user.setEmail(email);
        user.setFullName("Test User");
        return user;
    }

    @Test
    @WithMockUser(username = "recruiter@example.com", roles = {"RECRUITER"})
    void testCreateUser() throws Exception {
        UserDTO user = createUserDTO(1L, "user@example.com");
        when(userService.createUser(any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));

        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    @WithMockUser(username = "recruiter@example.com", roles = {"RECRUITER"})
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(createUserDTO(1L, "user@example.com")));

        mockMvc.perform(get("/api/users/getAllUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("user@example.com"));
    }

    @Test
    @WithMockUser(username = "recruiter@example.com", roles = {"RECRUITER"})
    void testGetUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(createUserDTO(1L, "user@example.com"));

        mockMvc.perform(get("/api/users/getUserById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    @WithMockUser(username = "recruiter@example.com", roles = {"RECRUITER"})
    void testUpdateUser() throws Exception {
        UserDTO updatedUser = createUserDTO(1L, "updated@example.com");
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1") //
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    @WithMockUser(username = "recruiter@example.com", roles = {"RECRUITER"})
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/deleteUser/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }
}
