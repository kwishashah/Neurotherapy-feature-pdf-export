package com.neuro.dao;

import com.neuro.db.DBConnection;
import com.neuro.util.PasswordUtil;

import java.sql.*;

public class UserDAO {

    // ================= LOGIN VALIDATION =================
    public static boolean validateUser(String username, String password) throws Exception {

        String sql = "SELECT password FROM users WHERE TRIM(username)=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username.trim());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    return PasswordUtil.verify(password, storedHash);
                }
            }
        }

        return false;
    }

    // ================= CHECK IF USERS EXIST =================
    public static boolean hasAnyUser() {

        String sql = "SELECT COUNT(*) FROM users";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() && rs.getInt(1) > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= CREATE USER =================
    public static boolean insertUser(String username, String password) throws Exception {

        String hashedPassword = PasswordUtil.hash(password);

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username.trim());
            stmt.setString(2, hashedPassword);

            return stmt.executeUpdate() > 0;
        }
    }

    // ================= CHECK USER EXISTS =================
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

    // ================= ENCRYPT OLD PASSWORDS =================
    public static void encryptExistingPasswords() throws Exception {

        String select = "SELECT username, password FROM users";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(select);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String username = rs.getString("username");
                String plainPassword = rs.getString("password");

                if (plainPassword != null && plainPassword.length() > 20) {
                    continue; // already hashed
                }

                String hashed = PasswordUtil.hash(plainPassword);

                String update = "UPDATE users SET password=? WHERE username=?";

                try (PreparedStatement up = conn.prepareStatement(update)) {
                    up.setString(1, hashed);
                    up.setString(2, username);
                    up.executeUpdate();
                }
            }
        }

        System.out.println("✅ All passwords encrypted");
    }

    // ================= GET USER ID (VERY IMPORTANT) =================
    public static int getUserId(String username) throws Exception {

        String sql = "SELECT user_id FROM users WHERE TRIM(username)=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        }

        return -1;
    }
}