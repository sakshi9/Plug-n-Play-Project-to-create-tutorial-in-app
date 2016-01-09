package com.example.sakshigupta.tutoriallibrary;

import android.graphics.Point;

/**
 * Created by sakshigupta on 08/01/16.
 */
public interface CalculateCoordinatesApi {

    CalculateCoordinatesApi NONE = new CalculateCoordinatesApi() {
        @Override
        public Point getPoint() {
            return new Point(1000000, 1000000);
        }
    };

    Point getPoint();
}
