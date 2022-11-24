package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class JoystickControl extends AppCompatActivity {
    Button layout;
    UDP udp = new UDP(false);
    Joystick joystick = new Joystick();

    String ipaddress;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick_control);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (this.getSupportActionBar() != null)
            this.getSupportActionBar().hide();

        LocalStorage localStorage = new LocalStorage(getSharedPreferences("Drone", MODE_PRIVATE));
        ipaddress = localStorage.read("ipaddress", "192.168.4.1");
        String port = localStorage.read("port", "4210");
        udp.change_ipaddress(ipaddress);
        udp.change_port(Integer.parseInt(port));

        layout = (Button) findViewById(R.id.layout);
        ImageButton settings_btn = (ImageButton) findViewById(R.id.settings);
        EditText edit_text_ipaddress = (EditText) findViewById(R.id.ipaddress);
        TextView ipaddress_label = (TextView) findViewById(R.id.ipaddresslabel);

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (edit_text_ipaddress.getVisibility() == View.VISIBLE){
                    edit_text_ipaddress.setVisibility(View.INVISIBLE);
                    ipaddress_label.setVisibility(View.INVISIBLE);
                }
                joystick.myTouchAction(event);
                return false;
            }
        });

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_text_ipaddress.getVisibility() == View.VISIBLE){
                    edit_text_ipaddress.setVisibility(View.INVISIBLE);
                    ipaddress_label.setVisibility(View.INVISIBLE);
                }
                else{
                    edit_text_ipaddress.setVisibility(View.VISIBLE);
                    ipaddress_label.setVisibility(View.VISIBLE);
                }
            }
        });

        edit_text_ipaddress.setText(ipaddress); //edit_text_ipaddress.setHint(ipaddress);
        edit_text_ipaddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                ipaddress = String.valueOf(edit_text_ipaddress.getText());
                udp.change_ipaddress(ipaddress);
                localStorage.write("ipaddress", ipaddress);
            }
        });

        joystick.init(this);
        joystick.move.init(R.id.joystick_move_color, R.id.joystick_move_white, R.id.joystick_move_ball, R.id.joystick_move_message);
        joystick.raise.init(R.id.joystick_raise_color, R.id.joystick_raise_white, R.id.joystick_raise_ball, R.id.joystick_raise_message);
        joystick.origin();
    }
}