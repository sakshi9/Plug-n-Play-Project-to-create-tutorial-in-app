package com.example.sakshigupta.tutoriallibrary;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;

class SettingTextualInfo {

    private static final int INDEX_TEXT_START_X = 0;
    private static final int INDEX_TEXT_START_Y = 1;
    private static final int INDEX_TEXT_WIDTH = 2;

    private final TextPaint titlePaint;
    private final TextPaint textPaint;
    private final Context context;
    private final float padding;
    private final float actionBarOffset;

    private Layout.Alignment detailTextAlignment = Layout.Alignment.ALIGN_NORMAL;
    private Layout.Alignment titleTextAlignment = Layout.Alignment.ALIGN_NORMAL;
    private CharSequence mTitle, mDetails;
    private float[] mBestTextPosition = new float[3];
    private DynamicLayout mDynamicTitleLayout;
    private DynamicLayout mDynamicDetailLayout;
    private TextAppearanceSpan mTitleSpan;
    private TextAppearanceSpan mDetailSpan;
    private boolean hasRecalculated;

    @TutorialView.TextPosition
    private int forcedTextPosition = TutorialView.UNDEFINED;

    public SettingTextualInfo(Resources resources, Context context) {
        padding = resources.getDimension(R.dimen.text_padding);
        actionBarOffset = resources.getDimension(R.dimen.action_bar_offset);

        this.context = context;

        titlePaint = new TextPaint();
        titlePaint.setAntiAlias(true);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
    }

    public void draw(Canvas canvas) {
        if (shouldDrawText()) {
            float[] textPosition = getBestTextPosition();
            int width = Math.max(0, (int) mBestTextPosition[INDEX_TEXT_WIDTH]);

            if (!TextUtils.isEmpty(mTitle)) {
                canvas.save();
                if (hasRecalculated) {
                    mDynamicTitleLayout = new DynamicLayout(mTitle, titlePaint,
                                                            width, titleTextAlignment, 1.0f, 1.0f, true);
                }
                if (mDynamicTitleLayout != null) {
                    canvas.translate(textPosition[INDEX_TEXT_START_X], textPosition[INDEX_TEXT_START_Y]);
                    mDynamicTitleLayout.draw(canvas);
                    canvas.restore();
                }
            }

            if (!TextUtils.isEmpty(mDetails)) {
                canvas.save();
                if (hasRecalculated) {
                    mDynamicDetailLayout = new DynamicLayout(mDetails, textPaint,
                                                             width, detailTextAlignment, 1.2f, 1.0f, true);
                }
                float offsetForTitle = mDynamicTitleLayout != null ? mDynamicTitleLayout.getHeight() : 0;
                if (mDynamicDetailLayout != null) {
                    canvas.translate(textPosition[INDEX_TEXT_START_X], textPosition[INDEX_TEXT_START_Y] + offsetForTitle);
                    mDynamicDetailLayout.draw(canvas);
                    canvas.restore();
                }

            }
        }
        hasRecalculated = false;
    }

    public void setContentText(CharSequence details) {
        if (details != null) {
            SpannableString ssbDetail = new SpannableString(details);
            ssbDetail.setSpan(mDetailSpan, 0, ssbDetail.length(), 0);
            mDetails = ssbDetail;
        }
    }

    public void setContentTitle(CharSequence title) {
        if (title != null) {
            SpannableString ssbTitle = new SpannableString(title);
            ssbTitle.setSpan(mTitleSpan, 0, ssbTitle.length(), 0);
            mTitle = ssbTitle;
        }
    }

    /**
     * Calculates the best place to position text
     *  @param canvasW width of the screen
     * @param canvasH height of the screen
     * @param shouldCentreText
     * @param showcase
     */
    public void calculateTextPosition(int canvasW, int canvasH, boolean shouldCentreText, Rect showcase) {

        int[] areas = new int[4]; //left, top, right, bottom
        areas[TutorialView.LEFT_OF_SHOWCASE] = showcase.left * canvasH;
        areas[TutorialView.ABOVE_SHOWCASE] = showcase.top * canvasW;
        areas[TutorialView.RIGHT_OF_SHOWCASE] = (canvasW - showcase.right) * canvasH;
        areas[TutorialView.BELOW_SHOWCASE] = (canvasH - showcase.bottom) * canvasW;

        int largest = 0;
        for(int i = 1; i < areas.length; i++) {
            if(areas[i] > areas[largest])
                largest = i;
        }

        if (forcedTextPosition != TutorialView.UNDEFINED) {
            largest = forcedTextPosition;
        }

        // Position text in largest area
        switch(largest) {
            case TutorialView.LEFT_OF_SHOWCASE:
                mBestTextPosition[INDEX_TEXT_START_X] = padding;
                mBestTextPosition[INDEX_TEXT_START_Y] = padding;
                mBestTextPosition[INDEX_TEXT_WIDTH] = showcase.left - 2 * padding;
                break;
            case TutorialView.ABOVE_SHOWCASE:
                mBestTextPosition[INDEX_TEXT_START_X] = padding;
                mBestTextPosition[INDEX_TEXT_START_Y] = padding + actionBarOffset;
                mBestTextPosition[INDEX_TEXT_WIDTH] = canvasW - 2 * padding;
                break;
            case TutorialView.RIGHT_OF_SHOWCASE:
                mBestTextPosition[INDEX_TEXT_START_X] = showcase.right + padding;
                mBestTextPosition[INDEX_TEXT_START_Y] = padding;
                mBestTextPosition[INDEX_TEXT_WIDTH] = (canvasW - showcase.right) - 2 * padding;
                break;
            case TutorialView.BELOW_SHOWCASE:
                mBestTextPosition[INDEX_TEXT_START_X] = padding;
                mBestTextPosition[INDEX_TEXT_START_Y] = showcase.bottom + padding;
                mBestTextPosition[INDEX_TEXT_WIDTH] = canvasW - 2 * padding;
                break;
        }
        if(shouldCentreText) {
            // Center text vertically or horizontally
            switch(largest) {
                case TutorialView.LEFT_OF_SHOWCASE:
                case TutorialView.RIGHT_OF_SHOWCASE:
                    mBestTextPosition[INDEX_TEXT_START_Y] += canvasH / 4;
                    break;
                case TutorialView.ABOVE_SHOWCASE:
                case TutorialView.BELOW_SHOWCASE:
                    mBestTextPosition[INDEX_TEXT_WIDTH] /= 2;
                    mBestTextPosition[INDEX_TEXT_START_X] += canvasW / 4;
                    break;
            }
        } else {
            // As text is not centered add actionbar padding if the text is left or right
            switch(largest) {
                case TutorialView.LEFT_OF_SHOWCASE:
                case TutorialView.RIGHT_OF_SHOWCASE:
                    mBestTextPosition[INDEX_TEXT_START_Y] += actionBarOffset;
                    break;
            }
        }

        hasRecalculated = true;
    }

    public void setTitleStyling(int styleId) {
        mTitleSpan = new TextAppearanceSpan(this.context, styleId);
        setContentTitle(mTitle);
    }

    public void setDetailStyling(int styleId) {
        mDetailSpan = new TextAppearanceSpan(this.context, styleId);
        setContentText(mDetails);
    }

    public float[] getBestTextPosition() {
        return mBestTextPosition;
    }

    public boolean shouldDrawText() {
        return !TextUtils.isEmpty(mTitle) || !TextUtils.isEmpty(mDetails);
    }
}
