package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TestingDrone extends AppCompatActivity{

    ImageButton b1, b2, b3, b4;

    int b1x1, b1x2, b1y1, b1y2;

    private static TextView xcordview;
    private static TextView ycordview;
    private static TextView buttonIndicator;
    private static RelativeLayout touchview;
    private static int defaultStates[];
    private final static int[] STATE_PRESSED = {
            android.R.attr.state_pressed,
            android.R.attr.state_focused
                    | android.R.attr.state_enabled };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_drone);

        xcordview = (TextView) findViewById(R.id.textView4);
        ycordview = (TextView) findViewById(R.id.textView3);
        buttonIndicator = (TextView) findViewById(R.id.button_indicator);
        touchview = (RelativeLayout) findViewById(R.id.relativelayout);

        b1 = (ImageButton) findViewById(R.id.button1);
        b2 = (ImageButton) findViewById(R.id.button2);
        b3 = (ImageButton) findViewById(R.id.button3);
        b4 = (ImageButton) findViewById(R.id.button4);
        defaultStates = b1.getBackground().getState();

        Drawable arrow = getResources().getDrawable(R.drawable.arrow);
        b1.setImageDrawable(arrow);
        b2.setImageDrawable(arrow);
        b3.setImageDrawable(arrow);
        b4.setImageDrawable(arrow);

    }
    @SuppressLint("ClickableViewAccessibility")
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        touchview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //onTouchs(v, event);
                return true;
            }

        });

        for (int i = 0; i < touchview.getChildCount(); i++) {
            View current = touchview.getChildAt(i);
            if (current instanceof ImageButton) {
                ImageButton b = (ImageButton) current;
                b.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        onTouchs(v, event);
                        return true;
                    }
                });
            }
        }
    }

    static void onTouchs(View v, MotionEvent event){
        int x = (int) event.getX();
        int y = (int) event.getY();

        xcordview.setText(String.valueOf(x));
        ycordview.setText(String.valueOf(y));

        for (int i = 0; i < touchview.getChildCount(); i++) {
            View current = touchview.getChildAt(i);
            if (current instanceof ImageButton) {
                ImageButton b = (ImageButton) current;

                if (!isPointWithin(x, y, b.getLeft(), b.getRight(), b.getTop(), b.getBottom())) {
                    b.getBackground().setState(defaultStates);
                } else {
                    b.getBackground().setState(STATE_PRESSED);
                    buttonIndicator.setText(String.valueOf(i));
                }

            }
        }
    }

    static boolean isPointWithin(int x, int y, int x1, int x2, int y1, int y2) {
        return (x <= x2 && x >= x1 && y <= y2 && y >= y1);
    }
}