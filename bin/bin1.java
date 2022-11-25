package com.example.myapplication;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class JoystickControl extends AppCompatActivity{
    ConstraintLayout body;

    ImageButton front;
    ImageButton back;
    ImageButton left;
    ImageButton right;
    ImageButton maintain;

    ImageButton top;
    ImageButton down;
    ImageButton middle;

    TextView textView;
    UDP udp = new UDP(false);
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick_control);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (this.getSupportActionBar() != null)
            this.getSupportActionBar().hide();

        LocalStorage localStorage = new LocalStorage(getSharedPreferences("Drone", MODE_PRIVATE));
        String ipaddress = localStorage.read("ipaddress", "192.168.4.1");
        String port = localStorage.read("port", "4210");

        udp.change_ipaddress(ipaddress);
        udp.change_port(Integer.parseInt(port));

        body = (ConstraintLayout) findViewById(R.id.layout);

        front = (ImageButton) findViewById(R.id.front);
        back = (ImageButton) findViewById(R.id.back);
        left = (ImageButton) findViewById(R.id.left);
        right = (ImageButton) findViewById(R.id.right);
        maintain = (ImageButton) findViewById(R.id.maintain);

        top = (ImageButton) findViewById(R.id.top);
        down = (ImageButton) findViewById(R.id.down);
        middle = (ImageButton) findViewById(R.id.middle);

        ImageButton backspace_btn = (ImageButton) findViewById(R.id.backspace);
        ImageButton settings_btn = (ImageButton) findViewById(R.id.settings);

        textView = (TextView) findViewById(R.id.message);

        Drawable backspace = getResources().getDrawable(R.drawable.backspace);
        Drawable settings = getResources().getDrawable(R.drawable.settings);
        Drawable arrow = getResources().getDrawable(R.drawable.arrow);
        Drawable circle = getResources().getDrawable(R.drawable.circle);

        backspace_btn.setImageDrawable(backspace);
        settings_btn.setImageDrawable(settings);

        front.setImageDrawable(arrow);
        back.setImageDrawable(arrow);
        left.setImageDrawable(arrow);
        right.setImageDrawable(arrow);
        maintain.setImageDrawable(circle);

        top.setImageDrawable(arrow);
        down.setImageDrawable(arrow);
        middle.setImageDrawable(circle);

        front.setOnTouchListener(
            new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    onTouchMyFunction(v, event);

                    return false;
                }
            }
        );
        //back.setOnTouchListener(this);
        //left.setOnTouchListener(this);
        //right.setOnTouchListener(this);
        //maintain.setOnTouchListener(this);

        //top.setOnTouchListener(this);
        //down.setOnTouchListener(this);
        //middle.setOnTouchListener(this);
    }

    static boolean isPointWithin(int x, int y, int x1, int x2, int y1, int y2) {
        return (x <= x2 && x >= x1 && y <= y2 && y >= y1);
    }

    public boolean onTouchMyFunction(View view, MotionEvent event) {
        Log.i("kamal","_______________________START______________________");
        int x = (int) event.getX();
        int y = (int) event.getY();
        Log.i("kamal", String.valueOf(x));
        Log.i("kamal", String.valueOf(y));
        for (int i = 0; i < body.getChildCount(); i++) {
            View current = body.getChildAt(i);
            if (current instanceof ImageButton) {
                ImageButton b = (ImageButton) current;
                if (isPointWithin(x, y, b.getLeft(), b.getRight(), b.getTop(), b.getBottom())) {
                    Log.i("kamal", "inside");
                    Log.i("kamal", String.valueOf(b.getLeft()));
                    Log.i("kamal", String.valueOf(b.getRight()));
                    Log.i("kamal", String.valueOf(b.getTop()));
                    Log.i("kamal", String.valueOf(b.getBottom()));
                    Log.i("kamal", "button id: " + String.valueOf(b.getId()));
                    Log.i("kamal", "front id: " + String.valueOf(front.getId()));
                    if (b == front) {
                        udp.send("front");
                        textView.setText("front");
                    }
                    else if (b == back) {
                        udp.send("back");
                        textView.setText("back");
                    }
                    else if (b == left) {
                        udp.send("left");
                        textView.setText("left");
                    }
                    else if (b == right) {
                        udp.send("right");
                        textView.setText("right");
                    }
                    else if (b == maintain) {
                        udp.send("maintain");
                        textView.setText("maintain");
                    }

                    else if (b == top) {
                        udp.send("up");
                        textView.setText("up");
                    }
                    else if (b == down) {
                        udp.send("down");
                        textView.setText("down");
                    }
                    else if (b == middle) {
                        udp.send("middle");
                        textView.setText("middle");
                    }
                    else{
                        Log.i("kamal","wasted");
                    }
                }
            }
        }
        return false;
    }
}
