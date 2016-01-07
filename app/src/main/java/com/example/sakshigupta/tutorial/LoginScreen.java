package com.example.sakshigupta.tutorial;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sakshigupta.tutoriallibrary.CreateTutorial;

/**
 * Created by sakshigupta on 08/01/16.
 */
public class LoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        Button login = (Button)findViewById(R.id.sign_in_button);

      //   int x = (int)login.getX();
       //  int y = (int)login.getY();

        int x = 500;
        int y = 500;

        String heading = "Login Screen";
        String content = "Enter your login details using your mobile number and password.";

        final SharedPreferences prefs = this.getSharedPreferences(
                "com.example.sakshigupta.tutorial", Context.MODE_PRIVATE);
        if (prefs.getBoolean("firstLaunch", true)) {
            prefs.edit().putBoolean("firstLaunch", false).commit();
            CreateTutorial.makeTutorial(LoginScreen.this, x, y, heading, content);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginScreen.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
