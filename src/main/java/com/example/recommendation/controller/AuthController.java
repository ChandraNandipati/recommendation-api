package com.example.recommendation.controller;

import com.example.recommendation.dto.User;
import com.example.recommendation.model.LoginRequest;
import com.example.recommendation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping(
            value="/register",
            consumes= MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String registerUser(@RequestBody User user){
        userRepository.save(user);
        return "User Saved Successfully";
    }

    @PostMapping(
            value="/login",
            consumes= MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public User loginUser(@RequestBody LoginRequest loginRequest){
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


    @GetMapping(
            value="/user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public User getUser(@RequestParam(name="id") String id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
