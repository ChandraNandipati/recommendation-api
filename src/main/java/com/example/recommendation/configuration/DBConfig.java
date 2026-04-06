package com.example.recommendation.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DBConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${test.sneha.value}")
    private String testSnehaValue;
    // dev swatidev
    // qa swatiqa

    public Connection getConnection() throws SQLException {
        System.out.println("USER : "+user);
        System.out.println("URL : "+url);
        System.out.println("PASSWORD : "+password);
        System.out.println("testSnehaValue : "+testSnehaValue);
        return DriverManager.getConnection(url, user, password);
    }
}
