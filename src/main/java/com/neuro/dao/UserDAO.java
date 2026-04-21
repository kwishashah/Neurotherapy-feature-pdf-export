
package com.neuro.dao;

import com.neuro.db.DBConnection;
import java.sql.*;

public class UserDAO {


    public static boolean validateUser(String username, String password) throws Exception {

        String sql = "SELECT 1 FROM users WHERE TRIM(username)=? AND password=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.trim());
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    public static boolean hasAnyUser() {
        String sql = "SELECT COUNT(*) FROM Users";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean insertUser(String username, String password) {

        try {
            // ✅ Step 1: check first
            if (userExists(username)) {
                return false;
            }

            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, username.trim());
                ps.setString(2, password);

                ps.executeUpdate();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean userExists(String username) throws Exception {

        String sql = "SELECT 1 FROM users WHERE TRIM(username)=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.trim());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}