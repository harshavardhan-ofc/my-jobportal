package com.jobportal.job_portal.controller;

import com.jobportal.job_portal.dto.UserDTO;
import com.jobportal.job_portal.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final IUserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto){
        return ResponseEntity.ok(userService.createUser(dto));
    }


    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @PutMapping("/id")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,@RequestBody UserDTO dto){
        return ResponseEntity.ok(userService.updateUser(id,dto));

    }

    @PreAuthorize("hasRole('RECRUITER')")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
       userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }



}
