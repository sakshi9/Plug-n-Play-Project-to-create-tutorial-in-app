package com.example.sakshigupta.tutoriallibrary;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;


/**
 * Created by sakshigupta on 08/01/16.
 */
public class TutorialView extends RelativeLayout implements TutorialViewApi, View.OnTouchListener{

    private SettingTextualInfo setTextInfo;
    private DrawCircleToHighlightViewApi showcaseDrawer;
    private RemoveTutorialViewApi removeTutorialView;
    private final CalculateArea calculateArea;
    private ImageView handImage;

    public static final int UNDEFINED = -1;
    public static final int LEFT_OF_SHOWCASE = 0;
    public static final int RIGHT_OF_SHOWCASE = 2;
    public static final int ABOVE_SHOWCASE = 1;
    public static final int BELOW_SHOWCASE = 3;

    private Bitmap bitmapBuffer;
    private final int[] positionInWindow = new int[2];

    private boolean hasAlteredText = false;
    private boolean hasNoTarget = false;
    private boolean shouldCentreText;

    private int showcaseX = -1;
    private int showcaseY = -1;
    private float scaleMultiplier = 1f;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({UNDEFINED, LEFT_OF_SHOWCASE, RIGHT_OF_SHOWCASE, ABOVE_SHOWCASE, BELOW_SHOWCASE})
    public @interface TextPosition {
    }

    protected TutorialView(Context context) {
        this(context, null);
    }

    protected TutorialView(Context context, AttributeSet attrs) {
        super(context, attrs);

        removeTutorialView = new RemoveTutorialView();
        calculateArea = new CalculateArea();

        final TypedArray styled = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.ShowcaseView, R.attr.showcaseViewStyle,
                        R.style.ShowcaseView);

        showcaseDrawer = new DrawCircleTOHighlightView(getResources(), context.getTheme());
        setTextInfo = new SettingTextualInfo(getResources(), getContext());

        updateStyle(styled, false);

        setOnTouchListener(this);
    }

    void setShowcasePosition(Point point) {
        setShowcasePosition(point.x, point.y);
    }


    void setShowcasePosition(int x, int y) {
        getLocationInWindow(positionInWindow);
        showcaseX = x - positionInWindow[0];
        showcaseY = y - positionInWindow[1];
        recalculateText();
        invalidate();
    }

    public void setTarget(final CalculateCoordinatesApi target) {
        setShowcase(target);
    }

    public void setShowcase(final CalculateCoordinatesApi target) {
        postDelayed(new Runnable() {
            @Override
            public void run() {

                    if (getMeasuredHeight() > 0 && getMeasuredWidth() > 0) {
                        updateBitmap();
                    }

                    Point targetPoint = target.getPoint();
                    if (targetPoint != null) {
                        hasNoTarget = false;
                        setShowcasePosition(targetPoint);
                    } else {
                        hasNoTarget = true;
                        invalidate();
                    }
            }
        }, 100);
    }

    private void updateBitmap() {
        if (bitmapBuffer == null || haveBoundsChanged()) {
            if (bitmapBuffer != null) {
                bitmapBuffer.recycle();
            }
            bitmapBuffer = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        }
    }

    private void updateStyle(TypedArray styled, boolean invalidate) {

        int titleTextAppearance = styled.getResourceId(R.styleable.ShowcaseView_sv_titleTextAppearance,
                R.style.TextAppearance_ShowcaseView_Title);
        int detailTextAppearance = styled.getResourceId(R.styleable.ShowcaseView_sv_detailTextAppearance,
                R.style.TextAppearance_ShowcaseView_Detail);

        styled.recycle();

        setTextInfo.setTitleStyling(titleTextAppearance);
        setTextInfo.setDetailStyling(detailTextAppearance);
        hasAlteredText = true;

        if (invalidate) {
            invalidate();
        }
    }

    private boolean haveBoundsChanged() {
        return getMeasuredWidth() != bitmapBuffer.getWidth() ||
                getMeasuredHeight() != bitmapBuffer.getHeight();
    }

    public boolean hasShowcaseView() {
        return (showcaseX != 1000000 && showcaseY != 1000000) && !hasNoTarget;
    }

    private void recalculateText() {
        boolean recalculatedCling = calculateArea.calculateShowcaseRect(showcaseX, showcaseY, showcaseDrawer);
        boolean recalculateText = recalculatedCling;
        if (recalculateText) {
            Rect rect = hasShowcaseView() ? calculateArea.getShowcaseRect() : new Rect();
            setTextInfo.calculateTextPosition(getMeasuredWidth(), getMeasuredHeight(), shouldCentreText, rect);
        }
        hasAlteredText = false;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (showcaseX < 0 || showcaseY < 0 || bitmapBuffer == null) {
            super.dispatchDraw(canvas);
            return;
        }
        if (!hasNoTarget) {
            showcaseDrawer.drawShowcase(bitmapBuffer, showcaseX, showcaseY, scaleMultiplier);
            showcaseDrawer.drawToCanvas(canvas, bitmapBuffer);
        }
        setTextInfo.draw(canvas);
        super.dispatchDraw(canvas);
    }

    @Override
    public void hide() {
        removeTutorialView.fadeOutView(
                this, new RemoveTutorialViewApi.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        setVisibility(View.GONE);
                        if (bitmapBuffer != null && !bitmapBuffer.isRecycled()) {
                            bitmapBuffer.recycle();
                            bitmapBuffer = null;
                        }
                    }
                });
    }

    @Override
    public void show() {
        if (getMeasuredHeight() > 0 && getMeasuredWidth() > 0) {
            if (bitmapBuffer == null || haveBoundsChanged()) {
                if (bitmapBuffer != null) {
                    bitmapBuffer.recycle();
                }
                bitmapBuffer = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            }
        }
    }

    private static void createTutorialView(TutorialView tutorialView, ViewGroup parent, int parentIndex) {
        if(tutorialView.getParent()!=null) {
            ((ViewGroup) tutorialView.getParent()).removeView(tutorialView);
        }
        parent.addView(tutorialView, parentIndex);
            tutorialView.show();
    }

    @Override
    public void setContentTitle(CharSequence title) {
        setTextInfo.setContentTitle(title);
    }

    @Override
    public void setContentText(CharSequence text) {
        setTextInfo.setContentText(text);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.hide();
        return true;
    }

    public static class Builder {

        TutorialView tutorialView;
        Activity m_activity;
        private ViewGroup parent;
        private int parentIndex;

        public Builder(Activity activity) {
            this.m_activity = activity;
            this.tutorialView = new TutorialView(activity);
            this.tutorialView.setTarget(CalculateCoordinatesApi.NONE);
            this.parent = (ViewGroup) activity.findViewById(android.R.id.content);
            this.parentIndex = parent.getChildCount();
        }

        public TutorialView build() {
            createTutorialView(tutorialView, parent, parentIndex);
            return tutorialView;
        }

        public Builder setTarget(CalculateCoordinatesApi target) {
            tutorialView.setTarget(target);
            return this;
        }

        public Builder setContentTitle(int resId) {
            return setContentTitle(m_activity.getString(resId));
        }

        public Builder setContentTitle(CharSequence title) {
            tutorialView.setContentTitle(title);
            return this;
        }

        public Builder setContentText(int resId) {
            return setContentText(m_activity.getString(resId));
        }

        public Builder setContentText(CharSequence text) {
            tutorialView.setContentText(text);
            return this;
        }
    }
}
