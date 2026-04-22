package com.neuro.ui;

import com.neuro.config.ClinicConfig;
import com.neuro.model.ClinicInfo;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ClinicSettingsDialog extends JDialog {

    private JTextField nameField;
    private JLabel logoLabel;
    private String logoPath;

    public ClinicSettingsDialog() {
        setTitle("Clinic Settings");
        setSize(400, 300);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 1));

        nameField = new JTextField();
        panel.add(new JLabel("Clinic Name:"));
        panel.add(nameField);

        JButton uploadBtn = new JButton("Upload Logo");
        logoLabel = new JLabel("No file selected");

        uploadBtn.addActionListener(e -> chooseLogo());

        panel.add(uploadBtn);
        panel.add(logoLabel);

        JButton saveBtn = new JButton("Save");

        saveBtn.addActionListener(e -> save());

        add(panel, BorderLayout.CENTER);
        add(saveBtn, BorderLayout.SOUTH);

        loadExisting();
    }

    private void chooseLogo() {
        JFileChooser chooser = new JFileChooser();

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            logoPath = file.getAbsolutePath();
            logoLabel.setText(file.getName());
        }
    }

    private void save() {
        try {
            ClinicInfo info = new ClinicInfo(
                    nameField.getText(),
                    logoPath
            );

            ClinicConfig.save(info);

            JOptionPane.showMessageDialog(this, "Saved!");
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving");
        }
    }

    private void loadExisting() {
        ClinicInfo info = ClinicConfig.load();

        if (info != null) {
            nameField.setText(info.getName());
            logoPath = info.getLogoPath();
            logoLabel.setText(new File(logoPath).getName());
        }
    }
}