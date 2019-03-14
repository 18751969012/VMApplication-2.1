package com.njust.major.bean;

import com.major.SerialPort;
import com.njust.major.util.Util;

import java.util.List;

public class MdbBean {
	public final static byte ACCEPT_ALL_BILLS = 0x001f;
	// public final static byte ACCEPT_NO_BILLS = 0x0000;
	public final static byte ESCROW_ALL_BILLS = 0x001f;
	// public final static byte ESCROW_NO_BILLS = 0x0000;
	private SerialPort mdbPort;
	private byte[] MDBTXbuf = new byte[10];
	private boolean chargeComplete = false;
	private boolean escrowed = false;
	private boolean escrowIntoBox = false;
	private boolean handleReback = false;
	private boolean rebackEscrow = false;
	private boolean receivecheck = false;
	private int oneYuanInBoxNum;
	private int fiveJiaoBoxbeNum;
	private int oneYuanInBoxNumb;
	private int fiveYuanInBoxNum;
	private int tenYuanInBoxNum;
	private int twentyYuanInBoxNum;
	private int oneYuanInTubeNum;
	private int fiveJiaoInTubeNum;
	private int chargeNum5;
	private int chargeNum10;
	private int currentPay = 0;
	private List<Cash> cashList;
	private Cash fjInBox;
	private Cash oyInBox;
	private Cash oyInBoxb;
	private Cash fyInBox;
	private Cash tyInBox;
	private Cash twyInBox;
	private Cash fjInTube;
	private Cash oyInTube;


	public int getOneYuanInBoxNum() {
		return oneYuanInBoxNum;
	}

	public void setOneYuanInBoxNum(int oneYuanInBoxNum) {
		this.oneYuanInBoxNum = oneYuanInBoxNum;
	}

	public int getFiveJiaoBoxbeNum() {
		return fiveJiaoBoxbeNum;
	}

	public void setFiveJiaoBoxbeNum(int fiveJiaoBoxbeNum) {
		this.fiveJiaoBoxbeNum = fiveJiaoBoxbeNum;
	}

	public int getOneYuanInBoxNumb() {
		return oneYuanInBoxNumb;
	}

	public int getChargeNum5() {
		return chargeNum5;
	}

	public void setChargeNum5(int chargeNum5) {
		this.chargeNum5 = chargeNum5;
	}

	public int getChargeNum10() {
		return chargeNum10;
	}

	public void setChargeNum10(int chargeNum10) {
		this.chargeNum10 = chargeNum10;
	}

	public void setOneYuanInBoxNumb(int oneYuanInBoxNumb) {
		this.oneYuanInBoxNumb = oneYuanInBoxNumb;
	}

	public int getFiveYuanInBoxNum() {
		return fiveYuanInBoxNum;
	}

	public void setFiveYuanInBoxNum(int fiveYuanInBoxNum) {
		this.fiveYuanInBoxNum = fiveYuanInBoxNum;
	}

	public int getTenYuanInBoxNum() {
		return tenYuanInBoxNum;
	}

	public void setTenYuanInBoxNum(int tenYuanInBoxNum) {
		this.tenYuanInBoxNum = tenYuanInBoxNum;
	}

	public int getTwentyYuanInBoxNum() {
		return twentyYuanInBoxNum;
	}

	public void setTwentyYuanInBoxNum(int twentyYuanInBoxNum) {
		this.twentyYuanInBoxNum = twentyYuanInBoxNum;
	}

	public Cash getFjInBox() {
		return fjInBox;
	}

	public void setFjInBox(Cash fjInBox) {
		this.fjInBox = fjInBox;
	}

	public Cash getOyInBox() {
		return oyInBox;
	}

	public void setOyInBox(Cash oyInBox) {
		this.oyInBox = oyInBox;
	}

	public Cash getOyInBoxb() {
		return oyInBoxb;
	}

	public void setOyInBoxb(Cash oyInBoxb) {
		this.oyInBoxb = oyInBoxb;
	}

	public Cash getFyInBox() {
		return fyInBox;
	}

	public void setFyInBox(Cash fyInBox) {
		this.fyInBox = fyInBox;
	}

	public Cash getTyInBox() {
		return tyInBox;
	}

	public void setTyInBox(Cash tyInBox) {
		this.tyInBox = tyInBox;
	}

	public Cash getTwyInBox() {
		return twyInBox;
	}

	public void setTwyInBox(Cash twyInBox) {
		this.twyInBox = twyInBox;
	}

	public Cash getFjInTube() {
		return fjInTube;
	}

	public void setFjInTube(Cash fjInTube) {
		this.fjInTube = fjInTube;
	}

	public Cash getOyInTube() {
		return oyInTube;
	}

	public void setOyInTube(Cash oyInTube) {
		this.oyInTube = oyInTube;
	}

	public MdbBean(SerialPort port) {
		super();
		// TODO Auto-generated constructor stub
		mdbPort = port;
	}

	public boolean isChargeComplete() {
		return chargeComplete;
	}

