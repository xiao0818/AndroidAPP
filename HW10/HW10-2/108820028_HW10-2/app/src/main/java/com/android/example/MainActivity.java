package com.android.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private EditText mUrlEditText;
    private TextView mResultTextView;
    private String spinnerLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUrlEditText = (EditText) findViewById(R.id.url_edit_text);
        mResultTextView = (TextView) findViewById(R.id.result_text_view);

        // ... Rest of onCreate code ...
        // Create the spinner.
        Spinner spinner = findViewById(R.id.url_head_spinner);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }
        // Create ArrayAdapter using the string array and default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.url_head, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }
        // ... End of onCreate code ...
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Interface callback for when any spinner item is selected.
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spinnerLabel = adapterView.getItemAtPosition(i).toString();
    }

    // Interface callback for when no spinner item is selected.
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing.
    }

    public void getPageSource(View view) {
        String queryString = spinnerLabel + mUrlEditText.getText().toString();
//        displayToast(queryString);
        new FetchWeb(mResultTextView).execute(queryString);
    }
}