package com.neuro.ui;

import com.neuro.dao.PatientDAO;
import com.neuro.dao.UserDAO;
import com.neuro.model.Patient;
import com.neuro.model.ClinicInfo;
import com.neuro.config.ClinicConfig;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DoctorDashboard extends JFrame {

    private JTextField txtSearchMobile;
    private JTable tblPatients;
    private DefaultTableModel tableModel;
    JLabel lblLogo = new JLabel();
    JLabel lblTitle = new JLabel();
    public DoctorDashboard() {
        setTitle("Doctor Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ================= TOP BAR =================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // 🔹 Clinic Header Panel
        JPanel clinicHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));


        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        ClinicInfo info = ClinicConfig.load();
        String clinicName = (info != null && info.getName() != null)
                ? info.getName()
                : "Neurotherapy Clinic";

        if (info != null) {

            // ✅ Set clinic name
            if (info.getName() != null && !info.getName().isEmpty()) {
                lblTitle.setText(info.getName());
            } else {
                lblTitle.setText("Neurotherapy Clinic");
            }

            // ✅ Set logo
            if (info.getLogoPath() != null && !info.getLogoPath().isEmpty()) {
                try {
                    ImageIcon icon = new ImageIcon(info.getLogoPath());
                    Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                    lblLogo.setIcon(new ImageIcon(img));
                } catch (Exception e) {
                    System.out.println("Logo load failed");
                }
            }
        } else {
            lblTitle.setText("Neurotherapy Clinic");
        }

// 🔹 Add to header
        clinicHeader.add(lblLogo);
        clinicHeader.add(lblTitle);
       // JLabel lblTitle = new JLabel("Neurotherapy Clinic");
        //lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JButton btnAddPatient = new JButton("➕ Add Patient");

        JButton btnLogout = new JButton("Logout");

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(btnAddPatient);
        rightPanel.add(btnLogout);

        topBar.add(clinicHeader, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // ================= MAIN =================
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search by Mobile:"));
        txtSearchMobile = new JTextField(15);
        searchPanel.add(txtSearchMobile);
        JButton btnSearch = new JButton("Search");
        searchPanel.add(btnSearch);
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        //clinic logo and name
        JButton settingsBtn = new JButton("Clinic Settings");

        settingsBtn.addActionListener(e -> {
            ClinicSettingsDialog dialog = new ClinicSettingsDialog();
            dialog.setModal(true);  // ensure proper blocking
            dialog.setVisible(true);

            // 🔁 Refresh ONLY after dialog is closed
            refreshHeader();
        });
        //JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(settingsBtn);   // ✅ ADD THIS LINE
        rightPanel.add(btnAddPatient);
        rightPanel.add(btnLogout);

        // Table
        String[] columns = {"Patient ID", "Name", "Mobile", "Age", "Gender"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblPatients = new JTable(tableModel);
        mainPanel.add(new JScrollPane(tblPatients), BorderLayout.CENTER);

        // View button
        JButton btnView = new JButton("View Details");
        mainPanel.add(btnView, BorderLayout.SOUTH);

        // ================= ACTIONS =================
        btnSearch.addActionListener(e -> searchPatients());
        btnView.addActionListener(e -> viewPatientDetails());
        btnAddPatient.addActionListener(e -> openPatientForm());
        btnLogout.addActionListener(e -> logout());

        tblPatients.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) viewPatientDetails();
            }
        });

        loadAllPatients();
    }

    private void openPatientForm() {
        PatientHistoryFormMySQL form = new PatientHistoryFormMySQL();
        form.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                loadAllPatients();
            }
        });
        form.setVisible(true);
    }
    private void refreshHeader() {

        ClinicInfo info = ClinicConfig.load();

        if (info != null) {

            if (info.getName() != null && !info.getName().isEmpty()) {
                lblTitle.setText(info.getName());
            } else {
                lblTitle.setText("Neurotherapy Clinic");
            }

            if (info.getLogoPath() != null && !info.getLogoPath().isEmpty()) {
                try {
                    ImageIcon icon = new ImageIcon(info.getLogoPath());
                    Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                    lblLogo.setIcon(new ImageIcon(img));
                } catch (Exception e) {
                    lblLogo.setIcon(null);
                }
            } else {
                lblLogo.setIcon(null);
            }

        } else {
            lblTitle.setText("Neurotherapy Clinic");
            lblLogo.setIcon(null);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }

    // ================= DAO BASED =================

    private void loadAllPatients() {
        tableModel.setRowCount(0);

        try {
            List<Object[]> patients = PatientDAO.getAllPatients();

            for (Object[] row : patients) {
                tableModel.addRow(row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading patients:\n" + ex.getMessage());
        }
    }

    private void searchPatients() {
        String mobile = txtSearchMobile.getText().trim();

        if (mobile.isEmpty()) {
            loadAllPatients();
            return;
        }

        tableModel.setRowCount(0);

        try {
            List<Object[]> patients = PatientDAO.searchPatientsByMobile(mobile);

            for (Object[] row : patients) {
                tableModel.addRow(row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Search error:\n" + ex.getMessage());
        }
    }
    private void viewPatientDetails() {
        int row = tblPatients.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient.");
            return;
        }

        int patientId = (int) tableModel.getValueAt(row, 0);
        setVisible(false);
        new PatientDetailsFrame(this, patientId).setVisible(true);
    }

}