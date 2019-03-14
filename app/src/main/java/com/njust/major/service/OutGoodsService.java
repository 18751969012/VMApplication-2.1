package com.njust.major.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.njust.major.VMApplication;
import com.njust.major.thread.TransactionThread;
import com.njust.major.util.Util;

public class OutGoodsService extends Service {
    @Override
    public void onCreate() {
        Util.WriteFile("OutGoodsService_onCreate");
        Log.i("happy","OutGoodsService_onCreate");
        SystemClock.sleep(500);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        VMApplication.Flag = true;
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
