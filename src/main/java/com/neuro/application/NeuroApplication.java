package com.neuro.application;
import java.nio.file.Paths;
import com.neuro.dao.UserDAO;
import com.neuro.license.LicenseManager;
import com.neuro.ui.LoginFrame;
import com.neuro.ui.SignupFrame;
import java.time.LocalDate;
import javax.swing.*;
import com.neuro.license.LicenseInfo;
import com.neuro.license.LicenseDialog;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
/**
 * Main Application Entry Point for Neurotherapy Dashboard
 *
 * This class serves as the bootstrap for the application,
 * initializing the UI and checking for existing users.
 *
 * Layer Architecture:
 * - Database Layer: com.neuro.db (DBConnection)
 * - Data Access Layer: com.neuro.dao (UserDAO, PatientDAO, SessionDAO)
 * - Model Layer: com.neuro.model (Patient, PatientColumns)
 * - UI Layer: com.neuro.ui (All Swing frames and dialogs)
 * - Application Layer: com.neuro.application (Main entry point)
 */
public class NeuroApplication {
    public static void clearClipboard() {
        try {
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(""), null);
        } catch (Exception ignored) {}
    }

    public static void main(String[] args) {

        clearClipboard();

        if (args.length > 0 && args[0].equalsIgnoreCase("--machine-id")) {
            System.out.println(LicenseManager.getMachineIdentifier());
            return;
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // 🔐 LICENSE CHECK
        boolean valid = LicenseManager.checkLicenseOrExit();
        if (!valid) {
            System.exit(0);
        }

        SwingUtilities.invokeLater(() -> {

            if (UserDAO.hasAnyUser()) {
                new LoginFrame().setVisible(true);
            } else {
                new SignupFrame().setVisible(true);
            }
        });
    }
}
