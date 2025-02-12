package com.sp.silvercloud;

import android.view.View;
import android.view.GestureDetector;
import android.view.MotionEvent;
import androidx.annotation.NonNull;

public class GestureListener implements GestureDetector.OnGestureListener {

    private HomeFragment homeFragment;

    public GestureListener(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        if (homeFragment.navigationView != null &&
                homeFragment.navigationView.getVisibility() == View.VISIBLE) {

            int[] location = new int[2];
            homeFragment.navigationView.getLocationOnScreen(location);
            int navViewX = location[0];
            int navViewY = location[1];

            int touchX = Math.round(e.getX());
            int touchY = Math.round(e.getY());

            if (touchX < navViewX || touchX > navViewX + homeFragment.navigationView.getWidth() ||
                    touchY < navViewY || touchY > navViewY + homeFragment.navigationView.getHeight()) {
                homeFragment.navigationView.setVisibility(View.GONE);
            }
        }
        return true;
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {
    }

    @Override
    public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
