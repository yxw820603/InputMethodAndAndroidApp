package com.example.androidapp;

public class StatusManager {
    private boolean enabled = false;

    public void toggleStatus() {
        enabled = !enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
