package com.example.sakshigupta.tutoriallibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sakshigupta on 08/01/16.
 */
public class CreateTutorial {

    /*
    this function will be called from your application including required parameters to see an interactive tutorial
     */
    public static void makeTutorial(AppCompatActivity m_activity, int x, int y, String heading, String content) {

        Bundle bundle = new Bundle();
        bundle.putInt("X Coordinate", x);
        bundle.putInt("Y Coordinate", y);
        bundle.putString("heading", heading);
        bundle.putString("content", content);
        Intent intent = new Intent(m_activity, ContentInTutorial.class);
        intent.putExtras(bundle);
        m_activity.startActivity(intent);
    }
}
