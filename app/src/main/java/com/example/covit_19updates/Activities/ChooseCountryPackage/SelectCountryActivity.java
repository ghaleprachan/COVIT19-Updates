package com.example.covit_19updates.Activities.ChooseCountryPackage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covit_19updates.Activities.HomePage.DashboardActivity;
import com.example.covit_19updates.R;
import com.example.covit_19updates.ServicesPackages.CountryServices;

public class SelectCountryActivity extends AppCompatActivity {
    private static final String PREF_NAME = "CountryCode";
    private static final String COUNTRY_NAME = "CountryName";
    private Button next;
    private AutoCompleteTextView countryName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_counry_layout);
        userInterface();
        checkTheCountry();
        next.setEnabled(false);
        onCountryNameChange();
        setUpCountryAsType();
        onNextButtonClick();
    }

    private void checkTheCountry() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString(PREF_NAME, null) != null) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }
    }

    private void onNextButtonClick() {
        next.setOnClickListener(v -> {
            try {
                String selectedCountry = CountryServices.getSelectedCountryDetails(countryName.getText().toString()).get(0).getSlug();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(PREF_NAME, selectedCountry);
                editor.putString(COUNTRY_NAME, CountryServices.getSelectedCountryDetails(countryName.getText().toString()).get(0).getCountry());
                editor.apply();
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
            } catch (Exception ex) {
                Toast.makeText(this, "Exception: Country not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpCountryAsType() {
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, CountryServices.getAllCountry());
        countryName.setAdapter(countryAdapter);
    }

    private void onCountryNameChange() {
        countryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (countryName.getText().toString().length() == 0 || countryName.getText().toString().isEmpty()) {
                    next.setEnabled(false);
                } else {
                    next.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void userInterface() {
        next = findViewById(R.id.next);
        countryName = findViewById(R.id.countryName);
    }
}
