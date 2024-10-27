package com.yourdomain.model;

public class SpeedDTO {
    private String speedName;
    private float speedValue;
    private boolean slowSpeed;
    private boolean mediumSpeed;
    private boolean fastSpeed;

    // Getters and setters
    public String getSpeedName() {
        return speedName;
    }

    public void setSpeedName(String speedName) {
        this.speedName = speedName;
    }

    public float getSpeedValue() {
        return speedValue;
    }

    public void setSpeedValue(float speedValue) {
        this.speedValue = speedValue;
    }

    public boolean isSlowSpeed() {
        return slowSpeed;
    }

    public void setSlowSpeed(boolean slowSpeed) {
        this.slowSpeed = slowSpeed;
    }

    public boolean isMediumSpeed() {
        return mediumSpeed;
    }

    public void setMediumSpeed(boolean mediumSpeed) {
        this.mediumSpeed = mediumSpeed;
    }

    public boolean isFastSpeed() {
        return fastSpeed;
    }

    public void setFastSpeed(boolean fastSpeed) {
        this.fastSpeed = fastSpeed;
    }
}
