package com.example.yunotes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.yunotes.R;

public class StartScreen extends AppCompatActivity {

    //Memberikan waktu loading otomatis 5 detik
    private int startScreen = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        //Membuat object event handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Method untuk memanggil activity "MainActivity"
                Intent intent = new Intent(StartScreen.this, MainActivity.class);

                //Fungsi untuk memulai intent
                startActivity(intent);

                //Fungsi selesai
                finish();
            }
            //Memanggil variabel
        }, startScreen);
    }
}