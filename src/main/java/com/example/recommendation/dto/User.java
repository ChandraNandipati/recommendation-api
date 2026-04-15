package com.example.recommendation.dto;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users", schema = "auth")
@Schema(description = "User entity representing application user")
public class User {

    @Id
    @Column(name = "id")
    @Schema(description = "Unique user ID", example = "123")
    private String id;

    @Column(name = "first_name")
    @Schema(description = "User first name", example = "Sneha")
    private String firstName;

    @Column(name = "last_name")
    @Schema(description = "User last name", example = "Kasina")
    private String lastName;

    @Column(name = "email")
    @Schema(description = "User email address", example = "sneha@gmail.com")
    private String email;

    @Column(name = "password")
    @Schema(description = "User password", example = "password123")
    private String password;
}