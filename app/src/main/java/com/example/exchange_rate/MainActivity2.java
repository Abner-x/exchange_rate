package com.example.exchange_rate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity2 extends AppCompatActivity implements Runnable {
    Handler handler;
    EditText et1, et2, et3;

    private static final String TAG = "MainActivity2";

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Thread t = new Thread(this);
        t.start();
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 5) {
                    String str = (String) msg.obj;
                    Log.i(TAG, "handleMessage: getMessage msg = " + str);
                    et1.setText(str);
                }
                super.handleMessage(msg);
            }
        };

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

    @Override
    public void run() {
        /*URL url = null;
        Log.i(TAG, "run: ha");
        try {
            url = new URL("https://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();
            String html = inputStream2String(in);
            Log.i(TAG, "run: html=" + html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        String url = "https://www.usd-cny.com/bankofchina.htm";
        try {
            Document doc = Jsoup.connect(url).get();
            Log.i(TAG, "run: " + doc.title());
            Elements tables = doc.getElementsByTag("table");
            Element table5 = tables.get(0);
            Elements tds = doc.getElementsByTag("td");

            for (int i = 0; i < tds.size(); i += 6) {
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+1);
                String str1 = td1.text();
                String val = td2.text();
                Log.i(TAG, "run: " + str1 + "==>" + val);

                //float v = 100f / Float.parseFloat(val);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}