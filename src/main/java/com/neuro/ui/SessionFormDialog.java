package com.neuro.ui;

import com.neuro.dao.SessionDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Date;

public class SessionFormDialog extends JDialog {

    private final int patientId;
    private final Integer sessionId;
    private final PatientDetailsFrame parentFrame;

    private JTextField txtSessionNumber;
    private JTextField txtDate;
    private JTextArea txtTreatment;
    private JTextArea txtSummary;

    private JComboBox<String>[] beforeFields = new JComboBox[18];
    private JComboBox<String>[] afterFields = new JComboBox[18];

    public SessionFormDialog(PatientDetailsFrame parent, int patientId, Integer sessionId) {
        super(parent, true);
        this.parentFrame = parent;
        this.patientId = patientId;
        this.sessionId = sessionId;

        setTitle(sessionId == null ? "Add Session" : "Update Session");
        setSize(750, 650);
        setLocationRelativeTo(parent);

        initComponents();

        // 🔥 AUTO FILL NEW SESSION
        if (sessionId == null) {
            int next = SessionDAO.getNextSessionNumber(patientId);
            txtSessionNumber.setText(String.valueOf(next));
            txtDate.setText(java.time.LocalDate.now().toString());
        } else {
            loadSessionData();
        }
    }

    private void initComponents() {

        JPanel mainPanel = new JPanel(new BorderLayout(10,10));

        JPanel top = new JPanel(new GridLayout(3,2,5,5));

        top.add(new JLabel("Session Number:"));
        txtSessionNumber = new JTextField();
        top.add(txtSessionNumber);

        top.add(new JLabel("Date (yyyy-MM-dd):"));
        txtDate = new JTextField();
        top.add(txtDate);

        top.add(new JLabel("Treatment:"));
        txtTreatment = new JTextArea(2,20);
        top.add(new JScrollPane(txtTreatment));

        mainPanel.add(top, BorderLayout.NORTH);

        // 🔴 PAIN TABLE
        JPanel painPanel = new JPanel(new GridLayout(0,3,10,10));

        painPanel.add(new JLabel("Pain"));
        painPanel.add(new JLabel("Before"));
        painPanel.add(new JLabel("After"));

        String[] names = {
                "Pan","Gas","Gast","WD","Gal","Spl","Liv","Mu",
                "Rtov","Ltov","Dys","Const","Liv0","Mul0","Follic","Thia","B12","Nia"
        };

        String[] scale = {"0","1","2","3","4","5"};

        for(int i=0;i<names.length;i++){
            beforeFields[i] = new JComboBox<>(scale);
            afterFields[i] = new JComboBox<>(scale);

            painPanel.add(new JLabel(names[i]));
            painPanel.add(beforeFields[i]);
            painPanel.add(afterFields[i]);
        }

        mainPanel.add(new JScrollPane(painPanel), BorderLayout.CENTER);

        // 🔴 SUMMARY
        JPanel bottom = new JPanel(new BorderLayout(5,5));

        txtSummary = new JTextArea(4,20);
        bottom.add(new JLabel("Session Summary:"), BorderLayout.NORTH);
        bottom.add(new JScrollPane(txtSummary), BorderLayout.CENTER);

        JButton btnSave = new JButton(sessionId == null ? "Add" : "Update");
        btnSave.addActionListener(this::handleSave);

        bottom.add(btnSave, BorderLayout.SOUTH);

        mainPanel.add(bottom, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // 🔴 LOAD EXISTING SESSION
    private void loadSessionData() {
        try {
            var sessions = SessionDAO.getSessionsByPatient(patientId);

            for (var row : sessions) {

                if ((int) row.get(0) == sessionId) {

                    txtSessionNumber.setText(String.valueOf(row.get(1)));
                    txtDate.setText(String.valueOf(row.get(2)));
                    txtTreatment.setText(String.valueOf(row.get(3)));
                    txtSummary.setText(String.valueOf(row.get(6)));

                    String painData = (String) row.get(4);

                    if (painData != null) {

                        String[] pairs = painData.split(",");

                        for (int i = 0; i < pairs.length && i < beforeFields.length; i++) {

                            if (pairs[i].contains("->")) {

                                String[] vals = pairs[i].split("->");

                                beforeFields[i].setSelectedItem(vals[0]);
                                afterFields[i].setSelectedItem(vals[1]);
                            }
                        }
                    }

                    break;
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading session");
            e.printStackTrace();
        }
    }

    private String buildPainString() {
        StringBuilder sb = new StringBuilder();

        for(int i=0;i<beforeFields.length;i++){
            sb.append(beforeFields[i].getSelectedItem())
                    .append("->")
                    .append(afterFields[i].getSelectedItem())
                    .append(",");
        }

        return sb.toString();
    }

    private void handleSave(ActionEvent e) {
        try {

            int sessionNo = Integer.parseInt(txtSessionNumber.getText().trim());
            Date sqlDate = Date.valueOf(txtDate.getText().trim());

            String treatment = txtTreatment.getText().trim();
            String summary = txtSummary.getText().trim();

            String pain = buildPainString();

            if (sessionId == null) {

                SessionDAO.addSession(
                        patientId,
                        sessionNo,
                        sqlDate,
                        treatment,
                        pain,
                        "",   // after merged into pain
                        summary
                );

                JOptionPane.showMessageDialog(this, "Session Added!");

            } else {

                SessionDAO.updateSession(
                        sessionId,
                        sessionNo,
                        sqlDate,
                        treatment,
                        pain,
                        "",
                        summary
                );

                JOptionPane.showMessageDialog(this, "Session Updated!");
            }

            // 🔥 REFRESH TABLE
            parentFrame.loadSessions();

            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}