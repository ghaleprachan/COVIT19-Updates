package com.example.covit_19updates.NumberFormate;

import android.annotation.SuppressLint;

public class NumberFormatter {
    @SuppressLint("DefaultLocale")
    public static String formatNumber(int number) {
        return String.format("%,d", number);
    }
}
