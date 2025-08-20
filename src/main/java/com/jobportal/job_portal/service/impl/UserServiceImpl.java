package com.jobportal.job_portal.service.impl;

import com.jobportal.job_portal.dto.UserDTO;
import com.jobportal.job_portal.entity.User;
import com.jobportal.job_portal.repository.UserRepository;
import com.jobportal.job_portal.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    private UserDTO mapToDTO(User user) {

        UserDTO dto =new UserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());

        return dto;
    }

    private User mapToEntity(UserDTO dto){
        return User.builder()
                .id(dto.getId())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .role(dto.getRole())
                .build();
    }

    @Override
    public UserDTO createUser(UserDTO userDTO){
        if (userRepository.existsByEmail(userDTO.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        User user = mapToEntity(userDTO);
        return mapToDTO(userRepository.save(user));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

    }

    @Override
    public UserDTO getUserById(Long id){
        User user =userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDTO(user);
    }

    public UserDTO updateUser(Long id , UserDTO userDTO){
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found "));
         existingUser.setFullName(userDTO.getFullName());
         existingUser.setPhone(userDTO.getPhone());
         return mapToDTO(userRepository.save(existingUser));
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
}
