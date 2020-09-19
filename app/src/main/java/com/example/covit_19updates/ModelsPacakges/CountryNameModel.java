package com.example.covit_19updates.ModelsPacakges;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryNameModel {

    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("Slug")
    @Expose
    private String slug;
    @SerializedName("ISO2")
    @Expose
    private String iSO2;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getISO2() {
        return iSO2;
    }

    public void setISO2(String iSO2) {
        this.iSO2 = iSO2;
    }

    public CountryNameModel(String country, String slug, String iSO2) {
        this.country = country;
        this.slug = slug;
        this.iSO2 = iSO2;
    }
}