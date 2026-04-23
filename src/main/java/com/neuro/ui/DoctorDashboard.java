package com.neuro.ui;

import com.neuro.config.ClinicConfig;
import com.neuro.dao.PatientDAO;
import com.neuro.model.ClinicInfo;
import com.neuro.session.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DoctorDashboard extends JFrame {

    private JTextField txtSearchMobile;
    private JTable tblPatients;
    private DefaultTableModel tableModel;

    private int userId;

    private JLabel lblLogo = new JLabel();
    private JLabel lblTitle = new JLabel();

    public DoctorDashboard(int userId) {

        // ✅ SAFE SESSION HANDLING
        this.userId = (userId > 0) ? userId : UserSession.getUserId();

        setTitle("Doctor Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ================= TOP BAR =================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel clinicHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

        ClinicInfo info = ClinicConfig.load();

        if (info != null && info.getName() != null) {
            lblTitle.setText(info.getName());
        } else {
            lblTitle.setText("Neurotherapy Clinic");
        }

        if (info != null && info.getLogoPath() != null && !info.getLogoPath().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(info.getLogoPath());
                Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
            } catch (Exception ignored) {}
        }

        clinicHeader.add(lblLogo);
        clinicHeader.add(lblTitle);

        // ================= BUTTONS =================
        JButton btnAddPatient = new JButton("➕ Add Patient");
        JButton btnLogout = new JButton("Logout");
        JButton settingsBtn = new JButton("Clinic Settings");

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(settingsBtn);
        rightPanel.add(btnAddPatient);
        rightPanel.add(btnLogout);

        topBar.add(clinicHeader, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // ================= MAIN =================
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // SEARCH
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search by Mobile:"));

        txtSearchMobile = new JTextField(15);
        searchPanel.add(txtSearchMobile);

        JButton btnSearch = new JButton("Search");
        searchPanel.add(btnSearch);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // TABLE
        String[] columns = {"Patient ID", "Name", "Mobile", "Age", "Gender"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tblPatients = new JTable(tableModel);
        mainPanel.add(new JScrollPane(tblPatients), BorderLayout.CENTER);

        JButton btnView = new JButton("View Details");
        mainPanel.add(btnView, BorderLayout.SOUTH);

        // ================= ACTIONS =================
        btnSearch.addActionListener(e -> searchPatients());
        btnView.addActionListener(e -> viewPatientDetails());

        btnAddPatient.addActionListener(e -> openPatientForm());

        btnLogout.addActionListener(e -> logout());

        settingsBtn.addActionListener(e -> {
            ClinicSettingsDialog dialog = new ClinicSettingsDialog();
            dialog.setModal(true);
            dialog.setVisible(true);
            refreshHeader();
        });

        tblPatients.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) viewPatientDetails();
            }
        });

        loadAllPatients();
        System.out.println("Dashboard userId = " + userId);
    }

    // ================= OPEN FORM =================
    private void openPatientForm() {

        PatientHistoryFormMySQL form = new PatientHistoryFormMySQL(userId);

        form.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                loadAllPatients();
            }
        });

        form.setVisible(true);
    }

    // ================= LOAD ALL =================
    private void loadAllPatients() {

        tableModel.setRowCount(0);

        try {
            List<Object[]> patients = PatientDAO.getAllPatients(userId);

            for (Object[] row : patients) {
                tableModel.addRow(row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading patients:\n" + ex.getMessage());
        }
    }

    // ================= SEARCH =================
    private void searchPatients() {

        String mobile = txtSearchMobile.getText().trim();

        if (mobile.isEmpty()) {
            loadAllPatients();
            return;
        }

        tableModel.setRowCount(0);

        try {
            List<Object[]> patients = PatientDAO.searchPatientsByMobile(userId, mobile);

            for (Object[] row : patients) {
                tableModel.addRow(row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Search error:\n" + ex.getMessage());
        }
    }

    // ================= VIEW =================
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

    // ================= LOGOUT =================
    private void logout() {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            UserSession.clear();
            new LoginFrame().setVisible(true);
            dispose();
        }
    }

    // ================= HEADER REFRESH =================
    private void refreshHeader() {

        ClinicInfo info = ClinicConfig.load();

        if (info != null && info.getName() != null) {
            lblTitle.setText(info.getName());
        } else {
            lblTitle.setText("Neurotherapy Clinic");
        }

        if (info != null && info.getLogoPath() != null) {
            try {
                ImageIcon icon = new ImageIcon(info.getLogoPath());
                Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                lblLogo.setIcon(null);
            }
        }
    }
}