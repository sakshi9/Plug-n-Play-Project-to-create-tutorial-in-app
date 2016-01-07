package com.example.sakshigupta.tutoriallibrary;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by sakshigupta on 07/01/16.
 */
public class ContentInTutorial extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_screen);

        Bundle b = getIntent().getExtras();
        String headingStr = b.getString("heading");
        String contentStr = b.getString("content");
        int x_coordinate = b.getInt("X Coordinate");
        int y_coordinate = b.getInt("Y Coordinate");

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relative_layout);
        TextView heading = (TextView)findViewById(R.id.heading);
        TextView content = (TextView)findViewById(R.id.content);
        ImageView arrow = (ImageView)findViewById(R.id.arrow);
        heading.setText(headingStr);
        content.setText(contentStr);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(293, 294);
        params.leftMargin = x_coordinate;
        params.topMargin = y_coordinate;
        if(arrow.getParent()!=null)
            ((ViewGroup)arrow.getParent()).removeView(arrow);
        rl.addView(arrow, params);
    }

    public void GotIt(View v) {
        finish();
    }
}
