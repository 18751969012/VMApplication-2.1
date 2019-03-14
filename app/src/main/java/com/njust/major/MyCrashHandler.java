package com.njust.major;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

public class MyCrashHandler implements UncaughtExceptionHandler {

	private static MyCrashHandler instance;

	public static MyCrashHandler getInstance() {
		if (instance == null) {
			instance = new MyCrashHandler();
		}
		return instance;
	}

	public void init(Context ctx) {
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 核心方法，当程序crash 会回调此方法， Throwable中存放这错误日志
	 */
	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {

		String logPath;
		logPath = Environment.getExternalStorageDirectory().getAbsoluteFile()
				+ "/VM/" + "错误日志Log";

		File file = new File(logPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			FileWriter fw = new FileWriter(logPath + File.separator
					+ "myErrorlog.log", true);
			fw.write(new Date() + "错误原因：\n");
			// 错误信息
			// 这里还可以加上当前的系统版本，机型型号 等等信息
			StackTraceElement[] stackTrace = arg1.getStackTrace();
			fw.write(arg1.getMessage() + "\n");
			for (int i = 0; i < stackTrace.length; i++) {
				fw.write("file:" + stackTrace[i].getFileName() + " class:"
						+ stackTrace[i].getClassName() + " method:"
						+ stackTrace[i].getMethodName() + " line:"
						+ stackTrace[i].getLineNumber() + "\n");
			}
			fw.write("\n");
			fw.close();
			// 上传错误信息到服务器
			// uploadToServer();
		} catch (IOException e) {
			Log.e("crash handler", "load file failed...", e.getCause());
		}
		arg1.printStackTrace();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
