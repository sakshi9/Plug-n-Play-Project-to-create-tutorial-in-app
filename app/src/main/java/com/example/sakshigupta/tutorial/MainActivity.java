package com.example.sakshigupta.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.sakshigupta.tutoriallibrary.CalculateCoordinates;
import com.example.sakshigupta.tutoriallibrary.CalculateCoordinatesApi;
import com.example.sakshigupta.tutoriallibrary.TutorialView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start = (Button) findViewById(R.id.startBtn);
        start.setOnClickListener(this);

        CalculateCoordinatesApi viewCoordinates = new CalculateCoordinates(R.id.startBtn, this);

        new TutorialView.Builder(this)
                .setTarget(viewCoordinates)
                .setContentTitle(R.string.heading)
                .build();

    }

    @Override
    public void onClick (View view){
        startActivity(new Intent(this, CreateTutorialView.class));
    }
}
