
package com.neuro.license;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class LicenseDialog extends JDialog {

    private static final Logger logger = LoggerFactory.getLogger(LicenseDialog.class);

    private JTextField txtKey;
    private boolean success = false;

    public LicenseDialog() {
        setTitle("Activate License");
        setSize(400, 220);
        setLocationRelativeTo(null);
        setModal(true);

        JPanel panel = new JPanel(new BorderLayout(10,10));

        JLabel title = new JLabel("Enter License Key", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        txtKey = new JTextField();

        JButton btnActivate = new JButton("Activate");

        btnActivate.addActionListener(e -> handleActivate());

        panel.add(title, BorderLayout.NORTH);
        panel.add(txtKey, BorderLayout.CENTER);
        panel.add(btnActivate, BorderLayout.SOUTH);

        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        add(panel);
    }

    private void handleActivate() {
        try {
            String key = txtKey.getText().trim();

            LicenseInfo info = LicenseManager.validateLicenseKey(key);

            if (info != null && !info.isExpired()) {
                LicenseManager.saveLicense(key);
                success = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid or expired license");
            }

        } catch (Exception e) {
            logger.error("License validation failed", e);
            JOptionPane.showMessageDialog(this, "Error validating license");
        }
    }

    public boolean isSuccess() {
        return success;
    }
}