package com.yourdomain.model;

public class ChargingStationDTO {

    private String stationName;
    private String vehicleId;
    private float chargingSpeed;
    private boolean fastCharging;
    private String stationType;

    public ChargingStationDTO() {}

    public ChargingStationDTO(String stationName, String vehicleId, float chargingSpeed, boolean fastCharging, String stationType) {
        this.stationName = stationName;
        this.vehicleId = vehicleId;
        this.chargingSpeed = chargingSpeed;
        this.fastCharging = fastCharging;
        this.stationType = stationType;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public float getChargingSpeed() {
        return chargingSpeed;
    }

    public void setChargingSpeed(float chargingSpeed) {
        this.chargingSpeed = chargingSpeed;
    }

    public boolean isFastCharging() {
        return fastCharging;
    }

    public void setFastCharging(boolean fastCharging) {
        this.fastCharging = fastCharging;
    }

    public String getStationType() {
        return stationType;
    }

    public void setStationType(String stationType) {
        this.stationType = stationType;
    }

    @Override
    public String toString() {
        return "ChargingStationDTO{" +
                "stationName='" + stationName + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", chargingSpeed=" + chargingSpeed +
                ", fastCharging=" + fastCharging +
                ", stationType='" + stationType + '\'' +
                '}';
    }
}