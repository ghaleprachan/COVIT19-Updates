package com.example.covit_19updates.Activities.HomePage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covit_19updates.Activities.SearchPackage.SearchByCountry;
import com.example.covit_19updates.ApiUrlsPackage.APIUrls;
import com.example.covit_19updates.ModelsPacakges.Summary.SummuryModel;
import com.example.covit_19updates.NumberFormate.NumberFormatter;
import com.example.covit_19updates.ServicesPackages.SummaryService;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covit_19updates.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DashboardActivity extends AppCompatActivity {
    private static final String COUNTRY_NAME = "CountryName";
    private static final String PREF_NAME = "CountryCode";
    private TextView countryName;
    private TextView totalCases, totalDeaths, totalRecovered, newRecover,
            newDeaths, worldCases, worldDeath, worldRecover,
            newWorldDeath, newWorldRecovered;
    private String nameIs = null;
    private String countryCode;
    private ShimmerFrameLayout loading;
    private LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userInterface();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        countryName.setText(prefs.getString(COUNTRY_NAME, "Country Name"));

        nameIs = prefs.getString(COUNTRY_NAME, null);
        onAppBarPositionChange();
        countryCode = prefs.getString(PREF_NAME, null);
        Toast.makeText(this, countryCode, Toast.LENGTH_SHORT).show();
        APICall(countryCode);
    }

    private void APICall(String countryCode) {
        try {
            loading.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
            loading.startShimmer();
            @SuppressLint("SetTextI18n")
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    APIUrls.GET_SUMMARY,
                    response -> {
                        loading.setVisibility(View.GONE);
                        content.setVisibility(View.VISIBLE);
                        loading.stopShimmer();
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        SummuryModel summuryModel = gson.fromJson(response, SummuryModel.class);
                        if (SummaryService.addSummary(summuryModel)) {
                            totalCases.setText(NumberFormatter.formatNumber(SummaryService.getCountryStates(countryCode).get(0)
                                    .getTotalConfirmed()));
                            totalDeaths.setText(NumberFormatter.formatNumber(SummaryService.getCountryStates(countryCode).get(0)
                                    .getTotalDeaths()));
                            totalRecovered.setText(NumberFormatter.formatNumber(SummaryService.getCountryStates(countryCode).get(0)
                                    .getTotalRecovered()));
                            newRecover.setText(NumberFormatter.formatNumber(SummaryService.getCountryStates(countryCode).get(0)
                                    .getNewRecovered()));
                            newDeaths.setText(NumberFormatter.formatNumber(SummaryService.getCountryStates(countryCode).get(0)
                                    .getNewDeaths()));

                            newWorldDeath.setText(NumberFormatter.formatNumber(SummaryService.summuryModel.get(0).getGlobal().getNewDeaths()));
                            newWorldRecovered.setText(NumberFormatter.formatNumber(SummaryService.summuryModel.get(0).getGlobal().getNewRecovered()));
                            worldDeath.setText(NumberFormatter.formatNumber(SummaryService.summuryModel.get(0).getGlobal().getTotalDeaths()));
                            worldCases.setText(NumberFormatter.formatNumber(SummaryService.summuryModel.get(0).getGlobal().getTotalConfirmed()));
                            worldRecover.setText(NumberFormatter.formatNumber(SummaryService.summuryModel.get(0).getGlobal().getTotalRecovered()));
                        }
                    },
                    error -> {
                        loading.setVisibility(View.GONE);
                        content.setVisibility(View.GONE);
                        loading.stopShimmer();
                        Toast.makeText(this, "Server error: " + error, Toast.LENGTH_SHORT).show();
                    }
            );
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        } catch (Exception e) {
            Toast.makeText(this, "Exception: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void userInterface() {
        countryName = findViewById(R.id.countryName);
        totalCases = findViewById(R.id.totalCases);
        totalDeaths = findViewById(R.id.totalDeaths);
        totalRecovered = findViewById(R.id.totalRecovered);
        newRecover = findViewById(R.id.newTotalRecovered);
        newDeaths = findViewById(R.id.newTotalDeaths);
        loading = findViewById(R.id.loadingShimmer);
        content = findViewById(R.id.content);

        worldCases = findViewById(R.id.worldCasesCount);
        worldDeath = findViewById(R.id.worldDeathsCount);
        worldRecover = findViewById(R.id.worldRecoverCount);
        newWorldDeath = findViewById(R.id.newWorldDeathsCount);
        newWorldRecovered = findViewById(R.id.newWorldRecoverCount);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search) {
            startActivity(new Intent(this, SearchByCountry.class));
            Toast.makeText(this, "Make text", Toast.LENGTH_SHORT).show();
        } else {
            APICall(countryCode);
        }
        return super.onOptionsItemSelected(item);
    }

    private void onAppBarPositionChange() {
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if (nameIs != null) {
                        collapsingToolbarLayout.setTitle(nameIs);
                        collapsingToolbarLayout.setCollapsedTitleGravity(View.TEXT_ALIGNMENT_CENTER);
                    } else {
                        collapsingToolbarLayout.setTitle(" ");
                        collapsingToolbarLayout.setCollapsedTitleGravity(View.TEXT_ALIGNMENT_CENTER);
                    }
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}
