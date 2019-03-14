package com.njust.major.util;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Util {
	public static long count = 0;
	public static FileWriter fw = null;
	public static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
	static {
		File destFolder = new File(Environment.getExternalStorageDirectory()
				.getAbsoluteFile() + "/Android/data/com.icofficeapp/log");
		// File destFolder = new File(Environment.getExternalStorageDirectory()
		// .getAbsoluteFile() + "/vm/log/hardware");
		if (!destFolder.exists()) {
			destFolder.mkdir();
		}
	}

	public static String ReadFile(String Path) {
		BufferedReader reader = null;
		String laststr = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(Path);
			InputStreamReader inputStreamReader = new InputStreamReader(
					fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return laststr;
	}

	public static void WriteFile(String content) {
		String t = sd.format(Calendar.getInstance().getTime());
		String path = Environment.getExternalStorageDirectory()
				.getAbsoluteFile()
				+ "/Android/data/com.icofficeapp/log/comm_debug" + t + ".text";
		// String path = Environment.getExternalStorageDirectory()
		// .getAbsoluteFile() + "/vm/log/hardware/x" + t+".log";
		String time = sdf.format(Calendar.getInstance().getTime());
		String text = time + " " + content + "\r";
		try {
			fw = new FileWriter(path, true);
			fw.write(text, 0, text.length());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
