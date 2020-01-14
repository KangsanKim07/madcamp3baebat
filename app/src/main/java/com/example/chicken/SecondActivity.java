package com.example.chicken;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.ArrayList;


public class SecondActivity extends AppCompatActivity {
    private ArrayList<Second_order_info> order_array;
    private RecyclerView order;
    private LinearLayoutManager linearLayoutManager;
    private SecondAdapter adapter;
    public static Activity secondactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Button btn = (Button)findViewById(R.id.btn);
        secondactivity = SecondActivity.this;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        order_array = (ArrayList<Second_order_info>)intent.getExtras().getSerializable("order_array");
        order = (RecyclerView)findViewById(R.id.order);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        order.setLayoutManager(linearLayoutManager);
        adapter = new SecondAdapter(order_array);
        order.setAdapter(adapter);
        if(order_array.size() == 0){
            order.setBackgroundResource(R.drawable.ceo3);

        }

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ThirdActivity.class);
                intent.putExtra("order_array", order_array);
                startActivity(intent);
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                order_array.remove(viewHolder.getLayoutPosition());
                adapter.notifyItemRemoved(viewHolder.getLayoutPosition());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(order);

    }
}
