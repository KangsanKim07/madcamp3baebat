package com.example.chicken;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class AdminActivity extends Activity {
    private LineChart lineChart;
    private LineChart lineChart2;
    private List<Entry> entries = new ArrayList<>();
    private List<Entry> entries2 = new ArrayList<>();
    private ArrayList<Second_order_info> order_array = new ArrayList<Second_order_info>();
    private RecyclerView order;
    private LinearLayoutManager linearLayoutManager;
    private SecondAdapter adapter;
    LineData lineData;
    LineData lineData2;
    HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> map2 = new HashMap<Integer, Integer>();
    String year;
    int month_order_num;
    InputMethodManager imm, imm2, imm3;
    String month;
    String day;
    ArrayList<Integer> mostorder = new ArrayList<Integer>();
    int mostorderposition;
    int leastorderposition;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        for(int i = 0; i<4; i++) mostorder.add(0);
        EditText editText = (EditText) findViewById(R.id.year);
        year = editText.getText().toString();
        Button button = (Button)findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = editText.getText().toString();
                TextView ytitle = findViewById(R.id.ytitle);
                ytitle.setText(year+".");
                HttpGetRequest request = new HttpGetRequest();
                request.execute();
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(button.getWindowToken(), 0);
            }
        });

        lineChart = (LineChart) findViewById(R.id.chart);
        HttpGetRequest request = new HttpGetRequest();
        request.execute();
        EditText month_input = (EditText) findViewById(R.id.month);
        Button month1 = (Button) findViewById(R.id.button3);
        month1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month = month_input.getText().toString();
                TextView mtitle = findViewById(R.id.mtitle);
                mtitle.setText(month);
                lineChart2 = (LineChart) findViewById(R.id.chart2);
                MonthHttpGetRequest request1 = new MonthHttpGetRequest();
                request1.execute();
                imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(month1.getWindowToken(), 0);
            }
        });
        month = month_input.getText().toString();
        lineChart2 = findViewById(R.id.chart2);
        MonthHttpGetRequest request1 = new MonthHttpGetRequest();
        request1.execute();


        Button button1 = findViewById(R.id.button4);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.dday);
                day = et.getText().toString();
                DayGetRequest request2 = new DayGetRequest();
                request2.execute();
                imm3 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(button1.getWindowToken(), 0);
            }
        });
        day = "01";
