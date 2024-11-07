package com.phegondev.usersmanagementsystem.service;

import com.phegondev.usersmanagementsystem.dto.RegistrationRequestDTO;
import com.phegondev.usersmanagementsystem.dto.UserResponseDTO;
import com.phegondev.usersmanagementsystem.entity.User;
import com.phegondev.usersmanagementsystem.errorHandlers.CustomException;
import com.phegondev.usersmanagementsystem.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersManagementService {

    @Autowired
    private UsersRepository usersRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserResponseDTO register(RegistrationRequestDTO userRequestDTO){

        try {
            User user = new User();
            user.setUsername(userRequestDTO.getUsername());
            user.setEmail(userRequestDTO.getEmail());
            user.setRole(userRequestDTO.getRole());
            user.setName(userRequestDTO.getName());
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
            return UserResponseDTO.from(usersRepo.save(user));
        } catch (Exception e){
            throw new CustomException(e.getMessage());
        }
        }

    public UserResponseDTO login(RegistrationRequestDTO loginRequest){
        UserResponseDTO response = new UserResponseDTO();
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));
            var user = usersRepo.findByUsername(loginRequest.getUsername()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");

        return response;
    }





    public UserResponseDTO refreshToken(RegistrationRequestDTO refreshTokenReqiest){
        UserResponseDTO response = new UserResponseDTO();
        try{
            String username = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
            User user = usersRepo.findByUsername(username).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), user)) {
                var jwt = jwtUtils.generateToken(user);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenReqiest.getToken());
                response.setExpirationTime("24Hr");
            }
            return response;

        }catch (Exception e){
            throw new CustomException("Error when creating refresh token");
        }
    }


    public List<UserResponseDTO> getAllUsers() {

        try {
            List<User> usersResult = usersRepo.findAll();
            return usersResult.stream().map(UserResponseDTO::from).collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }


    public UserResponseDTO getUserById(Integer id) {
        try {
            User userById = usersRepo.findById(id).orElseThrow(() -> new CustomException("User Not found"));
            return UserResponseDTO.from(userById);
        } catch (Exception e) {
            throw new CustomException("User not found");
        }
    }


    public void deleteUser(Integer userId) {
        try {
                usersRepo.deleteById(userId);
            } catch (Exception e){
                throw new CustomException(e.getMessage());
            }
        }

    public UserResponseDTO updateUser(Integer userId, User updatedUser) {
        try {
            User existingUser = usersRepo.findById(userId).orElseThrow(() -> new CustomException("User Not found"));
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setName(updatedUser.getName());
                existingUser.setRole(updatedUser.getRole());
                return UserResponseDTO.from(usersRepo.save(existingUser));

        } catch (Exception e) {
            throw new CustomException(e.getMessage());
            }
    }


    public UserResponseDTO getMyInfo(String username){
        try {
            User user = usersRepo.findByUsername(username).orElseThrow(() -> new CustomException("User Not found"));
            return UserResponseDTO.from(user);
        }catch (Exception e){
            throw new CustomException(e.getMessage());
        }
    }
}