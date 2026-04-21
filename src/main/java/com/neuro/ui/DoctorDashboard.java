package com.neuro.ui;

import com.neuro.dao.PatientDAO;
import com.neuro.dao.UserDAO;
import com.neuro.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DoctorDashboard extends JFrame {

    private JTextField txtSearchMobile;
    private JTable tblPatients;
    private DefaultTableModel tableModel;

    public DoctorDashboard() {
        setTitle("Doctor Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ================= TOP BAR =================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("Neurotherapy Clinic");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JButton btnAddPatient = new JButton("➕ Add Patient");
        JButton btnLogout = new JButton("Logout");

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(btnAddPatient);
        rightPanel.add(btnLogout);

        topBar.add(lblTitle, BorderLayout.WEST);
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