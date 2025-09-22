package com.jobportal.job_portal.service;

import com.jobportal.job_portal.dto.UserDTO;

import java.util.List;

public interface IUserService {

    UserDTO createUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id );
    UserDTO updateUser(Long id , UserDTO userDTO);
    void deleteUser(Long id);


}