	public void setChargeComplete(boolean chargeComplete) {
		this.chargeComplete = chargeComplete;
	}

	public boolean isEscrowed() {
		return escrowed;
	}

	public void setEscrowed(boolean escrowed) {
		this.escrowed = escrowed;
	}

	public boolean isEscrowIntoBox() {
		return escrowIntoBox;
	}

	public void setEscrowIntoBox(boolean escrowIntoBox) {
		this.escrowIntoBox = escrowIntoBox;
	}

	public boolean isHandleReback() {
		return handleReback;
	}

	public void setHandleReback(boolean handleReback) {
		this.handleReback = handleReback;
	}

	public boolean isRebackEscrow() {
		return rebackEscrow;
	}

	public void setRebackEscrow(boolean rebackEscrow) {
		this.rebackEscrow = rebackEscrow;
	}

	public boolean isReceivecheck() {
		return receivecheck;
	}

	public void setReceivecheck(boolean receivecheck) {
		this.receivecheck = receivecheck;
	}

	public int getOneYuanInTubeNum() {
		return oneYuanInTubeNum;
	}

	public void setOneYuanInTubeNum(int oneYuanInTubeNum) {
		this.oneYuanInTubeNum = oneYuanInTubeNum;
	}

	public int getFiveJiaoInTubeNum() {
		return fiveJiaoInTubeNum;
	}

	public void setFiveJiaoInTubeNum(int fiveJiaoInTubeNum) {
		this.fiveJiaoInTubeNum = fiveJiaoInTubeNum;
	}

	public int getCurrentPay() {
		return currentPay;
	}

	public void setCurrentPay(int currentPay) {
		this.currentPay = currentPay;
	}

	public List<Cash> getCashList() {
		return cashList;
	}

	public void setCashList(List<Cash> cashList) {
		this.cashList = cashList;
	}

	public void setBillType(byte acceptType, byte escrowType) {
		MDBTXbuf[0] = 0x34;
		MDBTXbuf[1] = (byte) (acceptType >> 8);
		MDBTXbuf[2] = (byte) acceptType;
		MDBTXbuf[3] = (byte) (escrowType >> 8);
		MDBTXbuf[4] = (byte) escrowType;
		mdbPort.sendData(MDBTXbuf, 5);
	}

	public void pouseBill() {
		Util.WriteFile("MdbBean pouseBill");
		MDBTXbuf[0] = 0x34;
		MDBTXbuf[1] = (byte) (0x80);
		MDBTXbuf[2] = 0;
		MDBTXbuf[3] = (byte) (0x80);
		MDBTXbuf[4] = 0;
		mdbPort.sendData(MDBTXbuf, 5);
	}

	public void resetBillMachine() {
		Util.WriteFile("resetBill");
		MDBTXbuf[0] = 0x30; // ֽ�һ�����
		mdbPort.sendData(MDBTXbuf, 1);
	}

	public void acceptBill(boolean accept) {
		Util.WriteFile("MdbBean acceptBill");
		MDBTXbuf[0] = 0x35;
		if (accept) {
			MDBTXbuf[1] = 0x01;
		} else {
			MDBTXbuf[1] = 0x00;
		}
		mdbPort.sendData(MDBTXbuf, 2);
	}

	public void queryBillMachine() {
		MDBTXbuf[0] = 0x36;
		mdbPort.sendData(MDBTXbuf, 1);
	}

	public void setCoinType(boolean acceptAll) {
		MDBTXbuf[0] = 0x0C;
		if (acceptAll) {
			MDBTXbuf[1] = (byte) 0xff;
			MDBTXbuf[2] = (byte) 0xff;
			MDBTXbuf[3] = (byte) 0xff;
			MDBTXbuf[4] = (byte) 0xff;
		} else {
			MDBTXbuf[1] = (byte) 0x00;
			MDBTXbuf[2] = (byte) 0x00;
			MDBTXbuf[3] = (byte) 0x00;
			MDBTXbuf[4] = (byte) 0x00;
		}
		mdbPort.sendData(MDBTXbuf, 5);
	}

	public void resetCoinMachine() {
		MDBTXbuf[0] = 0x08;
		mdbPort.sendData(MDBTXbuf, 1);
	}

	public void queryTubeStatus() {
		MDBTXbuf[0] = 0x0A;
		mdbPort.sendData(MDBTXbuf, 1);
	}

	public void coinReback(int money) {
		MDBTXbuf[0] = (byte) 0x0f;
		MDBTXbuf[1] = (byte) 0x02;
		MDBTXbuf[2] = (byte) ((int) (money / 50));
		mdbPort.sendData(MDBTXbuf, 3);
	}

	public void chechReback() {
		MDBTXbuf[0] = (byte) 0x0f;
		MDBTXbuf[1] = (byte) 0x03;
		mdbPort.sendData(MDBTXbuf, 2);
	}

	public void checkCoin() {
		MDBTXbuf[0] = (byte) 0x0A;
		mdbPort.sendData(MDBTXbuf, 1);
	}
}
