package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by negocios on 11/05/16.
 */
public class FloatWindowService extends Service {

    private WindowManager windowManager = null;

    private LinearLayout linearLayout = null;
    private View mFloatingView;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.activity_2, null);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setBackgroundColor(Color.WHITE);
        linearLayout.setLayoutParams(layoutParams);

        TextView textView = new TextView(this);
        textView.setText("This is a floating window");
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams layoutParamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
       // linearLayout.setBackgroundColor(Color.argb(66, 255, 0, 0));
        textView.setLayoutParams(layoutParamsText);


        linearLayout.addView(textView);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,500, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        params.x = 0;
        params.y = 0;
        params.gravity = Gravity.TOP | Gravity.TOP;
        windowManager.addView(mFloatingView, params);
        mFloatingView.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatedParams = params;
            int x,y;
            float touchX,touchY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x= updatedParams.x;
                        y=updatedParams.y;
                        touchX = motionEvent.getRawX();
                        touchY = motionEvent.getRawY();
                        break;
                        case MotionEvent.ACTION_MOVE:
                            updatedParams.x = (int)(x+(motionEvent.getRawX() - touchX));
                            updatedParams.y = (int)(y+(motionEvent.getRawY() - touchY));
                            windowManager.updateViewLayout(mFloatingView,updatedParams);
                            default:break;
                }


                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) windowManager.removeView(mFloatingView);
    }
}
