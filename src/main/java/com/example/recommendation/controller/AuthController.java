package com.example.recommendation.controller;

import com.example.recommendation.dto.User;
import com.example.recommendation.model.LoginRequest;
import com.example.recommendation.repository.UserRepository;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Auth API", description = "Authentication APIs for User")
public class AuthController {

    private final UserRepository userRepository;

    @Operation(summary = "Register new user", description = "Creates a new user in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping(
            value="/register",
            consumes= MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String registerUser(
            @RequestBody User user){
        userRepository.save(user);
        return "User Saved Successfully";
    }

    @Operation(summary = "Login user", description = "Authenticate user using email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping(
            value="/login",
            consumes= MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public User loginUser(
            @RequestBody LoginRequest loginRequest){

        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            return null;
        }

        return userRepository
                .findByEmailAndPassword(
                        loginRequest.getEmail().trim(),
                        loginRequest.getPassword().trim()
                )
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Operation(summary = "Get user by ID", description = "Fetch user details using user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping(
            value="/user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public User getUser(@RequestParam(name="id") String id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}