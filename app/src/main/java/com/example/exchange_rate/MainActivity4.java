package com.example.exchange_rate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity4 extends AppCompatActivity implements Runnable {

    Handler handler;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        GridView grid = findViewById(R.id.list);

        handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 5) {

                }
                super.handleMessage(msg);
            }
        };
        Thread t = new Thread(this);
        t.start();
    }

    @Override
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
    }
}

class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "myrate.db";
    public static final String TB_NAME = "tb_rates";



    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(@Nullable Context context) {
        this(context, DB_NAME,  null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TB_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,CURNAME TEXT,CURRATE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

class RateManger{
    private DBHelper dbHelper;
    private String tbName;

    public RateManger(Context context) {
        dbHelper = new DBHelper(context);
        tbName = DBHelper.TB_NAME;
    }
}
class RateItem {
    private int id;
    private String curName;
    private String curRate;

    public RateItem() {
        super();
        curName = "";
        curRate = "";
    }
    public RateItem(String curName, String curRate) {
        super();
        this.curName = curName;
        this.curRate = curRate;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getCurRate() {
        return curRate;
    }
    public void setCurRate(String curRate) {
        this.curRate = curRate;
    }
}

