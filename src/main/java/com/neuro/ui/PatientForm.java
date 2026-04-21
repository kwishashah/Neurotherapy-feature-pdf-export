package com.neuro.ui;

import com.neuro.dao.PatientDAO;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PatientForm extends JDialog {

    private Runnable onSaveCallback;

    private JTextField txtName, txtMobile, txtAge, txtDate;
    private JComboBox<String> cmbGender, cmbMarital;
    private JTextArea txtSymptoms;
    private JButton btnSave;

    public PatientForm(JFrame parent, Runnable onSaveCallback) {
        super(parent, true); // modal
        this.onSaveCallback = onSaveCallback;

        setTitle("Add Patient");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel lblTitle = new JLabel("Patient Registration");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);
        gbc.gridwidth = 1;

        // Name
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Patient Name:"), gbc);
        txtName = new JTextField();
        gbc.gridx = 1;
        panel.add(txtName, gbc);

        // Mobile
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Mobile:"), gbc);
        txtMobile = new JTextField();
        gbc.gridx = 1;
        panel.add(txtMobile, gbc);

        // Age
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Age:"), gbc);
        txtAge = new JTextField();
        gbc.gridx = 1;
        panel.add(txtAge, gbc);

        // Gender
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Gender:"), gbc);
        cmbGender = new JComboBox<>(new String[]{"-- Select --", "Male", "Female", "Other"});
        gbc.gridx = 1;
        panel.add(cmbGender, gbc);

        // Marital Status
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Marital Status:"), gbc);
        cmbMarital = new JComboBox<>(new String[]{"-- Select --", "Single", "Married", "Divorced", "Widowed"});
        gbc.gridx = 1;
        panel.add(cmbMarital, gbc);

        // Visit Date
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Visit Date (dd-MM-yyyy):"), gbc);
        txtDate = new JTextField(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        gbc.gridx = 1;
        panel.add(txtDate, gbc);

        // Symptoms
        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Symptoms:"), gbc);
        txtSymptoms = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(txtSymptoms);
        gbc.gridx = 1;
        panel.add(scrollPane, gbc);

        // Save button
        btnSave = new JButton("Save Patient");
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        panel.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            if (!isFormValid()) return;
            savePatient();
        });

        add(panel);
    }

    private boolean isFormValid() {
        // Name
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient name is required");
            txtName.requestFocus();
            return false;
        }

        // Mobile
        String mobile = txtMobile.getText().trim();
        if (!mobile.matches("[6-9][0-9]{9}")) {
            JOptionPane.showMessageDialog(this, "Enter valid 10-digit mobile number");
            txtMobile.requestFocus();
            return false;
        }

        // Age
        try {
            int age = Integer.parseInt(txtAge.getText());
            if (age < 1 || age > 120) {
                JOptionPane.showMessageDialog(this, "Age must be between 1 and 120");
                txtAge.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be numeric");
            txtAge.requestFocus();
            return false;
        }

        // Gender
        if (cmbGender.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select gender");
            return false;
        }

        // Marital
        if (cmbMarital.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select marital status");
            return false;
        }

        // Visit date
        String dateStr = txtDate.getText().trim();
        if (dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Visit date is required");
            txtDate.requestFocus();
            return false;
        }

        try {
            Date visitDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateStr);
            if (visitDate.after(new Date())) {
                JOptionPane.showMessageDialog(this, "Future dates are not allowed");
                txtDate.requestFocus();
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use dd-MM-yyyy");
            txtDate.requestFocus();
            return false;
        }

        // Symptoms
        if (txtSymptoms.getText().trim().length() < 3) {
            JOptionPane.showMessageDialog(this, "Please enter valid symptoms");
            txtSymptoms.requestFocus();
            return false;
        }

        return true;
    }

    private void savePatient() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date utilDate = sdf.parse(txtDate.getText().trim());
            java.sql.Timestamp timestamp = new java.sql.Timestamp(utilDate.getTime());

            PatientDAO.savePatient(
                    null, // patientId

                    txtName.getText(),
                    txtMobile.getText(),
                    Integer.parseInt(txtAge.getText()),
                    (String) cmbGender.getSelectedItem(),
                    (String) cmbMarital.getSelectedItem(),

                    "", // address
                    "", // occupation
                    "", // blood group

                    0f, // height
                    0f, // weight

                    "", // sufferingDuration
                    "", // mainDisease
                    "", // complications
                    txtSymptoms.getText(), // symptoms
                    "", // painPoints

                    "", "", "", "", "", "", // tongue → neurotherapy

                    "", // previousTreatment
                    "", // medicines
                    "", // detailedHistory
                    "", // examination

                    // ✅ NEW VITALS (VERY IMPORTANT)
                    "", // bp
                    "", // pulse
                    "", // o2
                    "", // temperature

                    "", // reports
                    "", // media (report analysis)
                    "", // allergy (patient_story)
                    "", // remarks

                    new java.sql.Timestamp(System.currentTimeMillis())
            );

            JOptionPane.showMessageDialog(this, "Patient saved successfully");
            if (onSaveCallback != null) onSaveCallback.run();
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving patient:\n" + e.getMessage());
            e.printStackTrace();
        }
    }


    private void clearForm() {
        txtName.setText("");
        txtMobile.setText("");
        txtAge.setText("");
        cmbGender.setSelectedIndex(0);
        cmbMarital.setSelectedIndex(0);
        txtSymptoms.setText("");
        txtDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
    }
}