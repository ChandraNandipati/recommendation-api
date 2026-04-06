package com.example.recommendation.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
//    private long id;
    private String id; // after UUID it is converted to string
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
