package com.example.chicken;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ThirdActivity extends AppCompatActivity {
    private ArrayList<Second_order_info> order_array;
    MediaPlayer hi, card,exno,howmuch,locaokwhat,menuokwhere,thx,time,pardon;
    private ArrayList<String> match_text;
    private static final int REQUEST_CODE = 1234;
    String[] anslist;
    private String daesa;
    private String location, menu, amount, pay, time1, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        ImageView gif = (ImageView)findViewById(R.id.gif);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gif);
        Glide.with(this).load(R.raw.loading).into(gif);
        hi = MediaPlayer.create(ThirdActivity.this, R.raw.hi);
        card = MediaPlayer.create(ThirdActivity.this, R.raw.card);
        exno =MediaPlayer.create(ThirdActivity.this, R.raw.exno);
        howmuch = MediaPlayer.create(ThirdActivity.this, R.raw.howmuch);
        locaokwhat = MediaPlayer.create(ThirdActivity.this, R.raw.locaokwhat);
        menuokwhere = MediaPlayer.create(ThirdActivity.this, R.raw.menuokwhere);
        thx = MediaPlayer.create(ThirdActivity.this, R.raw.thx);
        time = MediaPlayer.create(ThirdActivity.this, R.raw.time);
        pardon = MediaPlayer.create(ThirdActivity.this, R.raw.pardon);

        Intent intent = getIntent();
        order_array = (ArrayList<Second_order_info>)intent.getExtras().getSerializable("order_array");

        hi.start();
        new Waiter().execute();

        anslist = new String[] {"asdf", "asdf", "asdf", "asdf"};

    }
    class Waiter extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            while (hi.isPlaying()||card.isPlaying()||exno.isPlaying()||howmuch.isPlaying()||locaokwhat.isPlaying()||menuokwhere.isPlaying()
            ||menuokwhere.isPlaying()||pardon.isPlaying()||thx.isPlaying()||time.isPlaying()){
                try{Thread.sleep(500);}catch (Exception e){}
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(isConnected()){
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
                startActivityForResult(intent, REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            match_text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            try{
                String[] result = new DeepLearning1(ThirdActivity.this, MainActivity.komoran, MainActivity.retMap).getAnswer(match_text.get(0));
                for(int i=0;i<4;i++){
                    if(!(result[i].equals("asdf"))){
                        anslist[i] = result[i];
                    }
                }
                if(!(anslist[0].equals("asdf"))&&!(anslist[1].equals("asdf"))&&!(anslist[2].equals("asdf"))&&!(anslist[3].equals("asdf"))){
                    daesa = "thx";
                    Log.d("TTTTT", "-------------------------------" + daesa);
                }
                else if(!(anslist[0].equals("asdf"))&&!(anslist[1].equals("asdf"))&&!(anslist[2].equals("asdf"))&&(anslist[3].equals("asdf"))){
                    daesa = "howmany";
                    Log.d("TTTTT", "-------------------------------" + daesa);
                }
                else if((anslist[0].equals("asdf"))&&!(anslist[1].equals("asdf"))){
                    daesa = "locaokwhat";
                    Log.d("TTTTT", "-------------------------------" + daesa);
                }
                else if(!(anslist[0].equals("asdf"))&&(anslist[1].equals("asdf"))){
                    daesa = "menuokwhere";
                    Log.d("TTTTT", "-------------------------------" + daesa);
                }
                else if(!(anslist[0].equals("asdf"))&&!(anslist[1].equals("asdf"))&&(anslist[2].equals("asdf"))) {
                    if(anslist[3].equals("asdf")){
                        daesa = "howmany";
                        Log.d("TTTTT", "-------------------------------" + daesa);
                    }
                    else{
                        daesa = "card";
                        Log.d("TTTTT", "-------------------------------" + daesa);
                    }
                }
                else{
                    daesa = "pardon";
                    Log.d("TTTTT", "-------------------------------" + daesa);
                }
                speakCheckInBackground(daesa);
            }catch(IOException e){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public  boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net!=null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void speakCheckInBackground(String text) {
        Log.d("string" , "======================");
        if(text.equals("thx")){
            time.start();
            while (time.isPlaying()){
                try {
                    Thread.sleep(500);
                }catch (Exception e){}
            }
            thx.start();
            while (thx.isPlaying()){
                try {
                    Thread.sleep(500);
                }catch (Exception e){}
            }

            SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
            Calendar cal = Calendar.getInstance();
            String today = null;
            today = formatter.format(cal.getTime());
            Timestamp ts = Timestamp.valueOf(today);
            String ordertime = ts.toString();
            day = ordertime.split(" ")[0];
            time1 = ordertime.split(" ")[1].split(":")[0] +":"+ ordertime.split(" ")[1].split(":")[1];

            Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
            Second_order_info order = new Second_order_info(anslist[1], anslist[0], anslist[2], day+" "+time1, anslist[3]);
            location = order.getLocation(); menu = order.getMenu(); pay = order.getPay(); amount = order.getAmount();
            order_array.add(order);
            intent.putExtra("order_array", order_array);
            new HttpPostRequest().execute("http://192.249.19.252:1780/order");
            startActivity(intent);
            finish();
            SecondActivity secondActivity = (SecondActivity) SecondActivity.secondactivity;
            secondActivity.finish();
        }
        else if(text.equals("locaokwhat")){locaokwhat.start();}
        else if(text.equals("card")){card.start();}
        else if(text.equals("exno"))exno.start();
        else if(text.equals("howmany"))howmuch.start();
        else if(text.equals("menuokwhere"))menuokwhere.start();
        else if(text.equals("pardon"))pardon.start();
        else pardon.start();


        if(!(!(anslist[0].equals("asdf"))&&!(anslist[1].equals("asdf"))&&!(anslist[2].equals("asdf"))&&!(anslist[3].equals("asdf")))){
            new Waiter().execute();
        }

    }

    public class HttpPostRequest extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("location", location);
                jsonObject.accumulate("menu", menu);
                jsonObject.accumulate("amount", amount);
                jsonObject.accumulate("pay", pay);
                jsonObject.accumulate("time", day);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL("http://192.249.19.252:1780/order");
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송


                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }
}
