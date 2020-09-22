package com.example.exchange_rate;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText inp;
    Button btn_d;
    Button btn_e;
    Button btn_w;
    Button btn_c;
    Float d_rate_key;
    Float e_rate_key;
    Float w_rate_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inp = findViewById(R.id.editText);
        btn_d = findViewById(R.id.button);
        btn_e = findViewById(R.id.button2);
        btn_w = findViewById(R.id.button3);
        btn_c = findViewById(R.id.button4);
        d_rate_key = 0.1437f;
        e_rate_key = 0.1256f;
        w_rate_key = 171.3421f;
    }

    public void open(View view) {
        Intent main = new Intent(this, MainActivity2.class);
        main.putExtra("d_rate_key", d_rate_key);
        main.putExtra("e_rate_key", e_rate_key);
        main.putExtra("w_rate_key", w_rate_key);

        startActivityForResult(main,1);
    }

    public void dollar(View view) {

        if (inp.getText().toString() == null) {
            Toast.makeText(this, "请输入数字", Toast.LENGTH_LONG);
        }
        if (view.getId() == R.id.button) {
            rate(d_rate_key);
        } else if (view.getId() == R.id.button2) {
            rate(e_rate_key);
        } else if (view.getId() == R.id.button3) {
            rate(w_rate_key);
        }
    }

    public void rate(double r) {
        String yuan = inp.getText().toString();
        double num = Double.parseDouble(yuan);
        double res;
        res = num * r;
        inp.setText(String.valueOf(res));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            d_rate_key = data.getFloatExtra("d_rate",0.0f);
            e_rate_key = data.getFloatExtra("e_rate",0.0f);
            w_rate_key = data.getFloatExtra("w_rate",0.0f);
        }
    }
}