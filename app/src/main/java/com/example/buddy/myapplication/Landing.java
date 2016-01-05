package com.example.buddy.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Landing extends AppCompatActivity {
int count=0;
    ViewPager dealspager;
    SharedPreferences sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sh = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        if(sh.getInt("checklog",0)==1)
        {
            Intent in=new Intent(Landing.this,MainActivity.class);
            finish();
            startActivity(in);

        }
        setContentView(R.layout.activity_landing);
        ViewPagerAdapter adapter = new ViewPagerAdapter(Landing.this,3,getApplicationContext());
    dealspager = (ViewPager) findViewById(R.id.landing);
        adapter.notifyDataSetChanged();
        dealspager.setAdapter(adapter);
        dealspager.setCurrentItem(0);

        // Timer for auto sliding
       Timer timer  = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count <= 2) {
                            dealspager.setCurrentItem(count);
                            count++;
                        } else {
                            count = 0;
                            dealspager.setCurrentItem(count);
                        }
                    }
                });
            }
        }, 5000, 5000);
    }
}
