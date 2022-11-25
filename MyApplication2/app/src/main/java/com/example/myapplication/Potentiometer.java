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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class Potentiometer extends AppCompatActivity {
    Button layout;
    UDP udp = new UDP(false);
    Joystick joystick = new Joystick();
    ToggleButton switchButton;

    String ipaddress;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potentiometer);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (this.getSupportActionBar() != null)
            this.getSupportActionBar().hide();

        LocalStorage localStorage = new LocalStorage(getSharedPreferences("Drone", MODE_PRIVATE));
        ipaddress = localStorage.read("ipaddress", "192.168.4.1");
        String port = localStorage.read("port", "4210");
        udp.change_ipaddress(ipaddress);
        udp.change_port(Integer.parseInt(port));

        switchButton = (ToggleButton) findViewById(R.id.toggleButton);

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

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    udp.send("-2"); // The toggle is enabled
                } else {
                    udp.send("-3"); // The toggle is disabled
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
        joystick.raise.init(R.id.joystick_raise_color, R.id.joystick_raise_white, R.id.joystick_raise_ball, R.id.joystick_raise_message);
        joystick.origin();
    }
}