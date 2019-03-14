package com.njust.major.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Binder;
import android.os.IBinder;
import android.view.KeyEvent;

import com.njust.major.OutGoodsReceiver;
import com.njust.major.SCM.BigEyes;
import com.njust.major.SCM.DrinkGate;
import com.njust.major.SCM.TemperControl;
import com.njust.major.bean.MdbBean;
import com.njust.major.dao.MachineStateDao;
import com.njust.major.dao.impl.MachineStateDaoImpl;
import com.njust.major.thread.MdbThread;
import com.njust.major.thread.SCMThread;
import com.njust.major.thread.TransactionThread;
import com.njust.major.util.Util;

public class VMService extends Service {
	private MdbThread mdbThread;
	private SCMThread scmThread;
	public TransactionThread transactionThread;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Util.WriteFile("Service_onCreate");
		updateVersion();
		mdbThread = new MdbThread(getApplicationContext());
		scmThread = new SCMThread(getApplicationContext(), mdbThread);
		transactionThread = new TransactionThread(getApplicationContext(),
				scmThread, mdbThread);

		mdbThread.start(); // 开启纸硬币通信线程
		scmThread.start(); // 开启下位机通信线程
		transactionThread.start(); // 开启交易主线程
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Util.WriteFile("Service_onStartCommand");

		// 已投币金额显示清零
		Intent intent1 = new Intent();
		intent1.setAction("Serialport_Action_RECEIVE_MONEY");
		intent1.putExtra("receive_total_money", 0);
		sendBroadcast(intent1);

		// mdbThread、scmThread、transactionThread三个线程都暂停
		mdbThread.setFlag(false);
		scmThread.setFlag(false);
		transactionThread.setFlag(false);

		// mdbThread、scmThread、transactionThread三个线程初始化
		mdbThread.initMdb();
		scmThread.initSCM();
		transactionThread.initTransaction();

		// mdbThread、scmThread、transactionThread三个线程继续运行
		mdbThread.setFlag(true);
		scmThread.setFlag(true);
		transactionThread.setFlag(true);

		return super.onStartCommand(intent, flags, startId);
	}

	private String getVersionName() {
		String version = "";
		try {
			// 获取packagemanager的实例
			PackageManager packageManager = getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo;
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			version = packInfo.versionName.substring(packInfo.versionName.length() - 3);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}

	private void updateVersion() {
		MachineStateDao mDao = new MachineStateDaoImpl(getApplicationContext());
		mDao.updateVersion(getVersionName());
	}


}