//        DayGetRequest request2 = new DayGetRequest();
//        request2.execute();
    }

    // 웹서버에서 사용자 수강과목 JSONArray 데이터 가져와주는 클래스
    public class HttpGetRequest extends AsyncTask<Void, Void, String> {

        static final String REQUEST_METHOD = "GET";
        static final int READ_TIMEOUT = 15000;
        static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(Void... params){
            String op;
            String inputLine;

            try {
                // connect to the server
                URL myUrl = new URL("http://192.249.19.252:1780/order");
                HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.connect();

                // get the string from the input stream
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                op = stringBuilder.toString();

            } catch(IOException e) {
                e.printStackTrace();
                op = "error";
            }

            return op;
        }

        protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                //서버에 해당 id 찾아서 result 받아오고 파싱
                JSONArray jsonArray = new JSONArray(result);
                map.clear();
                for (int i = 0; i <jsonArray.length(); i++){
                    String time = jsonArray.getJSONObject(i).getString("time"); //yyyy-mm-dd
                    time = time.split("-")[0]+time.split("-")[1]; //yyyymm
                    int timekey = Integer.parseInt(time);
                    if(map.containsKey(timekey)){
                        int num = map.get(timekey);
                        num++;
                        map.remove(timekey);
                        map.put(timekey, num);
                    }
                    else{
                        map.put(timekey, 1);
                    }
                }
                TreeMap<Integer, Integer> tm = new TreeMap<Integer, Integer>(map);
                Iterator<Integer> iterator = tm.keySet().iterator();
                entries.clear();
                while(iterator.hasNext()){
                    Integer ki = iterator.next();
                    String kia = ki.toString().substring(0,4);
                    if(kia.equals(year)){
                        entries.add(new Entry(ki, map.get(ki)));
                    }
                }

            } catch (JSONException e){
                //핸들해줘요
            }

            LineDataSet lineDataSet = new LineDataSet(entries, "주문 건수");
            lineDataSet.setLineWidth(2);
            lineDataSet.setCircleRadius(6);
            lineDataSet.setCircleColor(Color.parseColor("#2AC1BC"));
            lineDataSet.setCircleColorHole(Color.rgb(42,193,188));
            lineDataSet.setColor(Color.parseColor("#2AC1BC"));
            lineDataSet.setDrawCircleHole(true);
            lineDataSet.setDrawCircles(true);
            lineDataSet.setDrawHorizontalHighlightIndicator(false);
            lineDataSet.setDrawHighlightIndicators(false);
            lineDataSet.setDrawValues(false);

            lineData = new LineData(lineDataSet);
            Log.d("t", "------------------------"+lineData);
            lineChart.setData(lineData);
            lineChart.getLegend().setEnabled(false);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextColor(Color.BLACK);
            xAxis.setDrawLabels(false);
            xAxis.enableGridDashedLine(8, 24, 0);

            YAxis yLAxis = lineChart.getAxisLeft();
            yLAxis.setTextColor(Color.BLACK);

            YAxis yRAxis = lineChart.getAxisRight();
            yRAxis.setDrawLabels(false);
            yRAxis.setDrawAxisLine(false);
            yRAxis.setDrawGridLines(false);

            Description description = new Description();
            description.setText("");

            lineChart.setDoubleTapToZoomEnabled(false);
            lineChart.setDrawGridBackground(false);
            lineChart.setDescription(description);
            lineChart.animateY(1000, Easing.EasingOption.EaseInQuad);
            lineChart.invalidate();

        }
    }

    // 웹서버에서 사용자 수강과목 JSONArray 데이터 가져와주는 클래스
    public class MonthHttpGetRequest extends AsyncTask<Void, Void, String> {

        static final String REQUEST_METHOD = "GET";
        static final int READ_TIMEOUT = 15000;
        static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(Void... params){
            String op;
            String inputLine;

            try {
                // connect to the server
                URL myUrl = new URL("http://192.249.19.252:1780/order");
                HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.connect();
                Log.d("tasdasda", "--------------------|||||||||||||||||||||||----");
                // get the string from the input stream
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                op = stringBuilder.toString();

            } catch(IOException e) {
                e.printStackTrace();
                op = "error";
            }

            return op;
        }

        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                //서버에 해당 id 찾아서 result 받아오고 파싱
                JSONArray jsonArray = new JSONArray(result);
                month_order_num = 0;
                mostorder.clear();
                for(int i = 0; i<4; i++) mostorder.add(0);;
                map2.clear();
                for (int i = 0; i <jsonArray.length(); i++){
                    String time = jsonArray.getJSONObject(i).getString("time"); //yyyy-mm-dd
                    time = time.split("-")[0]+time.split("-")[1]+time.split("-")[2]; //yyyymmdd
                    int timekey = Integer.parseInt(time);
                    if(map2.containsKey(timekey)){
                        int num = map2.get(timekey);
                        num++;
                        map2.remove(timekey);
                        map2.put(timekey,num);
                    }
                    else{
                        map2.put(timekey, 1);
                    }
                    if(time.substring(0,4).equals(year) && time.substring(4,6).equals(month)){
                        month_order_num++;
                        String menuorder = jsonArray.getJSONObject(i).getString("menu");
                        if (menuorder.equals("후라이드")) {
                            int m = mostorder.get(0);
                            m = m + 1;
                            mostorder.remove(0);
                            mostorder.add(0, m);
                        } else if (menuorder.equals("양념")) {
                            int m = mostorder.get(1);
                            m = m + 1;
                            mostorder.remove(1);
                            mostorder.add(1, m);
                        } else if (menuorder.equals("간장")) {
                            int m = mostorder.get(2);
                            m = m + 1;
                            mostorder.remove(2);
                            mostorder.add(2, m);
                        } else if (menuorder.equals("즐거운 매운맛")) {
                            int m = mostorder.get(3);
                            m = m + 1;
                            mostorder.remove(3);
                            mostorder.add(3, m);
                        }
                        Log.d("MONSTWST", "==========================="+mostorder.get(0)+mostorder.get(1)+mostorder.get(2)+mostorder.get(3));
                    }
                }
                TreeMap<Integer, Integer> tm = new TreeMap<Integer, Integer>(map2);
                Iterator<Integer> iterator = tm.keySet().iterator();
                entries2.clear();
                while(iterator.hasNext()){
                    Integer ki = iterator.next();
                    String kia = ki.toString().substring(0,4);
                    String kiaa = ki.toString().substring(4,6);
                    String kib = ki.toString().substring(6,8);
                    if(kia.equals(year)&&kiaa.equals(month)){
                        entries2.add(new Entry(Integer.parseInt(kib), map2.get(ki)));
                    }
                }

                TextView textView = findViewById(R.id.textView13);
                TextView textView2 = findViewById(R.id.textView17);
                TextView textView4 = findViewById(R.id.textView30);
                textView.setText(month);
                textView2.setText(month);
                textView4.setText(month);
                TextView textView1 = findViewById(R.id.textView12);
                textView1.setText(Integer.toString(month_order_num));
                int max = 0; int min = 999;
                for (int i = 0; i < 4 ; i++){
                    if(mostorder.get(i) > max) {
                        mostorderposition = i;
                        max = mostorder.get(i);
                    }
                    if(mostorder.get(i) < min){
                        leastorderposition = i;
                        min = mostorder.get(i);
                    }
                }
                TextView textView12 = findViewById(R.id.most);
                TextView textView3 = findViewById(R.id.least);
                if(mostorderposition == 0) textView12.setText("후라이드 치킨");
                else if(mostorderposition == 1) textView12.setText("양념 치킨");
                else if(mostorderposition == 2) textView12.setText("간장 치킨");
                else if(mostorderposition == 3) textView12.setText("즐거운 매운맛");

                if(leastorderposition == 0) textView3.setText("후라이드 치킨");
                else if(leastorderposition == 1) textView3.setText("양념 치킨");
                else if(leastorderposition == 2) textView3.setText("간장 치킨");
                else if(leastorderposition == 3) textView3.setText("즐거운 매운맛");

            } catch (JSONException e){
                //핸들해줘요
            }

            LineDataSet lineDataSet1 = new LineDataSet(entries2, "");
            lineDataSet1.setLineWidth(2);
            lineDataSet1.setCircleRadius(6);
            lineDataSet1.setCircleColor(Color.parseColor("#2AC1BC"));
            lineDataSet1.setCircleColorHole(Color.rgb(42,193,188));
            lineDataSet1.setColor(Color.parseColor("#2AC1BC"));
            lineDataSet1.setDrawCircleHole(true);
            lineDataSet1.setDrawCircles(true);
            lineDataSet1.setDrawHorizontalHighlightIndicator(false);
            lineDataSet1.setDrawHighlightIndicators(false);
            lineDataSet1.setDrawValues(false);

            lineData2 = new LineData(lineDataSet1);
            lineChart2.setData(lineData2);
            lineChart2.getLegend().setEnabled(false);

            XAxis xAxis = lineChart2.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextColor(Color.BLACK);
            xAxis.setDrawLabels(false);
            xAxis.enableGridDashedLine(8, 24, 0);

            YAxis yLAxis = lineChart2.getAxisLeft();
            yLAxis.setTextColor(Color.BLACK);

            YAxis yRAxis = lineChart2.getAxisRight();
            yRAxis.setDrawLabels(false);
            yRAxis.setDrawAxisLine(false);
            yRAxis.setDrawGridLines(false);

            Description description = new Description();
            description.setText("");
            lineChart2.setDoubleTapToZoomEnabled(false);
            lineChart2.setDrawGridBackground(false);
            lineChart2.setDescription(description);
            lineChart2.animateY(1000, Easing.EasingOption.EaseInQuad);
            lineChart2.invalidate();
        }
    }

    public class DayGetRequest extends AsyncTask<Void, Void, String> {

        static final String REQUEST_METHOD = "GET";
        static final int READ_TIMEOUT = 15000;
        static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(Void... params){
            String op;
            String inputLine;

            try {
                // connect to the server
                URL myUrl = new URL("http://192.249.19.252:1780/order/"+year+"-"+month+"-"+day);
                HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.connect();

                // get the string from the input stream
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                op = stringBuilder.toString();

            } catch(IOException e) {
                e.printStackTrace();
                op = "error";
            }

            return op;
        }

        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                //서버에 해당 id 찾아서 result 받아오고 파싱
                JSONArray jsonArray = new JSONArray(result);
                order_array.clear();
                Log.d("ASDSADAS", "_________________________"+jsonArray.length());
                for (int i = 0 ; i < jsonArray.length(); i++){
                    Second_order_info order2 = new Second_order_info(jsonArray.getJSONObject(i).getString("location")
                    ,jsonArray.getJSONObject(i).getString("menu"),jsonArray.getJSONObject(i).getString("pay"),
                            jsonArray.getJSONObject(i).getString("time"), jsonArray.getJSONObject(i).getString("amount"));
                    order_array.add(order2);
                    Log.d("ASDSADAS", "_++++++++++++++++++++++++++++++++++++++++++" + order_array.size());
                    Log.d("ASDSADAS", "_________________________"+jsonArray.length()+"____________"+i);
                }
                order = (RecyclerView)findViewById(R.id.reday);
                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                order.setLayoutManager(linearLayoutManager);
                adapter = new SecondAdapter(order_array);
                order.setAdapter(adapter);
            } catch (JSONException e){
                //핸들해줘요
            }
        }
    }

}
