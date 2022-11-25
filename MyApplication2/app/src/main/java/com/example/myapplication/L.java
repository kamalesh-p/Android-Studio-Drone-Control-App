package com.example.myapplication;

import android.util.Log;

public class L {
    static void l(String msg) {
        if (msg == "NULL") {
            Log.e("kamal", "msg");
        } else {
            Log.e("kamal", msg);
        }
    }
    static void l(int msg) {
        l(String.valueOf(msg));
    }
    static void l(double msg) {
        l(String.valueOf(msg));
    }
    static void l() {
        l("NULL");
    }
}
