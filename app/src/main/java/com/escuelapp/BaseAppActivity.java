package com.escuelapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.escuelapp.view.CustomProgressDialog;

public class BaseAppActivity extends AppCompatActivity {

    public CustomProgressDialog progressDialog;

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new CustomProgressDialog(this);
        }
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
