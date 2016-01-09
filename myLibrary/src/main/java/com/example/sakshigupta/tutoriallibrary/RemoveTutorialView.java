package com.example.sakshigupta.tutoriallibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by sakshigupta on 08/01/16.
 */
@TargetApi(VERSION_CODES.HONEYCOMB)
class RemoveTutorialView implements RemoveTutorialViewApi {

    private static final String ALPHA = "alpha";
    private static final float INVISIBLE = 0f;

    private final AccelerateDecelerateInterpolator interpolator;

    public RemoveTutorialView() {
        interpolator = new AccelerateDecelerateInterpolator();
    }

    @Override
    public void fadeOutView(View target, final AnimationEndListener listener) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(target, ALPHA, INVISIBLE);
        oa.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animator) {
                listener.onAnimationEnd();
            }

        });
        oa.start();
    }

}
