package com.njust.major.SCM;

import android.content.Context;

import com.major.SerialPort;
import com.njust.major.dao.MachineStateDao;
import com.njust.major.dao.impl.MachineStateDaoImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BigEyes {
	// private com.dwin.navy.serialportapi.SerialPort bigEyesPort;
	// private SerialPort bigEyesPort;
	// private SerialPort485 bigEyesPort;
	private SerialPort bigEyesPort;
	private byte eyesType;
	private int eyesNo;
	private byte[] BigEyesTXbuf = new byte[65];

	private boolean needQueryButton = true;
	private boolean receiveQueryButton = false;
	private int pressID = 0;

	private boolean needControlSale = false;
	private boolean receiveControlSale = false;
	private boolean salelight = false;
	private int lightedSaleID = 0;
	private List<Integer> lightedSaleIDList = new ArrayList<Integer>();
	private Map<Integer, Integer> saleLightMap = new HashMap<Integer, Integer>();

	private boolean needControlShort = false;
	private boolean receiveControlShort = false;
	private boolean shortlight = false;
	private int lightedShortID = 0;
	private List<Integer> lightedShortIDList = new ArrayList<Integer>();
	private Map<Integer, Integer> shortLightMap = new HashMap<Integer, Integer>();

	private boolean needControlLight = false;
	private boolean receiveControlLight = false;
	private boolean light = false;

	private boolean needControlPouse = false;
	private boolean receiveControlPouse = false;
	private Map<Integer, Integer> pouseLightMap = new HashMap<Integer, Integer>();

	private int byNo;
	private int remianBitNo;
	private Context context;

	private boolean BigEyesKeyDown = false;
	private boolean MdbPutInMoney = false;
	// public void updatePar() {
	// eyesType = (byte) mDao.queryMachineState(1).getEyesType();
	// eyesNo = mDao.queryMachineState(1).getEyesNo();
	// byNo = eyesNo / 8;
	// remianBitNo = eyesNo - byNo * 8;
	// initLightMap();
	// }

	public boolean isBigEyesKeyDown() {
		return BigEyesKeyDown;
	}

	public void setBigEyesKeyDown(boolean bigEyesKeyDown) {
		BigEyesKeyDown = bigEyesKeyDown;
	}

	public boolean isMdbPutInMoney() {
		return MdbPutInMoney;
	}

	public void setMdbPutInMoney(boolean mdbPutInMoney) {
		MdbPutInMoney = mdbPutInMoney;
	}

	public boolean isNeedControlPouse() {
		return needControlPouse;
	}

	public void setNeedControlPouse(boolean needControlPouse) {
		this.needControlPouse = needControlPouse;
	}

	public boolean isReceiveControlPouse() {
		return receiveControlPouse;
	}

	public void setReceiveControlPouse(boolean receiveControlPouse) {
		this.receiveControlPouse = receiveControlPouse;
	}

	public Map<Integer, Integer> getPouseLightMap() {
		return pouseLightMap;
	}

	public void setPouseLightMap(Map<Integer, Integer> pouseLightMap) {
		this.pouseLightMap = pouseLightMap;
	}

	public int getEyesNo() {
		return eyesNo;
	}

	public void setEyesNo(int eyesNo) {
		this.eyesNo = eyesNo;
		byNo = eyesNo / 8;
		remianBitNo = eyesNo - byNo * 8;
	}

	public byte getEyesType() {
		return eyesType;
	}

	public void setEyesType(byte eyesType) {
		this.eyesType = eyesType;
	}

	public boolean isNeedQueryButton() {
		return needQueryButton;
	}

	public void setNeedQueryButton(boolean needQueryButton) {
		this.needQueryButton = needQueryButton;
	}

	public boolean isReceiveQueryButton() {
		return receiveQueryButton;
	}

	public void setReceiveQueryButton(boolean receiveQueryButton) {
		this.receiveQueryButton = receiveQueryButton;
	}

	public int getPressID() {
		return pressID;
	}

	public void setPressID(int pressID) {
		this.pressID = pressID;
	}

	public boolean isNeedControlSale() {
		return needControlSale;
	}

	public void setNeedControlSale(boolean needControlSale) {
		this.needControlSale = needControlSale;
	}

	public boolean isReceiveControlSale() {
		return receiveControlSale;
	}

	public void setReceiveControlSale(boolean receiveControlSale) {
		this.receiveControlSale = receiveControlSale;
	}

	public boolean isSalelight() {
		return salelight;
	}

	public void setSalelight(boolean salelight) {
		this.salelight = salelight;
	}

	public int getLightedSaleID() {
		return lightedSaleID;
	}

	public void setLightedSaleID(int lightedSaleID) {
		this.lightedSaleID = lightedSaleID;
	}

	public List<Integer> getLightedSaleIDList() {
		return lightedSaleIDList;
	}

	public void setLightedSaleIDList(List<Integer> lightedSaleIDList) {
		this.lightedSaleIDList = lightedSaleIDList;
	}

	public boolean isNeedControlShort() {
		return needControlShort;
	}

	public void setNeedControlShort(boolean needControlShort) {
		this.needControlShort = needControlShort;
	}

	public boolean isReceiveControlShort() {
		return receiveControlShort;
	}

	public void setReceiveControlShort(boolean receiveControlShort) {
		this.receiveControlShort = receiveControlShort;
	}

	public boolean isShortlight() {
		return shortlight;
	}

	public void setShortlight(boolean shortlight) {
		this.shortlight = shortlight;
	}

	public int getLightedShortID() {
		return lightedShortID;
	}

	public void setLightedShortID(int lightedShortID) {
		this.lightedShortID = lightedShortID;
	}

	public List<Integer> getLightedShortIDList() {
		return lightedShortIDList;
	}

	public void setLightedShortIDList(List<Integer> lightedShortIDList) {
		this.lightedShortIDList = lightedShortIDList;
	}

	public boolean isLight() {
		return light;
	}

	public void setLight(boolean light) {
		this.light = light;
	}

	public boolean isNeedControlLight() {
		return needControlLight;
	}

	public void setNeedControlLight(boolean needControlLight) {
		this.needControlLight = needControlLight;
	}

	public boolean isReceiveControlLight() {
		return receiveControlLight;
	}

	public Map<Integer, Integer> getSaleLightMap() {
		return saleLightMap;
	}

	public void setSaleLightMap(Map<Integer, Integer> saleLightMap) {
		this.saleLightMap = saleLightMap;
	}

	public Map<Integer, Integer> getShortLightMap() {
		return shortLightMap;
	}

	public void setShortLightMap(Map<Integer, Integer> shortLightMap) {
		this.shortLightMap = shortLightMap;
	}

	public void setReceiveControlLight(boolean receiveControlLight) {
		this.receiveControlLight = receiveControlLight;
	}

	// public BigEyes(SerialPort485 serialPort485) {
	// super();
	// // TODO Auto-generated constructor stub
	// bigEyesPort = serialPort485;
	// }
	private MachineStateDao mDao;

	public BigEyes(SerialPort serialPort485, Context context) {
		super();
		// TODO Auto-generated constructor stub
		bigEyesPort = serialPort485;
		this.context = context;
		mDao = new MachineStateDaoImpl(context);
	}

	public void initLightMap() {
		for (int i = 0; i <= 42; i++) {
			saleLightMap.put(i, 0);
			shortLightMap.put(i, 0);
			pouseLightMap.put(i, 0);
		}
	}

	// public void openBigEyesPort() {
	// bigEyesPort = new SerialPort(1, 9600, 8, 1, 'n');
	// }
	public void setButton(byte buttonState) {
		BigEyesTXbuf[0] = 0x24; // ��ʼ��'$'
		BigEyesTXbuf[1] = 0x00;
		BigEyesTXbuf[2] = 0x08;
		BigEyesTXbuf[3] = 'B';
		BigEyesTXbuf[4] = buttonState;
		byte CRC = BigEyesTXbuf[0];
		for (int i = 0; i < 6; i++) {
			CRC = (byte) (CRC ^ BigEyesTXbuf[i]);
		}
		BigEyesTXbuf[6] = CRC;
		BigEyesTXbuf[7] = 0x21;
		bigEyesPort.sendData(BigEyesTXbuf, 8);
	}

	public void queryButton() {
		BigEyesTXbuf[0] = 0x24;
		BigEyesTXbuf[1] = 0x01;
		BigEyesTXbuf[2] = 0;
		BigEyesTXbuf[3] = eyesType;
		System.out.println("eyesType:" + eyesType);
		BigEyesTXbuf[4] = 0;
		BigEyesTXbuf[5] = 0;
		BigEyesTXbuf[6] = 0;
		BigEyesTXbuf[7] = 0;
		BigEyesTXbuf[8] = 0;
		BigEyesTXbuf[9] = 0;
		byte CRC = BigEyesTXbuf[0];
		for (int i = 1; i < 10; i++) {
			CRC = (byte) (CRC ^ BigEyesTXbuf[i]);
		}
		BigEyesTXbuf[10] = CRC;
		BigEyesTXbuf[11] = 0x21;
		bigEyesPort.sendData(BigEyesTXbuf, 12);
	}

	public void controlSale() {
		BigEyesTXbuf[0] = 0x24;
		BigEyesTXbuf[1] = 0x01;
		BigEyesTXbuf[2] = 0x01;
		BigEyesTXbuf[3] = eyesType;
		// BigEyesTXbuf[4] = (byte) lightedSaleID;
		// if (salelight) {
		// BigEyesTXbuf[5] = 1;
		// } else {
		// BigEyesTXbuf[5] = 0;
		// }
		// BigEyesTXbuf[6] = 0;
		// BigEyesTXbuf[7] = 0;
		// BigEyesTXbuf[8] = 0;
		// BigEyesTXbuf[9] = 0;
		setLightState(1);
		byte CRC = BigEyesTXbuf[0];
		for (int i = 1; i < 10; i++) {
			CRC = (byte) (CRC ^ BigEyesTXbuf[i]);
		}
		BigEyesTXbuf[10] = CRC;
		BigEyesTXbuf[11] = 0x21;
		bigEyesPort.sendData(BigEyesTXbuf, 12);
	}

	public void controlShort() {
		BigEyesTXbuf[0] = 0x24;
		BigEyesTXbuf[1] = 0x01;
		BigEyesTXbuf[2] = 0x02;
		BigEyesTXbuf[3] = eyesType;
		// BigEyesTXbuf[4] = 0;
		// BigEyesTXbuf[5] = 0;
		// BigEyesTXbuf[6] = (byte) lightedShortID;
		// if (shortlight) {
		// BigEyesTXbuf[7] = 1;
		// } else {
		// BigEyesTXbuf[7] = 0;
		// }
		// BigEyesTXbuf[8] = 0;
		// BigEyesTXbuf[9] = 0;
		setLightState(2);
		byte CRC = BigEyesTXbuf[0];
		for (int i = 1; i < 10; i++) {
			CRC = (byte) (CRC ^ BigEyesTXbuf[i]);
		}
		BigEyesTXbuf[10] = CRC;
		BigEyesTXbuf[11] = 0x21;
		bigEyesPort.sendData(BigEyesTXbuf, 12);
	}

	public void controlPouse() {
		BigEyesTXbuf[0] = 0x24;
		BigEyesTXbuf[1] = 0x01;
		BigEyesTXbuf[2] = 0x04;
		BigEyesTXbuf[3] = eyesType;
		// BigEyesTXbuf[4] = 0;
		// BigEyesTXbuf[5] = 0;
		// BigEyesTXbuf[6] = (byte) lightedShortID;
		// if (shortlight) {
		// BigEyesTXbuf[7] = 1;
		// } else {
		// BigEyesTXbuf[7] = 0;
		// }
		// BigEyesTXbuf[8] = 0;
		// BigEyesTXbuf[9] = 0;
		setLightState(3);
		byte CRC = BigEyesTXbuf[0];
		for (int i = 1; i < 10; i++) {
			CRC = (byte) (CRC ^ BigEyesTXbuf[i]);
		}
		BigEyesTXbuf[10] = CRC;
		BigEyesTXbuf[11] = 0x21;
		bigEyesPort.sendData(BigEyesTXbuf, 12);
	}

	public void controlLight() {
		BigEyesTXbuf[0] = 0x24;
		BigEyesTXbuf[1] = 0x01;
		BigEyesTXbuf[2] = 0x03;
		BigEyesTXbuf[3] = eyesType;
		if (light) {
			BigEyesTXbuf[4] = 1;
		} else {
			BigEyesTXbuf[4] = 0;
		}
		BigEyesTXbuf[5] = 0;
		BigEyesTXbuf[6] = 0;
		BigEyesTXbuf[7] = 0;
		BigEyesTXbuf[8] = 0;
		// if (light) {
		// BigEyesTXbuf[9] = 1;
		// } else {
		// BigEyesTXbuf[9] = 0;
		// }
		BigEyesTXbuf[9] = 0;
		byte CRC = BigEyesTXbuf[0];
		for (int i = 1; i < 10; i++) {
			CRC = (byte) (CRC ^ BigEyesTXbuf[i]);
		}
		BigEyesTXbuf[10] = CRC;
		BigEyesTXbuf[11] = 0x21;
		bigEyesPort.sendData(BigEyesTXbuf, 12);
		String a = Integer.toHexString(BigEyesTXbuf[0]) + " ";
		for (int i = 1; i < BigEyesTXbuf.length; i++) {
			a += Integer.toHexString(BigEyesTXbuf[i]) + " ";
		}
		// System.out.println("mdbb:"+a);
	}

	public void ReplyReceivePress() {
		BigEyesTXbuf[0] = 0x24; // ��ʼ��'$'
		BigEyesTXbuf[1] = 0x00;
		BigEyesTXbuf[2] = 0x08;
		BigEyesTXbuf[3] = 'p';
		byte CRC = BigEyesTXbuf[0];
		for (int i = 0; i < 6; i++) {
			CRC = (byte) (CRC ^ BigEyesTXbuf[i]);
		}
		BigEyesTXbuf[6] = CRC;
		BigEyesTXbuf[7] = 0x21;
		bigEyesPort.sendData(BigEyesTXbuf, 8);
	}

	public void shutDownAllSaleLight() {
		for (int i = 1; i <= eyesNo; i++) {
			saleLightMap.put(i, 0);
		}
	}

	private void setLightState(int type) {
		if (type == 1) {
			for (int i = 0; i < byNo; i++) {
				for (int j = 7; j >= 0; j--) {
					int a = saleLightMap.get(8 - j + i * 8);
					if (a == 1) {
						BigEyesTXbuf[4 + i] |= (0x1 << j);
					} else if (a == 0)
						BigEyesTXbuf[4 + i] &= ~(0x1 << j);
				}
			}
			for (int j = 7; j > 7 - remianBitNo; j--) {
				int a = saleLightMap.get(8 - j + byNo * 8);
				if (a == 1) {
					BigEyesTXbuf[4 + byNo] |= (0x1 << j);
				} else if (a == 0)
					BigEyesTXbuf[4 + byNo] &= ~(0x1 << j);
			}
		} else if (type == 2) {
			for (int i = 0; i < byNo; i++) {
				for (int j = 7; j >= 0; j--) {
					int a = shortLightMap.get(8 - j + i * 8);
					if (a == 1) {
						BigEyesTXbuf[4 + i] |= (0x1 << j);
					} else if (a == 0)
						BigEyesTXbuf[4 + i] &= ~(0x1 << j);
				}
			}
			for (int j = 7; j > 7 - remianBitNo; j--) {
				int a = shortLightMap.get(8 - j + byNo * 8);
				if (a == 1) {
					BigEyesTXbuf[4 + byNo] |= (0x1 << j);
				} else if (a == 0)
					BigEyesTXbuf[4 + byNo] &= ~(0x1 << j);
			}
		} else if (type == 3) {
			for (int i = 0; i < byNo; i++) {
				for (int j = 7; j >= 0; j--) {
					int a = pouseLightMap.get(8 - j + i * 8);
					if (a == 1) {
						BigEyesTXbuf[4 + i] |= (0x1 << j);
					} else if (a == 0)
						BigEyesTXbuf[4 + i] &= ~(0x1 << j);
				}
			}
			for (int j = 7; j > 7 - remianBitNo; j--) {
				int a = pouseLightMap.get(8 - j + byNo * 8);
				if (a == 1) {
					BigEyesTXbuf[4 + byNo] |= (0x1 << j);
				} else if (a == 0)
					BigEyesTXbuf[4 + byNo] &= ~(0x1 << j);
			}
		}
	}
	// public class ReceiveBigEyesThread extends Thread {
	// @Override
	// public void run() {
	// super.run();
	// while (!isInterrupted()) {
	// BigEyesRXbuf = bigEyesPort.receiveData();
	// if (BigEyesRXbuf != null) {
	// if (BigEyesRXbuf[0] == 0x24) {
	// if (BigEyesRXbuf[2] == 'l') {
	// receiveLight = true;
	// }
	// if (BigEyesRXbuf[2] == 'd') {
	// receiveDown = true;
	// }
	// if (BigEyesRXbuf[2] == 'n') {
	// receiveNeedSupply = true;
	// }
	// if (BigEyesRXbuf[2] == 'P') {
	// receivePress = true;
	// pressID = Integer.toHexString(BigEyesRXbuf[3])
	// + Integer.toHexString(BigEyesRXbuf[4]);
	// ReplyReceivePress();
	// }
	// }
	// }
	// try {
	// Thread.sleep(1000);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	//
	// }
	// }
	// }
	// }
}
