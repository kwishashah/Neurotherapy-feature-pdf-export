package com.neuro.license;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LicenseInfo {

    private final String type;
    private final LocalDate expiry;

    public LicenseInfo(String type, LocalDate expiry) {
        this.type = type;
        this.expiry = expiry;
    }

    // ✅ Check if expired using trusted date
    public boolean isExpired() {
        LocalDate today = LicenseManager.getTrustedDate();
        return expiry == null || today.isAfter(expiry);
    }

    // ✅ Days left using trusted date (never negative)
    public long daysLeft() {
        LocalDate today = LicenseManager.getTrustedDate();
        long days = ChronoUnit.DAYS.between(today, expiry);
        return Math.max(days, 0);
    }

    public String getType() {
        return type;
    }

    public LocalDate getExpiry() {
        return expiry;
    }
}