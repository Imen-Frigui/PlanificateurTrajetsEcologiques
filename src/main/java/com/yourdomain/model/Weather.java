package com.yourdomain.model;

public class Weather {
    private String weatherType; // Corresponds to PlanificateurTrajetsEcologiques:WeatherType
    private boolean sunny; // Corresponds to PlanificateurTrajetsEcologiques:Sunny
    private boolean snowy; // Corresponds to PlanificateurTrajetsEcologiques:Snowy
    private boolean rainy; // Corresponds to PlanificateurTrajetsEcologiques:Rainy
    private float temperature; // Corresponds to PlanificateurTrajetsEcologiques:Temperature

    // Object properties
    private String affectsTrafficCondition; // Corresponds to ObjectProperty affectsTrafficCondition

    // Add getters and setters
    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public boolean isSunny() {
        return sunny;
    }

    public void setSunny(boolean sunny) {
        this.sunny = sunny;
    }

    public boolean isSnowy() {
        return snowy;
    }

    public void setSnowy(boolean snowy) {
        this.snowy = snowy;
    }

    public boolean isRainy() {
        return rainy;
    }

    public void setRainy(boolean rainy) {
        this.rainy = rainy;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getAffectsTrafficCondition() {
        return affectsTrafficCondition;
    }

    public void setAffectsTrafficCondition(String affectsTrafficCondition) {
        this.affectsTrafficCondition = affectsTrafficCondition;
    }
}
