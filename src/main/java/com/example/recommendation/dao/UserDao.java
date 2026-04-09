package com.example.recommendation.dao;


import com.example.recommendation.configuration.DBConfig;
import com.example.recommendation.dto.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDao{

    private final DBConfig dbConfig;

    // INSERT USER
    public void saveUser(User user) {
        String sql = "INSERT INTO auth.users (first_name, last_name, email, password) VALUES ('"
                + user.getFirstName() + "', '"
                + user.getLastName() + "', '"
                + user.getEmail() + "', '"
                + user.getPassword() + "')";

        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("User inserted successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public User loginUser(String email, String password) {

        if (email == null || password == null) {
            log.warn("Email or password is null");
            return null;
        }

        String sql = "SELECT * FROM auth.users WHERE trim(email) = ? AND trim(password) = ?";

        log.info("Executing login query for email: {}", email.trim());
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.trim());
            stmt.setString(2, password.trim());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));

                System.out.println("Login successful");
                return user;
            } else {
                System.out.println("Invalid email or password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public User getUserById(String id) {

        String sql = "SELECT * FROM auth.users WHERE id = '" + id+"'";

        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
