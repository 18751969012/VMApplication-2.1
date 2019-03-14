package com.njust.major;

import java.util.Timer;
import java.util.TimerTask;

public class CTimer {
	private Timer sendTimer;
	private TimerTask sendTimerTask;
	private long delay;

	private boolean timeOut = false;

	public CTimer(long delay) {
		super();
		// TODO Auto-generated constructor stub
		this.delay = delay;
	}

	public boolean isTimeOut() {
		return timeOut;
	}

	public void setTimeOut(boolean timeOut) {
		this.timeOut = timeOut;
	}

	public void startTimer() {
		setTimeOut(false);
		if (sendTimer == null) {
			sendTimer = new Timer();
		}

		if (sendTimerTask == null) {
			sendTimerTask = new TimerTask() {
				@Override
				public void run() {
					setTimeOut(true);
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
		setTimeOut(false);
	}
}
