package com.example.sakshigupta.tutorial;

import android.app.Activity;
import android.os.Bundle;

import com.example.sakshigupta.tutoriallibrary.CalculateCoordinatesApi;
import com.example.sakshigupta.tutoriallibrary.TutorialView;
import com.example.sakshigupta.tutoriallibrary.CalculateCoordinates;

/**
 * Created by sakshigupta on 09/01/16.
 */
public class CreateTutorialView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_view);

        CalculateCoordinatesApi viewCoordinates = new CalculateCoordinates(R.id.button, this);

        new TutorialView.Builder(this)
                .setTarget(viewCoordinates)
                .setContentTitle(R.string.title)
                .setContentText(R.string.content)
                .build();
    }
}
