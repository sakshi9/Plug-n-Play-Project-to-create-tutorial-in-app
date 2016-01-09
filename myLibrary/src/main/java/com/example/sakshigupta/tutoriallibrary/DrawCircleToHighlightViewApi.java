package com.example.sakshigupta.tutoriallibrary;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public interface DrawCircleToHighlightViewApi {

    void drawShowcase(Bitmap buffer, float x, float y, float scaleMultiplier);

    int getShowcaseWidth();

    int getShowcaseHeight();

    void drawToCanvas(Canvas canvas, Bitmap bitmapBuffer);
}
