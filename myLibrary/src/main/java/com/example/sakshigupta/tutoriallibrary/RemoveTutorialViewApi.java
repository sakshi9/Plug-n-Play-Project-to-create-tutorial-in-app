package com.example.sakshigupta.tutoriallibrary;

import android.view.View;

/**
 * Created by sakshigupta on 08/01/16.
 */

interface RemoveTutorialViewApi {

    void fadeOutView(View target, AnimationEndListener listener);

    interface AnimationEndListener {
        void onAnimationEnd();
    }
}
