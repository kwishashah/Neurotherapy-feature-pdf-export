package com.neuro.config;

import com.neuro.model.ClinicInfo;

import java.io.IOException;
import java.nio.file.*;

public class ClinicConfig {

    private static final Path CONFIG_FILE = Paths.get(
            System.getProperty("user.home"), ".neuro", "clinic.cfg"
    );

    public static void save(ClinicInfo info) throws IOException {
        Files.createDirectories(CONFIG_FILE.getParent());

        String data = (info.getName() == null ? "" : info.getName())
                + "|" + (info.getLogoPath() == null ? "" : info.getLogoPath());

        Files.writeString(CONFIG_FILE, data);
    }

    public static ClinicInfo load() {
        try {
            if (!Files.exists(CONFIG_FILE)) return null;

            String data = Files.readString(CONFIG_FILE).trim();

            if (data.isEmpty()) return null;

            String[] parts = data.split("\\|", -1);

            String name = parts.length > 0 ? parts[0] : "";
            String logo = parts.length > 1 ? parts[1] : "";

            return new ClinicInfo(name, logo);

        } catch (Exception e) {
            return null;
        }
    }
}