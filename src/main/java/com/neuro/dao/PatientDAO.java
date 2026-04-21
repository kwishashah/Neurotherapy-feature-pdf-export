package com.neuro.dao;

import com.neuro.db.DBConnection;
import com.neuro.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // ================= SAVE PATIENT =================
    public static void savePatient(
            Integer patientId,
            String name,
            String mobile,
            Integer age,
            String gender,
            String maritalStatus,
            String address,
            String occupation,
            String bloodGroup,
            Float height,
            Float weight,
            String sufferingDuration,
            String mainDisease,
            String complications,
            String symptoms,
            String painPoints,
            String tongue,
            String stool,
            String urine,
            String nails,
            String navel,
            String neurotherapyRequired,
            String previousTreatment,
            String medicines,
            String detailedHistory,
            String examination,
            String bp,
            String pulse,
            String o2,
            String temperature,
            String reports,
            String media,
            String patientStory,
            String remarks,
            Timestamp createdAt
    ) throws SQLException {

        String sql = "INSERT INTO PatientHistory ("
                + "patient_id, patient_name, mobile_number, age, gender, marital_status, address, occupation, "
                + "blood_group, height, weight, suffering_duration, main_disease, complications, symptoms, "
                + "pain_points, tongue, stool, urine, nails, navel, neurotherapy_required, previous_treatment, "
                + "medicines, detailed_history, examination, bp,pulse,o2,temperature, reports, media, patient_story, remarks, created_at"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, patientId);
            ps.setString(2, name);
            ps.setString(3, mobile);
            ps.setObject(4, age, Types.INTEGER);
            ps.setString(5, gender);
            ps.setString(6, maritalStatus);
            ps.setString(7, address);
            ps.setString(8, occupation);
            ps.setString(9, bloodGroup);
            ps.setObject(10, height, Types.FLOAT);
            ps.setObject(11, weight, Types.FLOAT);
            ps.setString(12, sufferingDuration);
            ps.setString(13, mainDisease);
            ps.setString(14, complications);
            ps.setString(15, symptoms);
            ps.setString(16, painPoints);
            ps.setString(17, tongue);
            ps.setString(18, stool);
            ps.setString(19, urine);
            ps.setString(20, nails);
            ps.setString(21, navel);
            ps.setString(22, neurotherapyRequired);
            ps.setString(23, previousTreatment);
            ps.setString(24, medicines);
            ps.setString(25, detailedHistory);
            ps.setString(26, examination);
            ps.setString(27, bp);
            ps.setString(28, pulse);
            ps.setString(29, o2);
            ps.setString(30, temperature);

            ps.setString(31, reports);
            ps.setString(32, media);
            ps.setString(33, patientStory);
            ps.setString(34, remarks);
            ps.setObject(35, createdAt, Types.TIMESTAMP);

            ps.executeUpdate();
        }
    }

    // ================= GET ALL PATIENTS =================
    public static List<Object[]> getAllPatients() throws SQLException {

        List<Object[]> list = new ArrayList<>();

        String sql = "SELECT patient_id, patient_name, mobile_number, age, gender, neurotherapy_required FROM PatientHistory";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("patient_id"),
                        rs.getString("patient_name"),
                        rs.getString("mobile_number"),
                        rs.getInt("age"),
                        rs.getString("gender")
                });
            }
        }

        return list;
    }

    // ================= SEARCH PATIENT =================
    public static List<Object[]> searchPatientsByMobile(String mobile) throws SQLException {

        List<Object[]> list = new ArrayList<>();

        String sql = "SELECT patient_id, patient_name, mobile_number, age, gender, neurotherapy_required "
                + "FROM PatientHistory WHERE mobile_number LIKE ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + mobile + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                            rs.getInt("patient_id"),
                            rs.getString("patient_name"),
                            rs.getString("mobile_number"),
                            rs.getInt("age"),
                            rs.getString("gender")
                    });
                }
            }
        }

        return list;
    }
}