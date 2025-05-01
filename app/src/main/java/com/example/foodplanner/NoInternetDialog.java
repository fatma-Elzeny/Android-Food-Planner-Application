package com.example.foodplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

public class NoInternetDialog {
    public static void show(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_no_internet, null);

        AlertDialog dialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setView(view)
                .setCancelable(true)
                .create();

        dialog.show();
    }
}
