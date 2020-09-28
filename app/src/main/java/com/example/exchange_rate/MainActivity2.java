package com.example.exchange_rate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity2 extends AppCompatActivity {

    EditText et1,et2,et3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        float d_rate = intent.getFloatExtra("d_rate_key", 0.0f);
        float e_rate = intent.getFloatExtra("e_rate_key", 0.0f);
        float w_rate = intent.getFloatExtra("w_rate_key", 0.0f);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et1.setText(String.valueOf(d_rate));
        et2.setText(String.valueOf(e_rate));
        et3.setText(String.valueOf(w_rate));
    }

    public void returnFarther(View view) {
        SharedPreferences sp = getSharedPreferences("rateData", Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("d_rate", Float.parseFloat(et1.getText().toString()));
        editor.putFloat("e_rate", Float.parseFloat(et2.getText().toString()));
        editor.putFloat("w_rate", Float.parseFloat(et3.getText().toString()));
        editor.apply();

        Intent data = getIntent();
        data.putExtra("d_rate", Float.parseFloat(et1.getText().toString()));
        data.putExtra("e_rate", Float.parseFloat(et2.getText().toString()));
        data.putExtra("w_rate", Float.parseFloat(et3.getText().toString()));
        setResult(2, data);
        finish();
    }
}