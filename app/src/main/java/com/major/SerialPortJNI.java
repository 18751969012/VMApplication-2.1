package com.major;

import android.util.Log;

public class SerialPortJNI {
	static {
		Log.i("NativeClass", "before load library");
		System.loadLibrary("native-lib");
		Log.i("NativeClass", "after load library");
	}
	public native int 	Open(int Port,int Rate,int nBits,char nEvent,int nStop);
	public native int 	Close(int Port);
	public native byte[]	Read(int Port);
	public native int	Write(int Port,byte[] buffer,int len);
}
