package com.example.recommendation.dao;


import com.example.recommendation.configuration.DBConfig;
import com.example.recommendation.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
@RequiredArgsConstructor
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

        String sql = "SELECT * FROM auth.users WHERE email = '"
                + email + "' AND password = '" + password + "'";

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
