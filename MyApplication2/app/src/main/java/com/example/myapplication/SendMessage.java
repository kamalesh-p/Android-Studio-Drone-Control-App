package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SendMessage extends AppCompatActivity {
    String ipaddress, port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // initialise layout.xml elements
        EditText edit_text_ipaddress = (EditText) findViewById(R.id.ipaddress);
        EditText edit_text_port = (EditText) findViewById(R.id.port);
        EditText edit_text_message = (EditText) findViewById(R.id.message);
        Button button_send = (Button) findViewById(R.id.send);
        // initialise java class
        LocalStorage localStorage = new LocalStorage(getSharedPreferences("Drone", MODE_PRIVATE));
        UDP udp = new UDP();

        ipaddress = localStorage.read("ipaddress", "192.168.4.1");
        port = localStorage.read("port", "4210");
        udp.change_ipaddress(ipaddress);
        udp.change_port(Integer.parseInt(port));

        String default_msg = "Hello World";
        edit_text_ipaddress.setText(ipaddress); //edit_text_ipaddress.setHint(ipaddress);
        edit_text_port.setText(port); //edit_text_port.setHint(port);
        edit_text_message.setHint(default_msg);

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

        edit_text_port.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                port = String.valueOf(edit_text_port.getText());
                udp.change_port(Integer.parseInt(port));
                localStorage.write("port", port);
            }
        });

        button_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String msg = String.valueOf(edit_text_message.getText());
                if (msg.matches("")) {
                    msg = default_msg;
                }
                udp.send(msg);
            }
        });
    }
}