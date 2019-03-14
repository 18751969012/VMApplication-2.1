package com.njust.major;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.njust.major.service.VMService;

public class StartReceive extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		// 收到广播开启服务
		Intent service = new Intent(context, VMService.class);
		context.startService(service);
	}
}
