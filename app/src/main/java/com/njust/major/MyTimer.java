package com.njust.major;

import android.content.Context;
import android.content.Intent;

import com.njust.major.bean.Transaction;
import com.njust.major.dao.TransactionDao;
import com.njust.major.dao.impl.TransactionDaoImpl;
import com.njust.major.util.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MyTimer {
	private Context context;
	private Timer sendTimer;
	private TimerTask sendTimerTask;
	private long delay = 90000;
	private TransactionDao tDao;
	private SimpleDateFormat sdf;

	public MyTimer(Context context) {
		super();
		// TODO Auto-generated constructor stub
		this.context = context;
		tDao = new TransactionDaoImpl(context);
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public void startTimer() {
		if (sendTimer == null) {
			sendTimer = new Timer();
		}

		if (sendTimerTask == null) {
			sendTimerTask = new TimerTask() {
				@Override
				public void run() {
					Transaction transaction = tDao.queryLastedTransaction();
					if (transaction != null && transaction.getComplete() == 0
							&& transaction.getPayedAll() == 0) {
						transaction.setComplete(1);
						String time = sdf.format(Calendar.getInstance()
								.getTime());
						transaction.setEndTime(time);
						Util.WriteFile("存储交易1：" + transaction.toString());
						tDao.updateTransaction(transaction);
						Intent intent = new Intent();
						intent.setAction("Avm.cancelTransaction");
						context.sendBroadcast(intent);
						stopTimer();
					}
				}
			};
		}

		if (sendTimer != null && sendTimerTask != null)
			sendTimer.schedule(sendTimerTask, delay);
	}

	public void stopTimer() {
		if (sendTimer != null) {
			sendTimer.cancel();
			sendTimer = null;
		}
		if (sendTimerTask != null) {
			sendTimerTask.cancel();
			sendTimerTask = null;
		}
	}
}
