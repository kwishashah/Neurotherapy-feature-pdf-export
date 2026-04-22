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

        String data = info.getName() + "|" + info.getLogoPath();
        Files.writeString(CONFIG_FILE, data);
    }

    public static ClinicInfo load() {
        try {
            if (!Files.exists(CONFIG_FILE)) return null;

            String data = Files.readString(CONFIG_FILE);
            String[] parts = data.split("\\|");

            return new ClinicInfo(parts[0], parts[1]);

        } catch (Exception e) {
            return null;
        }
    }
}