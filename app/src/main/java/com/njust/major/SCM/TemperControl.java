package com.njust.major.SCM;

import android.content.Context;

import com.major.SerialPort;
import com.njust.major.bean.MachineState;
import com.njust.major.dao.MachineStateDao;
import com.njust.major.dao.impl.MachineStateDaoImpl;

public class TemperControl {
	public final static byte Hot = 0x01;
	public final static byte Cold = 0;
	public final static byte Close = 0x02;
	public final static byte Temperature = 0x01;
	public final static byte YAFState = 0x02;
	public final static byte DCFan = 0x01;
	public final static byte ACFan = 0x02;
	// private com.dwin.navy.serialportapi.SerialPort temperControlPort;
	// private SerialPort temperControlPort;
	// private SerialPort485 temperControlPort;
	private SerialPort temperControlPort;
	private Context context;
	// private byte[] temperControlRXbuf;
	private byte[] temperControlTXbuf = new byte[15];
	private boolean needSetTemper = true;
	private boolean receiveSetTemper = false;
	private boolean needQueryState = true;
	private boolean receiveQueryState = false;
	private int machine;
	private byte set_left_type;// 1��ʾ���䣬2��ʾ����
	private int set_left_temperature;
	private byte set_right_type;
	private int set_right_temperature;
	private byte queryType;
	private int left_type;// 1��ʾ���䣬2��ʾ����
	private int left_temperature;
	private int right_type;
	private int right_temperature;
	private int compressorState;// 1��ʾ��11��ʾ�쳣
	private int fanState;

	private MachineStateDao mDao;

	public int getCompressorState() {
		return compressorState;
	}

	public void setCompressorState(int compressorState) {
		this.compressorState = compressorState;
	}

	public int getFanState() {
		return fanState;
	}

	public void setFanState(int fanState) {
		this.fanState = fanState;
	}

	public boolean isNeedSetTemper() {
		return needSetTemper;
	}

	public void setNeedSetTemper(boolean needSetTemper) {
		this.needSetTemper = needSetTemper;
	}

	public boolean isNeedQueryState() {
		return needQueryState;
	}

	public void setNeedQueryState(boolean needQueryState) {
		this.needQueryState = needQueryState;
	}

	public boolean isReceiveQueryState() {
		return receiveQueryState;
	}

	public void setReceiveQueryState(boolean receiveQueryState) {
		this.receiveQueryState = receiveQueryState;
	}

	public boolean isReceiveSetTemper() {
		return receiveSetTemper;
	}

	public void setReceiveSetTemper(boolean receiveSetTemper) {
		this.receiveSetTemper = receiveSetTemper;
	}

	public int getLeft_type() {
		return left_type;
	}

	public void setLeft_type(int left_type) {
		this.left_type = left_type;
	}

	public int getLeft_temperature() {
		return left_temperature;
	}

	public void setLeft_temperature(int left_temperature) {
		this.left_temperature = left_temperature;
	}

	public int getRight_type() {
		return right_type;
	}

	public void setRight_type(int right_type) {
		this.right_type = right_type;
	}

	public int getRight_temperature() {
		return right_temperature;
	}

	public void setRight_temperature(int right_temperature) {
		this.right_temperature = right_temperature;
	}

	public byte getSet_left_type() {
		return set_left_type;
	}

	public void setSet_left_type(byte set_left_type) {
		this.set_left_type = set_left_type;
	}

	public int getSet_left_temperature() {
		return set_left_temperature;
	}

	public void setSet_left_temperature(int set_left_temperature) {
		this.set_left_temperature = set_left_temperature;
	}

	public byte getSet_right_type() {
		return set_right_type;
	}

	public void setSet_right_type(byte set_right_type) {
		this.set_right_type = set_right_type;
	}

	public int getSet_right_temperature() {
		return set_right_temperature;
	}

	public void setSet_right_temperature(int set_right_temperature) {
		this.set_right_temperature = set_right_temperature;
	}

	// public TemperControl(SerialPort485 serialPort485, Context context) {
	// super();
	// // TODO Auto-generated constructor stub
	// temperControlPort = serialPort485;
	// this.context = context;
	// mDao = new MachineStateDaoImpl(context);
	// }

	public TemperControl(SerialPort serialPort485, Context context) {
		super();
		// TODO Auto-generated constructor stub
		temperControlPort = serialPort485;
		this.context = context;
		mDao = new MachineStateDaoImpl(context);
	}

	public void setTemperature(int counter) {
		updatePar(counter);
		temperControlTXbuf[0] = 0x24;
		if (counter == 1) {
			temperControlTXbuf[1] = 0x03;
		} else {
			temperControlTXbuf[1] = (byte) (0x65 + (counter - 2));
		}
		temperControlTXbuf[2] = 0x0C;
		temperControlTXbuf[3] = 'T';
		temperControlTXbuf[4] = set_left_type;
		// if (set_left_temperature < 0) {
		// set_left_temperature = Math.abs(set_left_temperature) | 1 << 7;
		// }
		temperControlTXbuf[5] = (byte) set_left_temperature;

		temperControlTXbuf[6] = set_right_type;
		// if (set_right_temperature < 0) {
		// set_right_temperature = Math.abs(set_right_temperature) | 1 << 7;
		// }
		temperControlTXbuf[7] = (byte) set_right_temperature;
		temperControlTXbuf[8] = (byte) fanState;
		byte CRC = temperControlTXbuf[0];
		for (int i = 1; i < 10; i++) {
			CRC = (byte) (CRC ^ temperControlTXbuf[i]);
		}
		temperControlTXbuf[10] = CRC;
		temperControlTXbuf[11] = 0x21;
		temperControlPort.sendData(temperControlTXbuf, 12);
	}

	public void queryState(int counter) {
		temperControlTXbuf[0] = 0x24;
		if (counter == 1) {
			temperControlTXbuf[1] = 0x03;
		} else {
			temperControlTXbuf[1] = (byte) (0x65 + (counter - 2));
		}
		temperControlTXbuf[2] = 0x0C;
		temperControlTXbuf[3] = 'Q';
		for (int i = 4; i < 10; i++) {
			temperControlTXbuf[i] = 0;
		}
		byte CRC = temperControlTXbuf[0];
		for (int i = 1; i < 10; i++) {
			CRC = (byte) (CRC ^ temperControlTXbuf[i]);
		}
		temperControlTXbuf[10] = CRC;
		temperControlTXbuf[11] = 0x21;
		temperControlPort.sendData(temperControlTXbuf, 12);
	}

	public void updatePar(int counter) {
		MachineState queryMachineState = mDao.queryMachineState(counter);
		if (queryMachineState.getLeftState() == 0) {
			setSet_left_type(Cold);
		} else if (queryMachineState.getLeftState() == 1) {
			setSet_left_type(Hot);
		} else if (queryMachineState.getLeftState() == 2) {
			setSet_left_type(Close);
		}
		setSet_left_temperature(queryMachineState.getLeftSetTemp());
		if (counter == 1) {
			if (queryMachineState.getRightState() == 0) {
				setSet_right_type(Cold);
			} else if (queryMachineState.getRightState() == 1) {
				setSet_right_type(Hot);
			} else if (queryMachineState.getRightState() == 2) {
				setSet_right_type(Close);
			}
			setSet_right_temperature(queryMachineState.getRightSetTemp());
		}
		if (queryMachineState.getFanType() == 1) {
			setFanState(DCFan);
		} else if (queryMachineState.getFanType() == 2) {
			setFanState(ACFan);
		}

	}
}
