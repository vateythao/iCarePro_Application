package com.vat.icare.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.vat.icare.R;

public class LoaderDialog {
    Context context;
    AlertDialog alertDialog;

    public LoaderDialog(Context context) {
        this.context = context;
    }

    public void show() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.customLoader);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_loader, null);
        alert.setView(view);
        alert.setCancelable(false);
        alertDialog =alert.create();
        alertDialog.show();
    }

    public void dismiss() {
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
}
