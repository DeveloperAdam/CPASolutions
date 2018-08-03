package com.techease.cpasolutions.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.techease.cpasolutions.R;

public class MainActivity extends AppCompatActivity {

    String token;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((AppCompatActivity) this).getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token=sharedPreferences.getString("token","");

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (token.equals(""))
                    {
                        startActivity(new Intent(MainActivity.this,FullscreenActivity.class));
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(MainActivity.this,NavigationActivity.class));
                        finish();
                    }

                }
            }
        };
        timer.start();
    }
}
