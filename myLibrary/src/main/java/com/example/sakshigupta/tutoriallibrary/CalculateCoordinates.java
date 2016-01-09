package com.example.sakshigupta.tutoriallibrary;

import android.app.Activity;
import android.graphics.Point;
import android.view.View;

/**
 * Created by sakshigupta on 08/01/16.
 */
public class CalculateCoordinates implements CalculateCoordinatesApi {
    private final View mView;

    public CalculateCoordinates(int viewId, Activity activity) {
        mView = activity.findViewById(viewId);
    }

    @Override
    public Point getPoint() {
        int[] location = new int[2];
        mView.getLocationInWindow(location);
        int x = location[0] + mView.getWidth() / 2;
        int y = location[1] + mView.getHeight() / 2;
        return new Point(x, y);
    }
}
