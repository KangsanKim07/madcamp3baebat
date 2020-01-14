package com.example.chicken;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Second_order_info> order_array = new ArrayList<Second_order_info>();
    public static Komoran komoran;
    String json;
    public static HashMap<String, float[]> retMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        komoran = new Komoran(DEFAULT_MODEL.LIGHT);
        loadJSONFromAsset();
        retMap = new Gson().fromJson(
                json, new TypeToken<HashMap<String, float[]>>() {}.getType()
        );

        Handler hand = new Handler();
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                i.putExtra("order_array", order_array);
                startActivity(i);
                finish();
            }
        }, 3500);
    }

    public void loadJSONFromAsset() {
        try {
            InputStream is = this.getAssets().open("embedding20.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        return;
    }

}
