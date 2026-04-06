package com.example.recommendation.controller;

import com.example.recommendation.configuration.DBConfig;
import com.example.recommendation.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;

@RestController
@RequestMapping("api/v1/health")
@RequiredArgsConstructor
public class HelloWorldController {

    private final DBConfig dbConfig;

    @GetMapping(
            value="/check",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String getHealthCheck() throws SQLException {
        Connection conn = dbConfig.getConnection();
        return null;
    }
}
