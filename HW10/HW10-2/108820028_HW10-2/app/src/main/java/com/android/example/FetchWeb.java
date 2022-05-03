package com.android.example;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class FetchWeb extends AsyncTask<String, Void, String> {
    private WeakReference<TextView> mResultTextView;

    FetchWeb(TextView resultTextView) {
        this.mResultTextView = new WeakReference<>(resultTextView);
    }

    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.getWebInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            mResultTextView.get().setText(s);
        } catch (Exception e) {
            mResultTextView.get().setText("Check your internet connection and try again.");
        }
    }
}