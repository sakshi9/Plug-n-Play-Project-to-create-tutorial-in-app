package com.example.sakshigupta.tutorial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.sakshigupta.tutoriallibrary.CreateTutorial;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = (Button)findViewById(R.id.startButton);

        int x = 50;
        int y = 60;
        String heading = "Guide To ShadowFax";
        String content = "Click on start to see insights of this application.";

        /*
               This tutorial will display only on first launch
         */
        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.sakshigupta.tutorial", Context.MODE_PRIVATE);
        if (prefs.getBoolean("firstLaunch", true)) {

            /*
              You need to pass context, coordinates of a particular view and textual information as string
             */

            CreateTutorial.makeTutorial(MainActivity.this, x, y, heading, content);
        }
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(MainActivity.this, LoginScreen.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
