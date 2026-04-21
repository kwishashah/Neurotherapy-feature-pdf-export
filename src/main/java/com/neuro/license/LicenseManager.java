package com.neuro.license;

import com.neuro.exceptions.LicenceException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.io.IOException;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Enumeration;

public class LicenseManager {

    private LicenseManager() {}

    // 🔐 MUST match generator project
    private static final String HMAC_ALGO = "HmacSHA256";
    private static final String SECRET_KEY = "your-very-secret-key";

    private static final Path LICENSE_FILE = Paths.get(
            System.getProperty("user.home"), ".neuro", "license.key"
    );

    private static final Path TRIAL_FILE = Paths.get(
            System.getProperty("user.home"), ".neuro", "trial.dat"
    );

    // ================= MACHINE ID =================
    public static String getMachineIdentifier() {
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();

            while (networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();

                // ✅ Skip unwanted interfaces
                if (network.isLoopback() || network.isVirtual() || !network.isUp())
                    continue;

                String name = network.getName().toLowerCase();

                // ✅ Prefer stable interfaces
                if (!(name.contains("en0") || name.contains("eth0") || name.contains("wlan")))
                    continue;

                byte[] mac = network.getHardwareAddress();

                if (mac != null && mac.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (byte b : mac) {
                        sb.append(String.format("%02X", b));
                    }
                    return sb.toString();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "UNKNOWN_MACHINE";
    }

    // ================= VALIDATE LICENSE =================
    public static LicenseInfo validateLicenseKey(String licenseKey) throws LicenceException {
        try {

            if (licenseKey == null || licenseKey.isBlank()) return null;

            String decoded = new String(Base64.getDecoder().decode(licenseKey));
            String[] parts = decoded.split("\\|");

            if (parts.length != 4) return null;

            String machineId = parts[0];
            String expiryDate = parts[1];
            String type = parts[2];
            String signature = parts[3];

            String currentMachineId = getMachineIdentifier();

            String data = machineId + "|" + expiryDate + "|" + type;

            Mac mac = Mac.getInstance(HMAC_ALGO);
            mac.init(new SecretKeySpec(SECRET_KEY.getBytes(), HMAC_ALGO));

            String expectedSignature = Base64.getEncoder()
                    .encodeToString(mac.doFinal(data.getBytes()));

            // 🔥 DEBUG FIRST (before returning anything)
            System.out.println("----- LICENSE DEBUG -----");
            System.out.println("Decoded: " + decoded);
            System.out.println("Machine in license: " + machineId);
            System.out.println("Current machine: " + currentMachineId);
            System.out.println("Expiry: " + expiryDate);
            System.out.println("Type: " + type);
            System.out.println("Signature (given): " + signature);
            System.out.println("Signature (expected): " + expectedSignature);
            System.out.println("-------------------------");

            // 🔴 Now validations
            if (!machineId.equals(currentMachineId)) {
                System.out.println("❌ Machine mismatch");
                return null;
            }

            if (!expectedSignature.equals(signature)) {
                System.out.println("❌ Signature mismatch");
                return null;
            }

            LocalDate expiry = LocalDate.parse(expiryDate);

            return new LicenseInfo(type, expiry);

        } catch (Exception e) {
            throw new LicenceException("Invalid License", e);
        }
    }
    // ================= TRIAL =================
    public static boolean isTrialValid() {
        try {
            if (!Files.exists(TRIAL_FILE)) {
                Files.createDirectories(TRIAL_FILE.getParent());
                Files.writeString(TRIAL_FILE, LocalDate.now().toString());
                return true;
            }

            LocalDate start = LocalDate.parse(Files.readString(TRIAL_FILE));
            long days = ChronoUnit.DAYS.between(start, LocalDate.now());

            return days <= 7;

        } catch (Exception e) {
            return false;
        }
    }

    // ================= MAIN LICENSE CHECK =================
    public static boolean checkLicenseOrExit() {

        try {
            String key = loadLicense();

            System.out.println("🔐 Starting License Check...");
            System.out.println("LICENSE KEY FROM FILE: " + key);
            if (key == null) {

                if (isTrialValid()) {
                    JOptionPane.showMessageDialog(null, "Trial Mode Active");
                    return true;
                }

                showLicenseDialog();
                return false;
            }

            LicenseInfo info = validateLicenseKey(key);

            if (info == null) {
                showLicenseDialog();
                return false;
            }

            if (info.isExpired()) {
                JOptionPane.showMessageDialog(null, "License Expired!");
                showLicenseDialog();
                return false;
            }

            if (info.daysLeft() <= 5) {
                JOptionPane.showMessageDialog(null,
                        "License expires in " + info.daysLeft() + " days!");
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "License Error");
            return false;
        }
    }
    // ================= LICENSE UI =================
    private static void showLicenseDialog() {

        LicenseDialog dialog = new LicenseDialog();
        dialog.setVisible(true);

        if (!dialog.isSuccess()) {
            System.exit(0);
        }
    }

    // ================= SAVE / LOAD =================
    public static void saveLicense(String licenseKey) throws IOException {
        Files.createDirectories(LICENSE_FILE.getParent());
        Files.writeString(LICENSE_FILE, licenseKey, StandardCharsets.UTF_8);
    }

    public static String loadLicense() {
        try {
            if (!Files.exists(LICENSE_FILE)) return null;

            return Files.readString(LICENSE_FILE, StandardCharsets.UTF_8).trim();

        } catch (IOException e) {
            return null;
        }
    }
}