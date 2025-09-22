package com.jobportal.job_portal.service.impl;

import com.jobportal.job_portal.dto.UserDTO;
import com.jobportal.job_portal.entity.User;
import com.jobportal.job_portal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById() {
        User user = User.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .role("APPLICANT")
                .password("secret")
                .username("john123")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById(1L);

        assertEquals("John Doe", result.getFullName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getUserById(99L));
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void testCreateUser() {
        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setFullName("John Doe");
        dto.setEmail("john@example.com");
        dto.setPhone("1234567890");
        dto.setRole("APPLICANT");

        User user = User.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .role("APPLICANT")
                .build();

        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.createUser(dto);

        assertEquals("John Doe", result.getFullName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserEmailAlreadyExists() {
        UserDTO dto = new UserDTO();
        dto.setEmail("john@example.com");

        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.createUser(dto));
        assertEquals("Email already exists", ex.getMessage());
    }

    @Test
    void testGetAllUsers() {
        User user1 = User.builder().id(1L).fullName("John").email("john@example.com").phone("111").role("APPLICANT").build();
        User user2 = User.builder().id(2L).fullName("Jane").email("jane@example.com").phone("222").role("ADMIN").build();

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserDTO> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("John", users.get(0).getFullName());
        assertEquals("Jane", users.get(1).getFullName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testUpdateUser() {
        User existingUser = User.builder()
                .id(1L)
                .fullName("Old Name")
                .email("old@example.com")
                .phone("111")
                .role("APPLICANT")
                .build();

        UserDTO updateDto = new UserDTO();
        updateDto.setFullName("New Name");
        updateDto.setPhone("222");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserDTO result = userService.updateUser(1L, updateDto);

        assertEquals("New Name", result.getFullName());
        assertEquals("222", result.getPhone());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserNotFound() {
        UserDTO updateDto = new UserDTO();
        updateDto.setFullName("New Name");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.updateUser(1L, updateDto));
        assertEquals("User not found ", ex.getMessage());
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
