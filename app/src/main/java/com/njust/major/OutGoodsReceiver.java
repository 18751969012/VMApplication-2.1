package com.njust.major;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.njust.major.service.OutGoodsService;

public class OutGoodsReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // 收到广播开启服务
        Log.i("happy","OutGoodsReceiver接受到了");
        Intent service = new Intent(context, OutGoodsService.class);
        try{
            context.stopService(service);
        }catch (Exception e){
            e.printStackTrace();
        }
        context.startService(service);
    }
}
