package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    public void openNewActivity(int activity) {
        Intent intent = null;
        switch (activity) {
            case -1:
                intent = new Intent(this, TestingDrone.class);
                break;
            case 1:
                intent = new Intent(this, SendMessage.class);
                break;
            case 2:
                intent = new Intent(this, JoystickControl.class);
                break;
            case 3:
                intent = new Intent(this, Potentiometer.class);
                break;
            case 4:
                intent = new Intent(this, SendValue.class);
                break;
            default:
                return;
        }
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openNewActivity(4);

        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Drawable drawable = getResources().getDrawable(R.drawable.drone);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);

        imageView.setImageDrawable(drawable);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openNewActivity(1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openNewActivity(2);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openNewActivity(3);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openNewActivity(4);
            }
        });
    }
}