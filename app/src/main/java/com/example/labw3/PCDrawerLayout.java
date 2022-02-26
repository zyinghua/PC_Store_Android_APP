package com.example.labw3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class PCDrawerLayout extends DrawerLayout {
    private boolean drawerFactor = false;

    public PCDrawerLayout(@NonNull Context context) {
        super(context);
    }

    public PCDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            return super.onTouchEvent(ev);
        else
            return false;
    }
}
