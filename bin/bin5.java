package com.example.myapplication;

import static android.app.PendingIntent.getActivity;

import static java.sql.Types.NULL;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

public class JoystickControl extends AppCompatActivity {
    void l(String msg){
        if(msg == "NULL") {
            Log.e("kamal", "msg");
        }
        else{
            Log.e("kamal", msg);
        }
    }
    void l(int msg){
        l(String.valueOf(msg));
    }
    void l(){
        l("NULL");
    }
    class Joystick {
        private class Move {
            ImageView color;
            ImageView white;
            RelativeLayout ball;
            boolean isActive = false;

            void init(int color, int white, int ball) {
                this.color = (ImageView) findViewById(color);
                this.white = (ImageView) findViewById(white);
                this.ball = (RelativeLayout) findViewById(ball);
            }

            void origin() {
                ball.setX((float) (color.getWidth() / 2) + color.getX() - (float) (ball.getWidth() / 2));
                ball.setY((float) (color.getHeight() / 2) + color.getY() - (float) (ball.getHeight() / 2));
            }

            void setXY(int x, int y) {
                if (x > move.color.getWidth() + move.color.getLeft()) {
                    ball.setX(move.color.getWidth() + move.color.getLeft() - ball.getWidth() / 2);
                } else if (x < move.color.getLeft()) {
                    ball.setX(move.color.getLeft() - ball.getWidth() / 2);
                } else {
                    ball.setX(x - ball.getWidth() / 2);
                }
                if (y > move.color.getHeight() + move.color.getHeight() / 4) {
                    ball.setY(move.color.getHeight() + move.color.getHeight() / 4);
                } else if (y < move.color.getTop() - move.color.getHeight() / 4) {
                    ball.setY(move.color.getTop() - move.color.getHeight() / 4);
                } else {
                    ball.setY(y);
                }
            }
        }

        private class Raise {
            ImageView color;
            ImageView white;
            RelativeLayout ball;
            boolean isActive = false;

            void init(int color, int white, int ball) {
                this.color = (ImageView) findViewById(color);
                this.white = (ImageView) findViewById(white);
                this.ball = (RelativeLayout) findViewById(ball);
            }

            void origin() {
                ball.setX((float) (color.getWidth() / 2) + color.getX() - (float) (ball.getWidth() / 2));
                originY();
            }

            void originY() {
                ball.setY((float) (color.getHeight() / 2) + color.getY() - (float) (ball.getHeight() / 2));
            }

            void setY(int y) {
                if(y < white.getY()){
                    // top most
                    ball.setY(white.getY());
                }
                else if(y > white.getBottom() - ball.getHeight()){
                    // bottom most
                    ball.setY(white.getBottom() - ball.getHeight());
                }
                else{
                    // center
                    // y >= white.getY() && y <= white.getBottom() - ball.getHeight()
                    ball.setY(y);
                }
            }
        }

        Move move = new Move();
        Raise raise = new Raise();

        void myTouchAction(int x, int y) {
            if (x >= raise.color.getLeft() && x <= raise.color.getRight()) {
                raise.setY(y);
            }
            else if(x < layout.getWidth()/2){
                move.setXY(x, y);
            }
            else{

            }
        }
    }
    Button layout;
    TextView textView;
    Joystick joystick = new Joystick();

    UDP udp = new UDP(false);

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

        layout = (Button) findViewById(R.id.layout);
        textView = (TextView) findViewById(R.id.message);
        ImageButton settings_btn = (ImageButton) findViewById(R.id.settings);

        joystick.move.init(R.id.joystick_move_color, R.id.joystick_move_white, R.id.joystick_move_ball);
        joystick.raise.init(R.id.joystick_raise_color, R.id.joystick_raise_white, R.id.joystick_raise_ball);

        joystick.move.origin();
        joystick.raise.origin();

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int action = MotionEventCompat.getActionMasked(event);
                //int index = MotionEventCompat.getActionIndex(event);
                int xPos = -1;
                int yPos = -1;
                //Log.e("kamal",actionToString(action));
                String msg = "";
                for(int i=0; i < event.getPointerCount();i++){
                    xPos = (int) MotionEventCompat.getX(event, i);
                    yPos = (int) MotionEventCompat.getY(event, i);
                    msg += "[x = " + String.valueOf(xPos) + ", y = " + String.valueOf(yPos) + "]";
                    joystick.myTouchAction(xPos, yPos);
                }
                textView.setText(msg);

                return false;
            }
        });
    }

    // Given an action int, returns a string description
    public static String actionToString(int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return "Down";
            case MotionEvent.ACTION_MOVE:
                return "Move";
            case MotionEvent.ACTION_POINTER_DOWN:
                return "Pointer Down";
            case MotionEvent.ACTION_UP:
                return "Up";
            case MotionEvent.ACTION_POINTER_UP:
                return "Pointer Up";
            case MotionEvent.ACTION_OUTSIDE:
                return "Outside";
            case MotionEvent.ACTION_CANCEL:
                return "Cancel";
        }
        return "";
    }

    static boolean isPointWithin(int x, int y, int x1, int x2, int y1, int y2) {
        return (x <= x2 && x >= x1 && y <= y2 && y >= y1);
    }
}
