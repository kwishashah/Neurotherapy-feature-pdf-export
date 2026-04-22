package com.neuro.model;

public class ClinicInfo {

    private String name;
    private String logoPath;

    public ClinicInfo(String name, String logoPath) {
        this.name = name;
        this.logoPath = logoPath;
    }

    public String getName() {
        return name;
    }

    public String getLogoPath() {
        return logoPath;
    }
}