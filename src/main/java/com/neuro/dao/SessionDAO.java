package com.neuro.dao;

import com.neuro.db.DBConnection;

import java.sql.*;
import java.util.Vector;

public class SessionDAO {

    // 🔹 Get all sessions of a patient
    public static Vector<Vector<Object>> getSessionsByPatient(int patientId) throws Exception {

        Vector<Vector<Object>> data = new Vector<>();

        String sql = """
            SELECT session_id, session_number, session_date,
                   treatment_given, pain_before, pain_after, session_summary
            FROM NeurotherapySessions
            WHERE patient_id = ?
            ORDER BY session_number
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();

                row.add(rs.getInt("session_id"));
                row.add(rs.getInt("session_number"));
                row.add(rs.getDate("session_date"));
                row.add(rs.getString("treatment_given"));
                row.add(rs.getString("pain_before"));
                row.add(rs.getString("pain_after"));
                row.add(rs.getString("session_summary"));

                data.add(row);
            }
        }

        return data;
    }

    // 🔹 Get next session number (auto increment per patient)
    public static int getNextSessionNumber(int patientId) {

        int next = 1;

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT COUNT(*) FROM NeurotherapySessions WHERE patient_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, patientId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                next = rs.getInt(1) + 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return next;
    }

    // 🔹 Add new session
    public static void addSession(
            int patientId,
            int sessionNo,
            Date date,
            String treatment,
            String painBefore,
            String painAfter,
            String summary) throws Exception {

        String sql = """
            INSERT INTO NeurotherapySessions
            (patient_id, session_number, session_date,
             treatment_given, pain_before, pain_after, session_summary)
            VALUES (?,?,?,?,?,?,?)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ps.setInt(2, sessionNo);
            ps.setDate(3, date);
            ps.setString(4, treatment);
            ps.setString(5, painBefore);
            ps.setString(6, painAfter);
            ps.setString(7, summary);

            ps.executeUpdate();
        }
    }

    // 🔹 Update existing session
    public static void updateSession(
            int sessionId,
            int sessionNo,
            Date date,
            String treatment,
            String painBefore,
            String painAfter,
            String summary) throws Exception {

        String sql = """
            UPDATE NeurotherapySessions
            SET session_number=?, session_date=?,
                treatment_given=?, pain_before=?, pain_after=?, session_summary=?
            WHERE session_id=?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sessionNo);
            ps.setDate(2, date);
            ps.setString(3, treatment);
            ps.setString(4, painBefore);
            ps.setString(5, painAfter);
            ps.setString(6, summary);
            ps.setInt(7, sessionId);

            ps.executeUpdate();
        }
    }
}