package com.escuelapp.view;

import android.app.ProgressDialog;
import android.content.Context;

import com.escuelapp.R;

/**
 * Created by alan on 10/1/16.
 */
public class CustomProgressDialog extends ProgressDialog {

    public CustomProgressDialog(Context context) {
        super(context);
        setMessage(context.getString(R.string.loading));
        setIndeterminate(true);
    }
}
