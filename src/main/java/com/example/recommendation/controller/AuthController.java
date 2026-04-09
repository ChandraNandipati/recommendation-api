package com.example.recommendation.controller;


import com.example.recommendation.dao.UserDao;
import com.example.recommendation.dto.User;
import com.example.recommendation.model.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final UserDao userDao;

    @PostMapping(
            value="/register",
            consumes= MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String registerUser(@RequestBody User user){
        // later
        userDao.saveUser(user);
        return "User Saved Successfully";
    }

    @PostMapping(
            value="/login",
            consumes= MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public User loginUser(@RequestBody LoginRequest loginRequest){
        return userDao.loginUser(loginRequest.getEmail(),loginRequest.getPassword());
    }


    @GetMapping(
            value="/user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public User getUser(@RequestParam(name="id") String id){
        return userDao.getUserById(id);
    }
}
