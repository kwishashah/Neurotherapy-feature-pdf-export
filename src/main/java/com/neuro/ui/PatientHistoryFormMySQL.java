package com.neuro.ui;

import com.neuro.dao.PatientDAO;

import javax.swing.*;
import java.awt.*;

public class PatientHistoryFormMySQL extends JFrame {

    private JTextField txtName, txtMobile, txtAge, txtOccupation;
    private JTextField txtBloodGroup, txtHeight, txtWeight, txtDuration;

    private JTextField txtBP, txtPulse, txtO2, txtTemp;

    private JTextArea txtAddress, txtMainDisease, txtComplications;
    private JTextArea txtSymptoms, txtAllergy, txtRemarks;
    private JTextArea txtPreviousTreatment, txtMedicines, txtDetailedHistory;
    private JTextArea txtExamination, txtReportAnalysis;

    private JTextField txtReportPath;
    private JButton btnUploadReport;

    // 🔥 UPDATED
    private JComboBox<String>[] painFields = new JComboBox[18];
    private JComboBox<String> left4th, right4th;

    private JComboBox<String> cmbGender, cmbMarital;

    public PatientHistoryFormMySQL() {

        setTitle("Patient History Form");
        setSize(950, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // Basic Fields
        txtName = new JTextField(20);
        txtMobile = new JTextField(20);
        txtAge = new JTextField(5);
        cmbGender = new JComboBox<>(new String[]{"Male","Female","Other"});
        cmbMarital = new JComboBox<>(new String[]{"Single","Married"});
        txtAddress = createArea();
        txtOccupation = new JTextField(20);
        txtBloodGroup = new JTextField();
        txtHeight = new JTextField(5);
        txtWeight = new JTextField(5);
        txtDuration = new JTextField();

        txtMainDisease = createArea();
        txtComplications = createArea();
        txtSymptoms = createArea();

        txtPreviousTreatment = createArea();
        txtMedicines = createArea();
        txtDetailedHistory = createArea();
        txtExamination = createArea();

        txtReportPath = new JTextField(20);
        txtReportPath.setEditable(false);

        btnUploadReport = new JButton("Upload Report");

        txtReportAnalysis = createArea();
        txtAllergy = createArea();
        txtRemarks = createArea();

        // Vitals
        txtBP = new JTextField(8);
        txtPulse = new JTextField(5);
        txtO2 = new JTextField(5);
        txtTemp = new JTextField(5);

        // Layout
        y = addRow(panel, gbc, y, "Patient Name", txtName);
        y = addRow(panel, gbc, y, "Mobile", txtMobile);
        y = addRow(panel, gbc, y, "Age", txtAge);
        y = addRow(panel, gbc, y, "Gender", cmbGender);
        y = addRow(panel, gbc, y, "Marital", cmbMarital);
        y = addRow(panel, gbc, y, "Address", txtAddress);
        y = addRow(panel, gbc, y, "Occupation", txtOccupation);
        y = addRow(panel, gbc, y, "Blood Group", txtBloodGroup);

        JPanel hw = new JPanel(new FlowLayout(FlowLayout.LEFT));
        hw.add(txtHeight); hw.add(new JLabel("cm"));
        hw.add(txtWeight); hw.add(new JLabel("kg"));
        y = addRow(panel, gbc, y, "Height / Weight", hw);

        y = addRow(panel, gbc, y, "Duration", txtDuration);
        y = addRow(panel, gbc, y, "Main Disease", txtMainDisease);
        y = addRow(panel, gbc, y, "Complications", txtComplications);
        y = addRow(panel, gbc, y, "Symptoms", txtSymptoms);

        // 🔴 PAIN POINTS
        JPanel painPanel = new JPanel(new GridLayout(0,2));

        String[] names = {
                "Pan","Gas","Gast","WD","Gal","Spl","Liv","Mu",
                "Rtov","Ltov","Dys","Const","Liv0","Mul0","Follic","Thia","B12","Nia"
        };

        String[] scale = {"0","1","2","3","4","5"};

        for(int i=0;i<names.length;i++){
            painFields[i] = new JComboBox<>(scale);
            painPanel.add(new JLabel(names[i]));
            painPanel.add(painFields[i]);
        }

        left4th = new JComboBox<>(new String[]{"No","Yes"});
        right4th = new JComboBox<>(new String[]{"No","Yes"});

        painPanel.add(new JLabel("Left 4th"));
        painPanel.add(left4th);
        painPanel.add(new JLabel("Right 4th"));
        painPanel.add(right4th);

        y = addRow(panel, gbc, y, "Pain Points", painPanel);

        // Vitals
        JPanel vitals = new JPanel(new FlowLayout(FlowLayout.LEFT));
        vitals.add(new JLabel("BP")); vitals.add(txtBP);
        vitals.add(new JLabel("Pulse")); vitals.add(txtPulse);
        vitals.add(new JLabel("O2")); vitals.add(txtO2);
        vitals.add(new JLabel("Temp")); vitals.add(txtTemp);

        btnUploadReport.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();

            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "PDF & Images", "pdf", "jpg", "jpeg", "png"
            ));

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                txtReportPath.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        JPanel reportPanel = new JPanel(new BorderLayout(5,0));
        reportPanel.add(txtReportPath, BorderLayout.CENTER);
        reportPanel.add(btnUploadReport, BorderLayout.EAST);

