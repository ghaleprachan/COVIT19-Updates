package com.example.covit_19updates.Activities.SearchPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covit_19updates.Activities.ChooseCountryPackage.SelectCountryActivity;
import com.example.covit_19updates.Activities.HomePage.DashboardActivity;
import com.example.covit_19updates.ApiUrlsPackage.APIUrls;
import com.example.covit_19updates.HideKeyboard.HideKeyboard;
import com.example.covit_19updates.ModelsPacakges.CountryNameModel;
import com.example.covit_19updates.NumberFormate.NumberFormatter;
import com.example.covit_19updates.R;
import com.example.covit_19updates.ServicesPackages.CountryServices;
import com.example.covit_19updates.ServicesPackages.SummaryService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SearchByCountry extends AppCompatActivity {
    private AutoCompleteTextView searchText;
    private RelativeLayout toolbar;
    private ImageView loading;
    private ScrollView scrollView;
    private ImageView back, clearText;

    private TextView countryName, info;
    private TextView totalCases, totalDeaths, totalRecovered, newRecover, newDeaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_country);
        userInterface();
        if (CountryServices.countryList.size() == 0) {
            countryListAPICall();
        } else {
            toolbar.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
            setUpCountryAsType();
        }
        setOnSearch();
        onBackBtnClick();
        textWatcher();
        clearText.setVisibility(View.GONE);
        onClearClick();
    }

    private void onClearClick() {
        clearText.setOnClickListener(v -> searchText.setText(null));
    }

    private void textWatcher() {
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchText.getText().toString().isEmpty() || searchText.getText().toString().length() == 0) {
                    scrollView.setVisibility(View.GONE);
                    clearText.setVisibility(View.GONE);
                } else {
                    clearText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void onBackBtnClick() {
        back.setOnClickListener(v -> onBackPressed());
    }

    private void setOnSearch() {
        searchText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                HideKeyboard.hideKeyboard(this);
                performSearch();
                return true;
            }
            return false;
        });
    }

    @SuppressLint("SetTextI18n")
    private void performSearch() {
        try {
            if (!searchText.getText().toString().isEmpty()) {
                countryName.setText(searchText.getText().toString());
                countryName.setTextColor(Color.BLACK);
                info.setTextColor(Color.BLACK);
                scrollView.setVisibility(View.VISIBLE);
                String countryCode = CountryServices.getSelectedCountryDetails(searchText.getText().toString()).get(0).getSlug();
                Toast.makeText(this, SummaryService.getCountryStates(countryCode).get(0)
                        .getTotalConfirmed().toString(), Toast.LENGTH_SHORT).show();
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
            }

        } catch (Exception ex) {
            scrollView.setVisibility(View.GONE);
            Toast.makeText(this, "Country Not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpCountryAsType() {
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, CountryServices.getAllCountry());
        searchText.setAdapter(countryAdapter);
    }

    private void userInterface() {
        searchText = findViewById(R.id.searchHere);
        toolbar = findViewById(R.id.toolbar);
        loading = findViewById(R.id.loadingImage);
        scrollView = findViewById(R.id.scrollView);

        countryName = findViewById(R.id.countryName);
        totalCases = findViewById(R.id.totalCases);
        totalDeaths = findViewById(R.id.totalDeaths);
        totalRecovered = findViewById(R.id.totalRecovered);
        newRecover = findViewById(R.id.newTotalRecovered);
        newDeaths = findViewById(R.id.newTotalDeaths);

        back = findViewById(R.id.backBtn);
        clearText = findViewById(R.id.clear_text);
        info = findViewById(R.id.info);
    }

    private void countryListAPICall() {
        try {
            AnimationDrawable animationDrawable = (AnimationDrawable) loading.getDrawable();
            animationDrawable.start();
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    APIUrls.GET_COUNTRIES,
                    response -> {
                        try {
                            ArrayList<CountryNameModel> countryListResponse = new Gson().fromJson(response, new TypeToken<List<CountryNameModel>>() {
                            }.getType());
                            if (CountryServices.addCountries(countryListResponse)) {
                                toolbar.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.GONE);
                                animationDrawable.stop();
                                setUpCountryAsType();
                            }
                        } catch (Exception ex) {
                            Toast.makeText(this, "Failed to get country names: " + ex, Toast.LENGTH_SHORT).show();
                        }
                    }, error -> {
                toolbar.setVisibility(View.VISIBLE);
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
}
