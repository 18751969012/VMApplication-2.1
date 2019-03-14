package com.major;

import java.io.UnsupportedEncodingException;

public class SerialPort extends SerialPortJNI {
private int Port;
private int Rate;
private int nBits;
private char nEvent;
private int nStop;
private String data;

public SerialPort(int port, int rate, int nBits, char nEvent, int nStop) {
	super();
	Port = port;
	Rate = rate;
	this.nBits = nBits;
	this.nEvent = nEvent;
	this.nStop = nStop;
	super.Open(port, rate, nBits, nEvent, nStop);
}
public void Close(){
	super.Close(Port);
}
public void sendData(byte[] buf, int length){
	super.Write(Port, buf, length);
}
public byte[] receiveData(){
	return super.Read(Port);
}
public String receiveData(String charsetName) {
	byte[] buf =super.Read(Port);
	if (buf!=null) {
		int length=buf.length;
		try {
			data = new String(buf, 0,length, charsetName);
//			if(data.contains("30 A2")){
//				System.out.println("rrrrr");
//				for(int i=0;i<length;i++){
//					System.out.println(Integer.toHexString(buf[i]));
//				}
//			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	} else {
		return null;
	}
}
}
