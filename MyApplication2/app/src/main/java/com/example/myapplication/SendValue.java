package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SendValue extends AppCompatActivity {
    String ipaddress;
    String default_msg = "10";
    ToggleButton switchButton;

    EditText edit_text_all_motors;
    int edit_text_value_id_array[];
    EditText edit_text_value_array[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_value);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (this.getSupportActionBar() != null)
            this.getSupportActionBar().hide();

        // initialise layout.xml elements
        EditText edit_text_ipaddress = (EditText) findViewById(R.id.ipaddress);
        edit_text_value_id_array = new int[]{R.id.value0,R.id.value1,R.id.value2,R.id.value3};
        edit_text_value_array = new EditText[edit_text_value_id_array.length];
        String old_value_array[] = new String[edit_text_value_id_array.length];
        String alphabet_array[] = new String[]{"A","B","C","D"};
        for(int i=0; i<edit_text_value_id_array.length; i++){
            edit_text_value_array[i] = (EditText) findViewById(edit_text_value_id_array[i]);
            old_value_array[i] = "0";
        }
        edit_text_all_motors = (EditText) findViewById(R.id.all_motor_value);

        Button button_send = (Button) findViewById(R.id.send);
        switchButton = (ToggleButton) findViewById(R.id.toggleButton);
        // initialise java class
        LocalStorage localStorage = new LocalStorage(getSharedPreferences("Drone", MODE_PRIVATE));
        UDP udp = new UDP(true);

        ipaddress = localStorage.read("ipaddress", "192.168.4.1");
        String port = localStorage.read("port", "4210");
        udp.change_ipaddress(ipaddress);
        udp.change_port(Integer.parseInt(port));

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
        for(int i=0; i<edit_text_value_id_array.length; i++) {
            edit_text_value_array[i].setText(default_msg); // edit_text_value.setHint(default_msg);
            edit_text_value_array[i].setImeOptions(EditorInfo.IME_ACTION_DONE);
            edit_text_value_array[i].setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                    if(actionId == EditorInfo.IME_ACTION_DONE){
                        // Your action on done
                        button_send.performClick();
                        return false;
                    }
                    return false;
                }
            });
        }
        edit_text_all_motors.setText(default_msg);
        edit_text_all_motors.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edit_text_all_motors.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                func1();
            }
        });
        edit_text_all_motors.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    func1();
                }
            }
        });
        edit_text_all_motors.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    // Your action on done
                    func1();
                    udp.send(edit_text_all_motors.getText().toString()); //button_send.performClick();
                    return false;
                }
                return false;
            }
        });
        button_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(edit_text_value_array[0].getText().toString().equals(edit_text_value_array[1].getText().toString())
                        && edit_text_value_array[1].getText().toString().equals(edit_text_value_array[2].getText().toString())
                        && edit_text_value_array[2].getText().toString().equals(edit_text_value_array[3].getText().toString())){
                    udp.send(edit_text_value_array[0].getText().toString());
                }
                else{
                    for(int i=0; i< edit_text_value_array.length; i++){
                        String new_value = edit_text_value_array[i].getText().toString();
                        if (new_value.matches("")) {
                            new_value = default_msg;
                            edit_text_value_array[i].setText(default_msg);
                        }
                        if(new_value.equals(old_value_array[i]) == false){
                            String msg = alphabet_array[i] + new_value;
                            udp.send(msg);
                            old_value_array[i] = new_value;
                        }
                    }
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
                for(int i=0; i<edit_text_value_id_array.length; i++){
                    //edit_text_value_array[i].setText(default_msg); // edit_text_value.setHint(default_msg);
                    old_value_array[i] = "0";
                }
            }
        });
    }

    void func1(){
        String value = edit_text_all_motors.getText().toString();
        for(int i=0; i<edit_text_value_id_array.length; i++){
            edit_text_value_array[i].setText(value); // edit_text_value.setHint(default_msg);
        }
    }
}