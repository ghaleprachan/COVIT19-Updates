package com.example.covit_19updates.ServicesPackages;

import com.example.covit_19updates.ModelsPacakges.CountryNameModel;

import java.util.ArrayList;

public class CountryServices {
    public static ArrayList<CountryNameModel> countryList = new ArrayList<>();

    public static boolean addCountries(ArrayList<CountryNameModel> names) {
        countryList.clear();
        countryList.addAll(names);
        return true;
    }

    public static ArrayList<String> getAllCountry() {
        ArrayList<String> countriesName = new ArrayList<>();
        for (int i = 0; i < countryList.size(); i++) {
            countriesName.add(countryList.get(i).getCountry());
        }
        return countriesName;
    }

    public static ArrayList<CountryNameModel> getSelectedCountryDetails(String countryName) {
        ArrayList<CountryNameModel> countryDetails = new ArrayList<>(1);
        for (int i = 0; i < countryList.size(); i++) {
            if (countryList.get(i).getCountry().equalsIgnoreCase(countryName)) {
                countryDetails.clear();
                countryDetails.add(countryList.get(i));
            }
        }
        return countryDetails;
    }
}
