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

    // ✅ Check if expired
    public boolean isExpired() {
        return expiry == null || LocalDate.now().isAfter(expiry);
    }

    // ✅ Days left (never negative)
    public long daysLeft() {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), expiry);
        return Math.max(days, 0);
    }

    public String getType() {
        return type;
    }

    public LocalDate getExpiry() {
        return expiry;
    }
}