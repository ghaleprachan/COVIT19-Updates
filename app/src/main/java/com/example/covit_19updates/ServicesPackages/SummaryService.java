package com.example.covit_19updates.ServicesPackages;

import com.example.covit_19updates.ModelsPacakges.Summary.Country;
import com.example.covit_19updates.ModelsPacakges.Summary.SummuryModel;

import java.util.ArrayList;

public class SummaryService {
    public static ArrayList<SummuryModel> summuryModel = new ArrayList<>(1);

    public static boolean addSummary(SummuryModel summury) {
        summuryModel.clear();
        summuryModel.add(0, summury);
        return true;
    }

    public static ArrayList<Country> getCountryStates(String countryCode) {
        ArrayList<Country> countryArrayList = new ArrayList<>(1);
        for (int i = 0; i < summuryModel.get(0).getCountries().size(); i++) {
            if (summuryModel.get(0).getCountries().get(i).getSlug().equalsIgnoreCase(countryCode)) {
                countryArrayList.clear();
                countryArrayList.add(0, summuryModel.get(0).getCountries().get(i));
            }
        }
        return countryArrayList;
    }
}
