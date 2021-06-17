package com.dave.properties.helper_class;

public class RentDurationData {

    private String duration;
    private String rentAmount;

    public RentDurationData() {
    }

    public RentDurationData(String duration, String rentAmount) {
        this.duration = duration;
        this.rentAmount = rentAmount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(String rentAmount) {
        this.rentAmount = rentAmount;
    }
}
