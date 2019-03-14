package com.njust.major;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.major.SerialPort;
import com.njust.major.SCM.DrinkGate;
import com.njust.major.util.Util;

import static com.njust.major.VMApplication.counter;
import static com.njust.major.VMApplication.keyTest;

public class KeyTestReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // 收到广播开启服务
        Log.i("happy","KeyTestReceiver接受到了");
        Util.WriteFile("KeyTestReceiver接受到了");
        keyTest = true;
        counter = Integer.parseInt(intent.getStringExtra("counter"));
    }
}
