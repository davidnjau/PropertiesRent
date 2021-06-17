package com.dave.properties.helper_class;

import android.widget.ScrollView;

public class PropertyData {

    private String property_name;
    private String property_location;
    private String property_rent;
    private String rent_due_at;
    private String property_details;

    public PropertyData() {
    }

    public PropertyData(String property_name, String property_location, String property_rent, String rent_due_at, String property_details) {
        this.property_name = property_name;
        this.property_location = property_location;
        this.property_rent = property_rent;
        this.rent_due_at = rent_due_at;
        this.property_details = property_details;
    }

    public String getProperty_name() {
        return property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public String getProperty_location() {
        return property_location;
    }

    public void setProperty_location(String property_location) {
        this.property_location = property_location;
    }

    public String getProperty_rent() {
        return property_rent;
    }

    public void setProperty_rent(String property_rent) {
        this.property_rent = property_rent;
    }

    public String getRent_due_at() {
        return rent_due_at;
    }

    public void setRent_due_at(String rent_due_at) {
        this.rent_due_at = rent_due_at;
    }

    public String getProperty_details() {
        return property_details;
    }

    public void setProperty_details(String property_details) {
        this.property_details = property_details;
    }
}
