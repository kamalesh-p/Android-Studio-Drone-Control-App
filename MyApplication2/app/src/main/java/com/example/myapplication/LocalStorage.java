package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

public class LocalStorage {
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor = null;
    public LocalStorage(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.editor = this.sharedPreferences.edit();
    }
    public void write(String keyString, String valueString){
        editor.putString(keyString, valueString);
        editor.commit();
    }
    public String read(String keyString, String defaultString){
        return sharedPreferences.getString(keyString, defaultString);
    }
    public String read(String keyString){
        return read(keyString, "\0");
    }
}
