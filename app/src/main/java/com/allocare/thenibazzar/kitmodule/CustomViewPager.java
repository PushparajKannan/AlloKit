package com.allocare.thenibazzar.kitmodule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

// Description : This is a ViewPager in which we can enable and disable horizontal scrolling by calling setScrollEnable() method
public class CustomViewPager extends ViewPager {

    private boolean scrollEnable = true;            // horizontal scrolling

    public CustomViewPager(Context ctx){            // public so that can be accessed outside the package
        super(ctx);
    }

    public CustomViewPager(Context ctx, AttributeSet attrs){            // constructor so that this view can also be accessed from xml layout
        super(ctx, attrs);
    }

    public void setScrollEnable(boolean enable){
        scrollEnable = enable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(scrollEnable)
            return super.onTouchEvent(ev);      // always returns true
        else
            return false;                       // return false to disable horizontal scrolling/swiping i.e. disable touch events
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(scrollEnable)
            return super.onInterceptTouchEvent(ev);
        else
            return false;
    }
}