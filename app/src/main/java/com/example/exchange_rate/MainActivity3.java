package com.example.exchange_rate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity3 extends ListActivity implements Runnable {

    Handler handler;



    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main3);
        handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 5) {
                    ArrayList<HashMap<String,String>>  l = (ArrayList<HashMap<String, String>>)msg.obj;
                    SimpleAdapter listAdapter = new SimpleAdapter(MainActivity3.this,l,
                            R.layout.list_item,new String[]{ "f","r"},new int[]{R.id.ForeignCurrency,R.id.rate} );
                   /* ListAdapter adapter = new ArrayAdapter<String>(MainActivity3.this, android.R.layout.simple_list_item_1, list);
                    setListAdapter(adapter);*/
                   setListAdapter(listAdapter);
                }
                super.handleMessage(msg);
            }
        };
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        Message msg = handler.obtainMessage(5);
        ArrayList<HashMap<String, String>> l = new ArrayList<>();
        String url = "https://www.usd-cny.com/bankofchina.htm";

        /*
        * 记录数据
        * */
        SharedPreferences sp = getSharedPreferences("rateData1", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        /*
        * 记录当前日期是否为首次访问
        * */
        SharedPreferences sp1 = getSharedPreferences("rateData2", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sp1.edit();

        Date date = new Date();
        String today = date.toString().substring(0, 10);
        editor.remove(today);

        if (sp1.getInt(today,0) == 0) {
            editor1.putInt(today,1);
            editor1.apply();
            try {
                Document doc = Jsoup.connect(url).get();
                Elements tables = doc.getElementsByTag("table");
                Element table5 = tables.get(0);
                Elements tds = doc.getElementsByTag("td");


                for (int i = 0; i < tds.size(); i += 6) {
                    HashMap<String, String> map = new HashMap<>();
                    Element td1 = tds.get(i);
                    Element td2 = tds.get(i + 1);
                    String str1 = td1.text();
                    String val = td2.text();
                    String s = str1 + "==>" + val;
                    map.put("f", str1);
                    map.put("r", val);
                    editor.putString(str1, val);
                    editor.apply();
                    l.add(map);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Map<String, String> map = (Map<String, String>) sp.getAll();
            for (String x : map.keySet()) {
                HashMap<String, String> map1 = new HashMap<>();
                map1.put("f", x);
                map1.put("r", map.get(x));
                System.out.println(x);
                l.add(map1);
            }
        }
        msg.obj = l;
        handler.sendMessage(msg);
        //update();
    }

    /*private void update() {
        Message msg = handler.obtainMessage(5);
        String url = "https://www.usd-cny.com/bankofchina.htm";
        try {
            Document doc = Jsoup.connect(url).get();
            Elements tables = doc.getElementsByTag("table");
            Element table5 = tables.get(0);
            Elements tds = doc.getElementsByTag("td");
            ArrayList<HashMap<String, String>> l = new ArrayList<>();

            for (int i = 0; i < tds.size(); i += 6) {
                HashMap<String, String> map = new HashMap<>();
                Element td1 = tds.get(i);
                Element td2 = tds.get(i + 1);
                String str1 = td1.text();
                String val = td2.text();
                String s = str1 + "==>" + val;
                map.put("f", str1);
                map.put("r", val);
                l.add(map);
            }

            msg.obj = l;
            handler.sendMessage(msg);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}


