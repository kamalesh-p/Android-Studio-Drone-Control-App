package com.example.myapplication;

import android.os.StrictMode;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UDP {
    private int port = -1;
    private InetAddress address = null;
    private DatagramSocket serverSocket = null;
    private boolean is_initialised = false;
    private void init(){
        if(is_initialised){
            return;
        }
        is_initialised = true;
        // StrictMode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // UDP programming
        try {
            serverSocket = new DatagramSocket();
        } catch (IOException ex) {
            Log.i("ERROR", String.valueOf(ex));
        }
    }
    UDP(boolean initialise){
        if(initialise){
            is_initialised = true;
            init();
        }
        else{
            is_initialised = false;
        }
    }
    UDP(){
        is_initialised = true;
        init();
    }
    public void change_ipaddress(String ip_address) {
        init();
        // Get the address that we are going to connect to.
        try {
            address = InetAddress.getByName(ip_address);
        } catch (UnknownHostException ex) {
            Log.i("ERROR", String.valueOf(ex));
        }
    }

    public void change_port(int port) {
        init();
        this.port = port;
    }
    public void send(String msg) {
        init();
        // Create a packet that will contain the data
        // (in the form of bytes) and send it.
        DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),
                msg.getBytes().length, address, port);
        try {
            serverSocket.send(msgPacket);
            Log.i("DEBUG", "message sent: " + msg);
        } catch (Exception ex) {
            Log.i("ERROR", String.valueOf(ex));
        }
    }
}
