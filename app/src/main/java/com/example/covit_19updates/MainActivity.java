package com.example.covit_19updates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covit_19updates.Activities.HomePage.DashboardActivity;
import com.example.covit_19updates.ApiUrlsPackage.APIUrls;
import com.example.covit_19updates.Activities.ChooseCountryPackage.SelectCountryActivity;
import com.example.covit_19updates.ModelsPacakges.CountryNameModel;
import com.example.covit_19updates.ServicesPackages.CountryServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String PREF_NAME = "CountryCode";
    private Button getStarted;
    private ImageView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userInterface();
        onGetStartedClick();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString(PREF_NAME, null) != null) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }
    }

    private void onGetStartedClick() {
        getStarted.setOnClickListener(v -> countryListAPICall());
    }

    private void countryListAPICall() {
        try {
            getStarted.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
            AnimationDrawable animationDrawable = (AnimationDrawable) loading.getDrawable();
            animationDrawable.start();
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    APIUrls.GET_COUNTRIES,
                    response -> {
                        getStarted.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        animationDrawable.stop();
                        try {
                            ArrayList<CountryNameModel> countryListResponse = new Gson().fromJson(response, new TypeToken<List<CountryNameModel>>() {
                            }.getType());
                            if (CountryServices.addCountries(countryListResponse)) {
                                startActivity(new Intent(this, SelectCountryActivity.class));
                                finish();
                            }
                        } catch (Exception ex) {
                            Toast.makeText(this, "Failed to get country names: " + ex, Toast.LENGTH_SHORT).show();
                        }
                    }, error -> {
                getStarted.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                animationDrawable.stop();
                Toast.makeText(this, "Server error: " + error, Toast.LENGTH_SHORT).show();
            }
            );
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        } catch (Exception ex) {
            Toast.makeText(this, "Something went wrong : " + ex, Toast.LENGTH_SHORT).show();
        }
    }

    private void userInterface() {
        getStarted = findViewById(R.id.getStarted);
        loading = findViewById(R.id.loadingImage);
    }
}
