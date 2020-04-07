package com.allocare.thenibazzar.kitmodule;

public interface IGPSActivity {
    public void locationChanged(double longitude, double latitude);
    public void displayGPSSettingsDialog();
}
