package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class TestingDrone extends AppCompatActivity{
    /*
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
    void l(double msg){
        l(String.valueOf(msg));
    }
    void l(){
        l("NULL");
    }
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_drone);

    }
}