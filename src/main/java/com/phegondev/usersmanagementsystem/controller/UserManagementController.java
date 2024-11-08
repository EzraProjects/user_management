package com.phegondev.usersmanagementsystem.controller;

import com.phegondev.usersmanagementsystem.dto.LoginDTO;
import com.phegondev.usersmanagementsystem.dto.RegistrationRequestDTO;
import com.phegondev.usersmanagementsystem.dto.UserResponseDTO;
import com.phegondev.usersmanagementsystem.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserManagementController {
    @Autowired
    private UsersManagementService usersManagementService;

    @PostMapping("/auth/register")
    public ResponseEntity<UserResponseDTO> regeister(@Validated @RequestBody RegistrationRequestDTO userRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(usersManagementService.register(userRequestDTO));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginDTO req){
        return ResponseEntity.ok(usersManagementService.login(req));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<UserResponseDTO> refreshToken(@RequestBody RegistrationRequestDTO req){
        return ResponseEntity.ok(usersManagementService.refreshToken(req));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        return ResponseEntity.ok(usersManagementService.getAllUsers());

    }

    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<UserResponseDTO> getUSerByID(@PathVariable Integer userId){
        return ResponseEntity.ok(usersManagementService.getUserById(userId));

    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Integer userId, @RequestBody RegistrationRequestDTO req){
        return ResponseEntity.ok(usersManagementService.updateUser(userId, req.toUser()));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<UserResponseDTO> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserResponseDTO response = usersManagementService.getMyInfo(username);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<Integer> deleteUSer(@PathVariable Integer userId){
        usersManagementService.deleteUser(userId);
        return ResponseEntity.ok(userId);
    }


}
