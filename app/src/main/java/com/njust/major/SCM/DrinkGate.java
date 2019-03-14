package com.njust.major.SCM;

import android.content.Context;

import com.major.SerialPort;
import com.njust.major.bean.MachineState;
import com.njust.major.dao.MachineStateDao;
import com.njust.major.dao.impl.MachineStateDaoImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DrinkGate {
	// private com.dwin.navy.serialportapi.SerialPort drinkGatePort;
	// private SerialPort drinkGatePort;
	// private SerialPort485 drinkGatePort;
	private SerialPort drinkGatePort;
	private byte[] drinkGateTXbuf = new byte[15];
	private boolean needDrinkOut_assist = false;
	private boolean needDrinkOut = false;
	private int pressedDrinkPositionID = 0;
	private int gateActionTime;
	private int roadNo;
	private int counter = 2;
	private int lightCounter = 2;
	private boolean receiveDrinkOut = false;
	private boolean receiveDrinkOut_assist = false;
	private boolean needCheckHavingDrink = true;
	// private boolean needCheckHavingDrink = false;
	private boolean receiveCheckHavingDrink = false;

	private boolean light = false;
	private boolean needLight = false;
	private boolean receiveLight = false;

	private boolean outGoodsSuccess = true;


	private MachineStateDao mDao;
	private int Raster = 0;
	private int LockBack = 0;
	private Map<Integer, Integer> shortRoadMap = new HashMap<Integer, Integer>();;

	// public void updatePar() {
	// roadNo = mDao.queryMachineState(1).getRoadNo();
	// initShortRoadMap();
	// gateActionTime = 200;
	// }


	public boolean isOutGoodsSuccess() {
		return outGoodsSuccess;
	}

	public void setOutGoodsSuccess(boolean outGoodsSuccess) {
		this.outGoodsSuccess = outGoodsSuccess;
	}

	public int getRaster() {
		getPar(counter);
		return Raster;
	}

	public void setRaster(int raster) {
		Raster = raster;
	}

	public boolean isLight() {
		return light;
	}

	public void setLight(boolean light) {
		this.light = light;
	}

	public int getLightCounter() {
		return lightCounter;
	}

	public void setLightCounter(int lightCounter) {
		this.lightCounter = lightCounter;
	}

	public boolean isNeedLight() {
		return needLight;
	}

	public void setNeedLight(boolean needLight) {
		this.needLight = needLight;
	}

	public boolean isReceiveLight() {
		return receiveLight;
	}

	public void setReceiveLight(boolean receiveLight) {
		this.receiveLight = receiveLight;
	}

	public boolean isReceiveDrinkOut_assist() {
		return receiveDrinkOut_assist;
	}

	public void setReceiveDrinkOut_assist(boolean receiveDrinkOut_assist) {
		this.receiveDrinkOut_assist = receiveDrinkOut_assist;
	}

	public boolean isNeedDrinkOut_assist() {
		return needDrinkOut_assist;
	}

	public void setNeedDrinkOut_assist(boolean needDrinkOut_assist) {
		this.needDrinkOut_assist = needDrinkOut_assist;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getRoadNo() {
		return roadNo;
	}

	public void setRoadNo(int roadNo) {
		this.roadNo = roadNo;
		gateActionTime = 200;
	}

	public Map<Integer, Integer> getShortRoadMap() {
		return shortRoadMap;
	}

	public void setShortRoadMap(Map<Integer, Integer> shortRoadMap) {
		this.shortRoadMap = shortRoadMap;
	}

	public int getGateActionTime() {
		return gateActionTime;
	}

	public void setGateActionTime(int gateActionTime) {
		this.gateActionTime = gateActionTime;
	}

	public boolean isNeedDrinkOut() {
		return needDrinkOut;
	}

	public void setNeedDrinkOut(boolean needDrinkOut) {
		this.needDrinkOut = needDrinkOut;
	}

	public boolean isNeedCheckHavingDrink() {
		return needCheckHavingDrink;
	}

	public void setNeedCheckHavingDrink(boolean needCheckHavingDrink) {
		this.needCheckHavingDrink = needCheckHavingDrink;
	}

	public ArrayList<String> getNoDrinkPositionID() {
		return NoDrinkPositionID;
	}

	public void setNoDrinkPositionID(ArrayList<String> noDrinkPositionID) {
		NoDrinkPositionID = noDrinkPositionID;
	}

	public boolean isReceiveCheckHavingDrink() {
		return receiveCheckHavingDrink;
	}

	public int getPressedDrinkPositionID() {
		return pressedDrinkPositionID;
	}

	public void setPressedDrinkPositionID(int pressedDrinkPositionID) {
		this.pressedDrinkPositionID = pressedDrinkPositionID;
	}

	public void setReceiveCheckHavingDrink(boolean receiveCheckHavingDrink) {
		this.receiveCheckHavingDrink = receiveCheckHavingDrink;
	}

	public ArrayList<String> NoDrinkPositionID;

	// public DrinkGateReceiveThread drinkGateReceiveThread;

	// public DrinkGate(SerialPort485 serialPort485) {
	// super();
	// // TODO Auto-generated constructor stub
	// drinkGatePort = serialPort485;
	// NoDrinkPositionID = new ArrayList<String>();
	// }

	public DrinkGate(SerialPort serialPort485, Context context) {
		super();
		// TODO Auto-generated constructor stub
		drinkGatePort = serialPort485;
		NoDrinkPositionID = new ArrayList<String>();
		mDao = new MachineStateDaoImpl(context);
	}

	public void initShortRoadMap() {
		for (int i = 0; i < 42; i++) {
			shortRoadMap.put(i + 1, 1);
		}
	}

	public boolean isReceiveDrinkOut() {
		return receiveDrinkOut;
	}

	public void setReceiveDrinkOut(boolean receiveDrinkOut) {
		this.receiveDrinkOut = receiveDrinkOut;
	}

	// public void openDrinkGatePort() {
	// drinkGatePort = new SerialPort(2, 9600, 8, 1, 'n');
	// }

	public void drinkOut() {
		drinkGateTXbuf[0] = 0x24;
		drinkGateTXbuf[1] = 0x02;
		drinkGateTXbuf[2] = 0x0C;
		drinkGateTXbuf[3] = 'E';
		drinkGateTXbuf[4] = (byte) pressedDrinkPositionID;
		drinkGateTXbuf[5] = (byte) (gateActionTime / 10);
		drinkGateTXbuf[6] = 0;
		// drinkGateTXbuf[6] = (byte) (gateActionTime - drinkGateTXbuf[5] *
		// 255);
		byte CRC = drinkGateTXbuf[0];
		for (int i = 1; i < 10; i++) {
			CRC = (byte) (CRC ^ drinkGateTXbuf[i]);
		}
		drinkGateTXbuf[10] = CRC;
		drinkGateTXbuf[11] = 0x21;
		drinkGatePort.sendData(drinkGateTXbuf, 12);
		// for (int i = 0; i < 12; i++) {
		// System.out.println(Integer.toHexString(drinkGateTXbuf[i]) + "gg");
		// }
		// int huoDao = ceng * cengNum + lie;
		// byte huoDaoBYTE = (byte) (huoDao / 10 * 16 + huoDao % 10);
		// drinkGateTXbuf[0] = 0x01;
		// drinkGateTXbuf[1] = 0x05;
		// drinkGateTXbuf[2] = huoDaoBYTE;
		// drinkGateTXbuf[3] = 0x00;
		// drinkGateTXbuf[4] = 0x00;
		// drinkGateTXbuf[5] = 0x00;
		// drinkGateTXbuf[6] = 0x00;
		// drinkGateTXbuf[7] = 0x00;
		// char crcChar = crcVal(drinkGateTXbuf, 8);
		// drinkGateTXbuf[8] = (byte) crcChar;
		// drinkGateTXbuf[9] = (byte) (crcChar >> 8);
		// drinkGatePort.sendData(drinkGateTXbuf, 10);
	}

	public void light_assist() {
		getPar(counter);
		drinkGateTXbuf[0] = 0x24;
		drinkGateTXbuf[1] = (byte) (0x80 + (lightCounter - 2));
		drinkGateTXbuf[2] = 0x0C;
		drinkGateTXbuf[3] = 0x01;
		drinkGateTXbuf[4] = 0;
		drinkGateTXbuf[5] = (byte) 0xFF;
		drinkGateTXbuf[6] = (byte) 0xFF;
		// drinkGateTXbuf[6] = (byte) (gateActionTime - drinkGateTXbuf[5] *
		// 255);
		drinkGateTXbuf[7] = 0;
		if (light) {
			drinkGateTXbuf[8] = 1;
		} else {
			drinkGateTXbuf[8] = 0;
		}
		byte CRC = drinkGateTXbuf[0];
		for (int i = 1; i < 10; i++) {
			CRC = (byte) (CRC ^ drinkGateTXbuf[i]);
		}
		drinkGateTXbuf[10] = CRC;
		drinkGateTXbuf[11] = 0x21;
		drinkGatePort.sendData(drinkGateTXbuf, 12);
	}

	public void checkOut_assist() {
		getPar(counter);
		drinkGateTXbuf[0] = 0x24;
		drinkGateTXbuf[1] = (byte) (0x80 + (counter - 2));
		drinkGateTXbuf[2] = 0x0C;
		drinkGateTXbuf[3] = 0x02;
		drinkGateTXbuf[4] = (byte) pressedDrinkPositionID;
		drinkGateTXbuf[5] = (byte) 0xFF;
		drinkGateTXbuf[6] = (byte) 0xFF;
		// drinkGateTXbuf[6] = (byte) (gateActionTime - drinkGateTXbuf[5] *
		// 255);
		drinkGateTXbuf[7] = (byte) Raster;
		drinkGateTXbuf[9] = (byte) LockBack;
		byte CRC = drinkGateTXbuf[0];
		for (int i = 1; i < 10; i++) {
			CRC = (byte) (CRC ^ drinkGateTXbuf[i]);
		}
		drinkGateTXbuf[10] = CRC;
		drinkGateTXbuf[11] = 0x21;
		drinkGatePort.sendData(drinkGateTXbuf, 12);
	}

	public void check_assist(int ct) {
		getPar(counter);
		drinkGateTXbuf[0] = 0x24;
		drinkGateTXbuf[1] = (byte) (0x80 + (ct - 2));
		drinkGateTXbuf[2] = 0x0C;
		drinkGateTXbuf[3] = 0x02;
		drinkGateTXbuf[4] = (byte) 1;
		drinkGateTXbuf[5] = (byte) 0xFF;
		drinkGateTXbuf[6] = (byte) 0xFF;
		// drinkGateTXbuf[6] = (byte) (gateActionTime - drinkGateTXbuf[5] *
		// 255);
		drinkGateTXbuf[7] = (byte) Raster;
		drinkGateTXbuf[9] = (byte) LockBack;
		byte CRC = drinkGateTXbuf[0];
		for (int i = 1; i < 10; i++) {
			CRC = (byte) (CRC ^ drinkGateTXbuf[i]);
		}
		drinkGateTXbuf[10] = CRC;
		drinkGateTXbuf[11] = 0x21;
		drinkGatePort.sendData(drinkGateTXbuf, 12);
	}

	public void checkGate_assist() {
		getPar(counter);
		drinkGateTXbuf[0] = 0x24;
		drinkGateTXbuf[1] = (byte) 0x80;
		drinkGateTXbuf[2] = 0x0C;
		drinkGateTXbuf[3] = 0x02;
		drinkGateTXbuf[4] = (byte) pressedDrinkPositionID;
		drinkGateTXbuf[5] = (byte) 0xFF;
		drinkGateTXbuf[6] = (byte) 0xFF;
		drinkGateTXbuf[7] = (byte) Raster;
		drinkGateTXbuf[9] = (byte) LockBack;
		byte CRC = drinkGateTXbuf[0];
		for (int i = 1; i < 10; i++) {
			CRC = (byte) (CRC ^ drinkGateTXbuf[i]);
		}
		drinkGateTXbuf[10] = CRC;
		drinkGateTXbuf[11] = 0x21;
		printReceive3();
		drinkGatePort.sendData(drinkGateTXbuf, 12);
	}

	private void printReceive3() {
		String a = "";
		for (int i = 0; i < 12; i++) {
			a += Integer.toHexString(drinkGateTXbuf[i]) + " ";
		}
		System.out.println(a + "  总字节：" + drinkGateTXbuf.length);

	}

	public void drinkOut_assist() {
		getPar(counter);
		drinkGateTXbuf[0] = 0x24;
		drinkGateTXbuf[1] = (byte) (0x80 + (counter - 2));
		drinkGateTXbuf[2] = 0x0C;
		drinkGateTXbuf[3] = 0x01;
		drinkGateTXbuf[4] = (byte) pressedDrinkPositionID;
		drinkGateTXbuf[5] = (byte) 0xFF;
		drinkGateTXbuf[6] = (byte) 0xFF;
		// drinkGateTXbuf[6] = (byte) (gateActionTime - drinkGateTXbuf[5] *
		// 255);
		drinkGateTXbuf[7] = (byte) Raster;
		drinkGateTXbuf[9] = (byte) LockBack;
		byte CRC = drinkGateTXbuf[0];
		for (int i = 1; i < 10; i++) {
			CRC = (byte) (CRC ^ drinkGateTXbuf[i]);
		}
		drinkGateTXbuf[10] = CRC;
		drinkGateTXbuf[11] = 0x21;
		drinkGatePort.sendData(drinkGateTXbuf, 12);
		// for (int i = 0; i < 12; i++) {
		// System.out.println(Integer.toHexString(drinkGateTXbuf[i]) + "gg");
		// }
	}

	public void drinkOut_assist_query() {
		getPar(counter);
		drinkGateTXbuf[0] = 0x24;
		drinkGateTXbuf[1] = (byte) (0x80 + (counter - 2));
		drinkGateTXbuf[2] = 0x0C;
		drinkGateTXbuf[3] = 0x02;
		drinkGateTXbuf[4] = (byte) pressedDrinkPositionID;
		drinkGateTXbuf[5] = (byte) 0xFF;
		drinkGateTXbuf[6] = (byte) 0xFF;
		// drinkGateTXbuf[6] = (byte) (gateActionTime - drinkGateTXbuf[5] *
		// 255);
		drinkGateTXbuf[7] = (byte) Raster;
		drinkGateTXbuf[9] = (byte) LockBack;
		byte CRC = drinkGateTXbuf[0];
		for (int i = 1; i < 10; i++) {
			CRC = (byte) (CRC ^ drinkGateTXbuf[i]);
		}
		drinkGateTXbuf[10] = CRC;
		drinkGateTXbuf[11] = 0x21;
		drinkGatePort.sendData(drinkGateTXbuf, 12);
		// for (int i = 0; i < 12; i++) {
		// System.out.println(Integer.toHexString(drinkGateTXbuf[i]) + "gg");
		// }
	}

	private void getPar(int counter) {
		MachineState state = mDao.queryMachineState(counter);
		Raster = state.getRaster();
		LockBack = state.getLockBack();
	}

	public void resetDrinkGate() {
		drinkGateTXbuf[0] = 0x01;
		drinkGateTXbuf[1] = 0x06;

		char crcChar = crcVal(drinkGateTXbuf, 2);
		drinkGateTXbuf[2] = (byte) crcChar;
		drinkGateTXbuf[3] = (byte) (crcChar >> 8);
		drinkGatePort.sendData(drinkGateTXbuf, 4);

	}

	public void checkDrinkGate() {
		drinkGateTXbuf[0] = 0x01;
		drinkGateTXbuf[1] = 0x03;

		char crcChar = crcVal(drinkGateTXbuf, 2);
		drinkGateTXbuf[2] = (byte) crcChar;
		drinkGateTXbuf[3] = (byte) (crcChar >> 8);
		drinkGatePort.sendData(drinkGateTXbuf, 4);

	}

	public void checkHavingDrink() {
		drinkGateTXbuf[0] = 0x24;
		drinkGateTXbuf[1] = 0x02;
		drinkGateTXbuf[2] = 0x0C;
		drinkGateTXbuf[3] = 'E';
		drinkGateTXbuf[4] = 0;
		drinkGateTXbuf[5] = 0;
		drinkGateTXbuf[6] = 0;
		byte CRC = drinkGateTXbuf[0];
		for (int i = 1; i < 10; i++) {
			CRC = (byte) (CRC ^ drinkGateTXbuf[i]);
		}
		drinkGateTXbuf[10] = CRC;
		drinkGateTXbuf[11] = 0x21;
		drinkGatePort.sendData(drinkGateTXbuf, 12);
		// drinkGateTXbuf[0] = 0x24;
		// drinkGateTXbuf[1] = (byte) (machine / 10 * 16 + machine % 10);
		// drinkGateTXbuf[2] = 0x08;
		// drinkGateTXbuf[3] = 'R';
		// byte CRC = drinkGateTXbuf[0];
		// for (int i = 0; i < 6; i++) {
		// CRC = (byte) (CRC ^ drinkGateTXbuf[i]);
		// }
		// drinkGateTXbuf[6] = CRC;
		// drinkGateTXbuf[7] = 0x21;
		// drinkGatePort.sendData(drinkGateTXbuf, 8);
	}

	// public class DrinkGateReceiveThread extends Thread {
	// @Override
	// public void run() {
	// super.run();
	// while (!isInterrupted()) {
	// drinkGateRXbuf = drinkGatePort.receiveData();
	// if (drinkGateRXbuf != null) {
	// if (drinkGateRXbuf[0] == 0x24) {
	// if (drinkGateRXbuf[2] == 'e') {
	// receiveDrinkOut = true;
	// }
	// }
	// }
	// try {
	// Thread.sleep(1000);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// }
	// }
	// }
	private char[] CrcTbl = { 0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0,
			0x0280, 0xC241, 0xC601, 0x06C0, 0x0780, 0xC741, 0x0500, 0xC5C1,
			0xC481, 0x0440, 0xCC01, 0x0CC0, 0x0D80, 0xCD41, 0x0F00, 0xCFC1,
			0xCE81, 0x0E40, 0x0A00, 0xCAC1, 0xCB81, 0x0B40, 0xC901, 0x09C0,
			0x0880, 0xC841, 0xD801, 0x18C0, 0x1980, 0xD941, 0x1B00, 0xDBC1,
			0xDA81, 0x1A40, 0x1E00, 0xDEC1, 0xDF81, 0x1F40, 0xDD01, 0x1DC0,
			0x1C80, 0xDC41, 0x1400, 0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0,
			0x1680, 0xD641, 0xD201, 0x12C0, 0x1380, 0xD341, 0x1100, 0xD1C1,
			0xD081, 0x1040, 0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1,
			0xF281, 0x3240, 0x3600, 0xF6C1, 0xF781, 0x3740, 0xF501, 0x35C0,
			0x3480, 0xF441, 0x3C00, 0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0,
			0x3E80, 0xFE41, 0xFA01, 0x3AC0, 0x3B80, 0xFB41, 0x3900, 0xF9C1,
			0xF881, 0x3840, 0x2800, 0xE8C1, 0xE981, 0x2940, 0xEB01, 0x2BC0,
			0x2A80, 0xEA41, 0xEE01, 0x2EC0, 0x2F80, 0xEF41, 0x2D00, 0xEDC1,
			0xEC81, 0x2C40, 0xE401, 0x24C0, 0x2580, 0xE541, 0x2700, 0xE7C1,
			0xE681, 0x2640, 0x2200, 0xE2C1, 0xE381, 0x2340, 0xE101, 0x21C0,
			0x2080, 0xE041, 0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1,
			0xA281, 0x6240, 0x6600, 0xA6C1, 0xA781, 0x6740, 0xA501, 0x65C0,
			0x6480, 0xA441, 0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0,
			0x6E80, 0xAE41, 0xAA01, 0x6AC0, 0x6B80, 0xAB41, 0x6900, 0xA9C1,
			0xA881, 0x6840, 0x7800, 0xB8C1, 0xB981, 0x7940, 0xBB01, 0x7BC0,
			0x7A80, 0xBA41, 0xBE01, 0x7EC0, 0x7F80, 0xBF41, 0x7D00, 0xBDC1,
			0xBC81, 0x7C40, 0xB401, 0x74C0, 0x7580, 0xB541, 0x7700, 0xB7C1,
			0xB681, 0x7640, 0x7200, 0xB2C1, 0xB381, 0x7340, 0xB101, 0x71C0,
			0x7080, 0xB041, 0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0,
			0x5280, 0x9241, 0x9601, 0x56C0, 0x5780, 0x9741, 0x5500, 0x95C1,
			0x9481, 0x5440, 0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1,
			0x9E81, 0x5E40, 0x5A00, 0x9AC1, 0x9B81, 0x5B40, 0x9901, 0x59C0,
			0x5880, 0x9841, 0x8801, 0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1,
			0x8A81, 0x4A40, 0x4E00, 0x8EC1, 0x8F81, 0x4F40, 0x8D01, 0x4DC0,
			0x4C80, 0x8C41, 0x4400, 0x84C1, 0x8581, 0x4540, 0x8701, 0x47C0,
			0x4680, 0x8641, 0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1,
			0x8081, 0x4040 };

	private char crcVal(byte[] pcMess, int wLen) {
		int i = 0;
		char nCRCData, Index = 0;
		nCRCData = 0xffff;
		while (wLen > 0) {
			wLen--;
			Index = (char) (nCRCData >> 8);
			Index = (char) (Index ^ (pcMess[i++] & 0x00ff));
			nCRCData = (char) ((nCRCData ^ CrcTbl[Index]) & 0x00ff);
			nCRCData = (char) ((nCRCData << 8) | (CrcTbl[Index] >> 8));
		}
		return (char) (nCRCData >> 8 | nCRCData << 8);
	}
}
