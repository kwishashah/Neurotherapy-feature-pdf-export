package com.neuro.ui;

import com.neuro.config.ClinicConfig;
import com.neuro.model.ClinicInfo;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ClinicSettingsDialog extends JDialog {

    private JTextField nameField;
    private JLabel logoLabel;
    private JLabel previewLabel;
    private String logoPath;

    public ClinicSettingsDialog() {
        setTitle("Clinic Settings");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 🔹 Clinic Name
        panel.add(new JLabel("Clinic Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(Box.createVerticalStrut(15));

        // 🔹 Upload Button
        JButton uploadBtn = new JButton("Upload Logo");
        panel.add(uploadBtn);

        // 🔹 Logo name
        logoLabel = new JLabel("No file selected");
        panel.add(logoLabel);

        panel.add(Box.createVerticalStrut(10));

        // 🔹 Preview
        previewLabel = new JLabel();
        previewLabel.setPreferredSize(new Dimension(120, 60));
        previewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(previewLabel);

        panel.add(Box.createVerticalStrut(20));

        // 🔹 Save button
        JButton saveBtn = new JButton("Save");

        add(panel, BorderLayout.CENTER);
        add(saveBtn, BorderLayout.SOUTH);

        // ================= ACTIONS =================
        uploadBtn.addActionListener(e -> chooseLogo());
        saveBtn.addActionListener(e -> save());

        loadExisting();
    }

    private void chooseLogo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image Files (JPG, PNG, JPEG)", "jpg", "jpeg", "png"
        ));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();
            String path = file.getAbsolutePath().toLowerCase();

            // 🔴 Validation
            if (!(path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg"))) {

                JOptionPane.showMessageDialog(
                        this,
                        "❌ Invalid file selected!\nPlease choose PNG or JPG image.",
                        "Invalid File",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // ✅ Save path
            logoPath = file.getAbsolutePath();
            logoLabel.setText(file.getName());

            // ✅ Preview image
            ImageIcon icon = new ImageIcon(logoPath);
            Image img = icon.getImage().getScaledInstance(120, 60, Image.SCALE_SMOOTH);
            previewLabel.setIcon(new ImageIcon(img));
        }
    }

    private void save() {
        try {
            ClinicInfo info = new ClinicInfo(
                    nameField.getText(),
                    logoPath
            );

            ClinicConfig.save(info);

            JOptionPane.showMessageDialog(this, "✅ Saved Successfully!");
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error saving");
        }
    }

    private void loadExisting() {
        ClinicInfo info = ClinicConfig.load();

        if (info != null) {
            nameField.setText(info.getName());
            logoPath = info.getLogoPath();

            if (logoPath != null && !logoPath.isEmpty()) {
                File file = new File(logoPath);
                logoLabel.setText(file.getName());

                ImageIcon icon = new ImageIcon(logoPath);
                Image img = icon.getImage().getScaledInstance(120, 60, Image.SCALE_SMOOTH);
                previewLabel.setIcon(new ImageIcon(img));
            }
        }
    }
}