        y = addRow(panel, gbc, y, "Reports (Upload)", reportPanel);
        y = addRow(panel, gbc, y, "Vitals", vitals);

        y = addRow(panel, gbc, y, "Previous Treatment", txtPreviousTreatment);
        y = addRow(panel, gbc, y, "Medicines", txtMedicines);
        y = addRow(panel, gbc, y, "Detailed History", txtDetailedHistory);
        y = addRow(panel, gbc, y, "Examination", txtExamination);

        y = addRow(panel, gbc, y, "Report Analysis", txtReportAnalysis);
        y = addRow(panel, gbc, y, "Allergy", txtAllergy);
        y = addRow(panel, gbc, y, "Remarks", txtRemarks);

        JButton btn = new JButton("Save");
        btn.addActionListener(e -> saveData());

        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        panel.add(btn, gbc);

        // 🔥 ENABLE ENTER NAVIGATION
        enableEnterFocus(txtName);
        enableEnterFocus(txtMobile);
        enableEnterFocus(txtAge);
        enableEnterFocus(txtOccupation);
        enableEnterFocus(txtBloodGroup);
        enableEnterFocus(txtHeight);
        enableEnterFocus(txtWeight);
        enableEnterFocus(txtDuration);
        enableEnterFocus(txtMainDisease);
        enableEnterFocus(txtComplications);
        enableEnterFocus(txtPreviousTreatment);
        enableEnterFocus(txtMedicines);
        enableEnterFocus(txtDetailedHistory);
        enableEnterFocus(txtExamination);
        enableEnterFocus(txtReportAnalysis);
        enableEnterFocus(txtAllergy);
        enableEnterFocus(txtRemarks);

        // 🔴 Special handling for Symptoms
        txtSymptoms.setFocusTraversalKeysEnabled(false);
        txtSymptoms.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    painFields[0].requestFocus();
                    e.consume();
                }
            }
        });
    }

    private void enableEnterFocus(JComponent comp) {
        comp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    comp.transferFocus();
                    e.consume();
                }
            }
        });
    }

    private void saveData() {
        try {

            Float height = txtHeight.getText().isEmpty()?null:Float.parseFloat(txtHeight.getText());
            Float weight = txtWeight.getText().isEmpty()?null:Float.parseFloat(txtWeight.getText());

            StringBuilder pain = new StringBuilder();
            for(int i=0;i<painFields.length;i++){
                pain.append(painFields[i].getSelectedItem()).append(",");
            }
            pain.append("L4=").append(left4th.getSelectedItem()).append(",");
            pain.append("R4=").append(right4th.getSelectedItem());

            PatientDAO.savePatient(
                    null,
                    txtName.getText(),
                    txtMobile.getText(),
                    txtAge.getText().isEmpty()?null:Integer.parseInt(txtAge.getText()),
                    (String)cmbGender.getSelectedItem(),
                    (String)cmbMarital.getSelectedItem(),
                    txtAddress.getText(),
                    txtOccupation.getText(),
                    txtBloodGroup.getText(),
                    height,
                    weight,
                    txtDuration.getText(),
                    txtMainDisease.getText(),
                    txtComplications.getText(),
                    txtSymptoms.getText(),
                    pain.toString(),
                    "", "", "", "", "", "",
                    txtPreviousTreatment.getText(),
                    txtMedicines.getText(),
                    txtDetailedHistory.getText(),
                    txtExamination.getText(),
                    txtBP.getText(),
                    txtPulse.getText(),
                    txtO2.getText(),
                    txtTemp.getText(),
                    txtReportPath.getText(),
                    txtReportAnalysis.getText(),
                    txtAllergy.getText(),
                    txtRemarks.getText(),
                    null
            );

            JOptionPane.showMessageDialog(this,"Saved!");
            dispose();

        } catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    private JTextArea createArea(){
        JTextArea a = new JTextArea(3,20);
        a.setLineWrap(true);
        return a;
    }

    private int addRow(JPanel panel, GridBagConstraints gbc, int y, String label, Component field){
        gbc.gridx=0; gbc.gridy=y;
        panel.add(new JLabel(label),gbc);
        gbc.gridx=1;
        panel.add(field instanceof JTextArea?new JScrollPane(field):field,gbc);
        return y+1;
    }
}