package com.njust.major.thread;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.major.SerialPort;
import com.njust.major.SCM.BigEyes;
import com.njust.major.SCM.DrinkGate;
import com.njust.major.SCM.TemperControl;
import com.njust.major.bean.InfoBean;
import com.njust.major.bean.MachineState;
import com.njust.major.bean.MdbBean;
import com.njust.major.bean.Transaction;
import com.njust.major.dao.InfoDao;
import com.njust.major.dao.MachineStateDao;
import com.njust.major.dao.TransactionDao;
import com.njust.major.dao.impl.InfoDaoImpl;
import com.njust.major.dao.impl.MachineStateDaoImpl;
import com.njust.major.dao.impl.TransactionDaoImpl;
import com.njust.major.util.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.njust.major.VMApplication.counter;
import static com.njust.major.VMApplication.keyTest;

public class SCMThread extends Thread {
	private Context context;
	private MachineStateDao mDao;
	private TransactionDao tDao;
	private InfoDao iDao;
	private SimpleDateFormat sdf;
	private boolean flag = false;
	private byte eyesType;
	private int eyesNo;
	private int roadNo;
	private int byNo;
	private int remianBitNo;
	private int boxTempCtl[] = new int[6];
	private int boxUse[] = new int[6];
	private MachineState machineState;
	private int mCount = 0;
	private int count = 20;
	private int delay = 150;
	private int sendTimes = 5;
	private int oldDoorState = 2;
	private boolean stop = false;
	private boolean opendoor = false;
	private SerialPort serialPort485;
	private byte[] RxBuff;
	public static BigEyes bigEyes;
	public DrinkGate drinkGate;
	public TemperControl temperControl;
	private MdbBean mdb;
	private int assistState = 0;


	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}


	public int[] getBoxUse() {
		return boxUse;
	}

	public SCMThread(Context context, MdbThread mdbThread) {
		super();
		this.context = context;
		mdb = mdbThread.mdbBean;
		mDao = new MachineStateDaoImpl(context);
		tDao = new TransactionDaoImpl(context);
		iDao = new InfoDaoImpl(context);
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		machineState = new MachineState();
		serialPort485 = new SerialPort(1, 38400, 8, 'n', 1);
		bigEyes = new BigEyes(serialPort485, context);
		drinkGate = new DrinkGate(serialPort485, context);
		temperControl = new TemperControl(serialPort485, context);
		Uri uri = Uri.parse("content://vm/Mupdate");
		context.getContentResolver().registerContentObserver(uri, false,
				new VMContentObserver(new Handler()));
	}

	@Override
	public void run() {
		super.run();
		while (true) {
			if (flag) {
				if (boxUse[1] == 1) {
					if (bigEyes.isNeedQueryButton()) {
						// 查询是否按下按钮、以及开关门状态
						queryButton();
					}
					if (bigEyes.isNeedControlSale()) {
						// 亮售货灯
						controlSale();
					}
					if (bigEyes.isNeedControlShort()) {
						// 亮缺货灯
						controlShort();
					}
					if (bigEyes.isNeedControlLight()) {
						// 亮照明灯
						controlLight();
					}

					if (bigEyes.isNeedControlPouse()) {
						// 亮暂停灯
						controlPouse();
					}
					// 电磁铁
					if (drinkGate.isNeedCheckHavingDrink() || opendoor) {
						// 查询货道板缺货反馈
						checkHavingDrink();
					}
					if (drinkGate.isNeedDrinkOut()) {
						// 出货
						drinkOut();
					}
				}

				// 辅板独立柜门控
				if (boxUse[1] == 0 && boxUse[2] == 1) {
					checkGate_assit();
				}

				// 辅板
				if (drinkGate.isNeedLight()) {
					needLight_assit();
				}
				if (drinkGate.isNeedDrinkOut_assist()) {
					drinkOut_assist();
				}
				// 温控板
				if (mCount == 0) {
					for (int j = 1; j < 6; j++) {
						if (boxUse[j] == 1) {
							if (boxTempCtl[j] == 1) {
								if (temperControl.isNeedSetTemper()) {
									SetTemper(j);
								}
								if (temperControl.isNeedQueryState()) {
									QueryState(j);
								}
							}
							if (j != 1) {
								drinkCheck_assist(j);
							}
						}
					}
				}
				mCount++;
				// System.out.println("count:" + mCount);
				if (count == mCount) {
					mCount = 0;
				}
				// 一键循环测试
				if (keyTest) {
					if(counter != 1){
						keyTest(counter);
					}
					keyTest = false;
				}
			}
			SystemClock.sleep(10);
		}
	}

	public void initSCM() {
		assistState = 0;
		for (int i = 1; i < 6; i++) {
			MachineState machineState = mDao.queryMachineState(i);
			if (1 == i) {
				eyesType = (byte) machineState.getEyesType();
				eyesNo = machineState.getEyesNo();
				roadNo = machineState.getRoadNo();
				byNo = roadNo / 8;
				remianBitNo = roadNo - byNo * 8;
			}
			boxTempCtl[i] = machineState.getTemperCtl();
			boxUse[i] = machineState.getUse();
			if (boxUse[i] == 1) {
				mDao.updateLight(machineState.getLight(), i);
			}
		}
		bigEyes.setEyesType(eyesType);
		bigEyes.setEyesNo(eyesNo);
		bigEyes.initLightMap();
		drinkGate.setRoadNo(roadNo);
		drinkGate.initShortRoadMap();

		bigEyes.setPressID(0);
		bigEyes.shutDownAllSaleLight();
		bigEyes.setNeedControlSale(true);
		bigEyes.setNeedQueryButton(true);
		drinkGate.setNeedCheckHavingDrink(true);
		temperControl.setNeedQueryState(true);
		temperControl.setNeedSetTemper(true);
		setStop(false);

		mDao.updateState(1, 1);
		for (int k = 1; k <= 5; k++) {
			mDao.updateCounterState(k, 0);
		}
	}

	private void queryButton() {
		for (int i = 0; i < sendTimes; i++) {
			RxBuff = serialPort485.receiveData();
			if (RxBuff != null) {
				printReceive();
				getData();
				if (RxBuff[0] == 0x24) {
					// int length = RxBuff[2] / 16 * 10 + RxBuff[2] % 16;
					int length = 12;
					if ((RxBuff.length >= length) && (RxBuff[11] == 0x21)) {
						if (RxBuff[1] == 0x01) {
							if (RxBuff[5] == 1) {
								int pressID = RxBuff[4];
								if (pressID != 0) {
									if(bigEyes.isMdbPutInMoney()){
										try {
											Thread.sleep(200);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
									bigEyes.setBigEyesKeyDown(true);
									System.out.println("按下按键："
											+ Integer.toHexString(pressID));
									Util.WriteFile("按下按键："
											+ Integer.toHexString(pressID));
									Transaction ts = tDao
											.queryLastedTransaction();
									if (ts == null || ts.getComplete() != 0
											|| (!stop)
											&& (ts.getComplete() == 0)) {
										if (bigEyes.getPressID() != pressID) {
											// mdb.pouseBill();
											List<InfoBean> list = iDao
													.queryByDrinkID(pressID);
											InfoBean bean = null;
											for (InfoBean infoBean : list) {
												if (infoBean.getState() == 0) {
													bean = infoBean;
													break;
												}
											}
											if (bean != null) {
												for (int k = 1; k <= 42; k++) {
													bigEyes.getSaleLightMap()
															.put(k, 0);
												}
												bigEyes.setPressID(pressID);
												bigEyes.getSaleLightMap().put(
														pressID, 1);
												controlSale();
												if (ts == null
														|| ts.getComplete() != 0) {
													Transaction transaction = new Transaction();
													transaction.setComplete(0);
													transaction.setType(2);
													String time = sdf
															.format(Calendar
																	.getInstance()
																	.getTime());
													transaction
															.setBeginTime(time);
													transaction.setEndTime("");
													int drinkid = bean
															.getDrinkID();
													int price = bean.getPrice();
													int positionID = bean
															.getPositionID();
													transaction
															.setDrinkID(drinkid);
													transaction.setPrice(price);
													transaction.setPayedAll(0);
													transaction.setCharge(0);
													transaction
															.setPositionID(positionID);
													transaction.setCounter(1);
													if (!stop) {
														tDao.addTransaction(transaction);
														bigEyes.setBigEyesKeyDown(false);
														Intent intent = new Intent();
														intent.setAction("Serialport_Action_Select_Goods");
														context.sendBroadcast(intent);
														Util.WriteFile("开始新交易");
													}
													// myTimer.stopTimer();
													// myTimer.startTimer();
													bigEyes.setBigEyesKeyDown(false);
												} else if (ts.getComplete() == 0) {
													int drinkid = bean
															.getDrinkID();
													int price = bean.getPrice();
													ts.setDrinkID(drinkid);
													ts.setPrice(price);
													ts.setPositionID(bean
															.getPositionID());
													ts.setCounter(1);
													if (!stop) {
														Util.WriteFile("存储交易3：" + ts.toString());
														tDao.updateTransaction(ts);
														bigEyes.setBigEyesKeyDown(false);
														Intent intent = new Intent();
														intent.setAction("Serialport_Action_Select_Goods");
														context.sendBroadcast(intent);
													}
													bigEyes.setBigEyesKeyDown(false);
												}
											}
											bigEyes.setBigEyesKeyDown(false);
											mdb.setBillType(
													mdb.ACCEPT_ALL_BILLS,
													mdb.ESCROW_ALL_BILLS);
										}
									}
									bigEyes.setBigEyesKeyDown(false);
								}
							}
							int doorState = RxBuff[5];
							if (doorState == 1) {
								doorState = 0;
							} else if (doorState == 0) {
								doorState = 1;
							}
							if (doorState != oldDoorState) {
								mDao.updateDoorState(doorState);
								Intent intent = new Intent();
								intent.setAction("Serialport_Action_Door_State");
								context.sendBroadcast(intent);
								if (doorState == 1) {
									opendoor = true;
									Util.WriteFile("open door");
									mdb.resetCoinMachine();
									SystemClock.sleep(1000);
									mdb.resetBillMachine();
									// mdb.pouseBill();
								} else if (doorState == 0) {
									opendoor = false;
									Util.WriteFile("close door");
									Intent start = new Intent();
									start.setAction("Avm.START_BACKGROUND");
									context.sendBroadcast(start);
								}
							}
							oldDoorState = doorState;
							break;
						} else {
							bigEyes.queryButton();
							SystemClock.sleep(delay);
						}
					} else {
						bigEyes.queryButton();
						SystemClock.sleep(delay);
					}
				} else {
					bigEyes.queryButton();
					SystemClock.sleep(delay);
				}
			} else {
				bigEyes.queryButton();
				SystemClock.sleep(delay);
			}
			if (i == sendTimes - 1) {
				// bigEyes.setNeedQueryButton(false);
				mDao.updateCounterState(1, 1);
				System.out.println("大眼睛板通信故障啦");
				Util.WriteFile("dyj");
				// android.os.Process.killProcess(android.os.Process.myPid());
			}
		}
	}

	private void controlSale() {
		bigEyes.setReceiveControlSale(false);
		for (int i = 0; i < sendTimes; i++) {
			RxBuff = serialPort485.receiveData();
			if (RxBuff != null) {
				printReceive();
				getData();
				if (RxBuff[0] == 0x24) {
					// int length = RxBuff[2] / 16 * 10 + RxBuff[2] % 16;
					int length = 12;
					if ((RxBuff.length >= length) && (RxBuff[11] == 0x21)) {
						if (RxBuff[1] == 0x01) {
							bigEyes.setReceiveControlSale(true);
							break;
						} else {
							bigEyes.controlSale();
							SystemClock.sleep(delay);
						}
					} else {
						bigEyes.controlSale();
						SystemClock.sleep(delay);
					}
				} else {
					bigEyes.controlSale();
					SystemClock.sleep(delay);
				}
			} else {
				bigEyes.controlSale();
				SystemClock.sleep(delay);
			}
			if (i == 4) {
				mDao.updateCounterState(1, 1);
				System.out.println("大眼睛板通信故障啦");
			}
		}
		bigEyes.setNeedControlSale(false);
	}

	private void controlShort() {
		for (int i = 0; i < sendTimes; i++) {
			RxBuff = serialPort485.receiveData();
			if (RxBuff != null) {
				printReceive();
				getData();
				if (RxBuff[0] == 0x24) {
					// int length = RxBuff[2] / 16 * 10 + RxBuff[2] % 16;
					int length = 12;
					if ((RxBuff.length >= length) && (RxBuff[11] == 0x21)) {
						if (RxBuff[1] == 0x01) {
							break;
						} else {
							bigEyes.controlShort();
							SystemClock.sleep(delay);
						}
					} else {
						bigEyes.controlShort();
						SystemClock.sleep(delay);
					}
				} else {
					bigEyes.controlShort();
					SystemClock.sleep(delay);
				}
			} else {
				bigEyes.controlShort();
				SystemClock.sleep(delay);
			}
			if (i == sendTimes - 1) {
				mDao.updateCounterState(1, 1);
				System.out.println("大眼睛板通信故障啦");
			}
		}
		bigEyes.setNeedControlShort(false);
	}

	private void controlLight() {
		bigEyes.setReceiveControlLight(false);
		for (int i = 0; i < sendTimes; i++) {
			RxBuff = serialPort485.receiveData();
			if (RxBuff != null) {
				printReceive();
				getData();
				if (RxBuff[0] == 0x24) {
					// int length = RxBuff[2] / 16 * 10 + RxBuff[2] % 16;
					int length = 12;
					if ((RxBuff.length >= length) && (RxBuff[11] == 0x21)) {
						if (RxBuff[1] == 0x01) {
							bigEyes.setNeedControlLight(false);
							break;
						} else {
							bigEyes.controlLight();
							SystemClock.sleep(delay);
						}
					} else {
						bigEyes.controlLight();
						SystemClock.sleep(delay);
					}
				} else {
					bigEyes.controlLight();
					SystemClock.sleep(delay);
				}
			} else {
				bigEyes.controlLight();
				SystemClock.sleep(delay);
			}
			if (i == sendTimes - 1) {
				mDao.updateCounterState(1, 1);
				bigEyes.setNeedControlLight(false);
				System.out.println("大眼睛板通信故障啦");
			}
		}
	}

	private void controlPouse() {
		bigEyes.setReceiveControlPouse(false);
		for (int i = 0; i < sendTimes; i++) {
			RxBuff = serialPort485.receiveData();
			if (RxBuff != null) {
				printReceive();
				getData();
				if (RxBuff[0] == 0x24) {
					// int length = RxBuff[2] / 16 * 10 + RxBuff[2] % 16;
					int length = 12;
					if ((RxBuff.length >= length) && (RxBuff[11] == 0x21)) {
						if (RxBuff[1] == 0x01) {
							bigEyes.setReceiveControlPouse(true);
							bigEyes.setNeedControlPouse(false);
							break;
						} else {
							bigEyes.controlPouse();
							SystemClock.sleep(delay);
						}
					} else {
						bigEyes.controlPouse();
						SystemClock.sleep(delay);
					}
				} else {
					bigEyes.controlPouse();
					SystemClock.sleep(delay);
				}
			} else {
				bigEyes.controlPouse();
				SystemClock.sleep(delay);
			}
			if (i == sendTimes - 1) {
				mDao.updateCounterState(1, 1);
				bigEyes.setNeedControlPouse(false);
				System.out.println("大眼睛板通信故障啦");
			}
		}
	}

	private void checkHavingDrink() {
		for (int q = 0; q < sendTimes; q++) {
			RxBuff = serialPort485.receiveData();
			if (RxBuff != null) {
				printReceive();
				getData();
				if (RxBuff[0] == 0x24) {
					// int length = RxBuff[2] / 16 * 10 + RxBuff[2] % 16;
					int length = 12;
					if ((RxBuff.length >= length) && (RxBuff[11] == 0x21)) {
						if (RxBuff[3] == 'E') {
							shortRoadToEyes();
							if (!opendoor) {
								drinkGate.setNeedCheckHavingDrink(false);
							}
							break;
						} else {
							System.out.println("009");
							drinkGate.checkHavingDrink();
							SystemClock.sleep(delay);
						}
					} else {
						System.out.println("008");
						drinkGate.checkHavingDrink();
						SystemClock.sleep(delay);
					}
				} else {
					System.out.println("007");
					drinkGate.checkHavingDrink();
					SystemClock.sleep(delay);
				}

			} else {
				System.out.println("006");
				drinkGate.checkHavingDrink();
				SystemClock.sleep(delay);
			}
			if (q == sendTimes - 1) {
				mDao.updateCounterState(1, 2);
				// mDao.updateState(1, 0);
				// drinkGate.setNeedCheckHavingDrink(false);
				System.out.println("货道板通信故障啦");
				Util.WriteFile("-----------------hdb");
			}
		}
	}

	private void drinkOut() {
		Util.WriteFile("调用drinkOUT");
		drinkGate.setReceiveDrinkOut(false);
		drinkGate.drinkOut();
		SystemClock.sleep(delay);

		RxBuff = serialPort485.receiveData();
		boolean flag = false;
		if (RxBuff != null) {
			printReceive();
			getData();
			if (RxBuff[0] == 0x24) {
				// int length = RxBuff[2] / 16 * 10 + RxBuff[2] % 16;
				int length = 12;
				if ((RxBuff.length >= length) && (RxBuff[11] == 0x21)) {
					if (RxBuff[3] == 'E') {
						flag = true;
						Util.WriteFile("drinkOut()593");
						drinkGate.setNeedDrinkOut(false);
						drinkGate.setReceiveDrinkOut(true);
						synchronized (drinkGate) {
							drinkGate.notifyAll();
						}
						Util.WriteFile("drinkOut()599");
					}
				}
			}
		}
		if (!flag) {
			mDao.updateCounterState(1, 2);
			// mDao.updateState(1, 0);
			drinkGate.setNeedDrinkOut(false);
			Util.WriteFile("货道板通信故障");
		}
	}

	private void checkGate_assit() {
		for (int i = 0; i < sendTimes; i++) {
			RxBuff = serialPort485.receiveData();
			if (RxBuff != null) {
				printReceive();
				getData();
				if (RxBuff[0] == 0x24) {
					// int length = RxBuff[2] / 16 * 10 + RxBuff[2] % 16;
					int length = 12;
					if ((RxBuff.length >= length) && (RxBuff[11] == 0x21)) {
						int doorState = RxBuff[2];
						if (doorState == 1) {
							doorState = 0;
						} else if (doorState == 0) {
							doorState = 1;
						}
						if (doorState != oldDoorState) {
							mDao.updateDoorState(doorState);
							Intent intent = new Intent();
							intent.setAction("Serialport_Action_Door_State");
							context.sendBroadcast(intent);
							if (doorState == 1) {
								opendoor = true;
								Util.WriteFile("open door2");
								mdb.resetCoinMachine();
								SystemClock.sleep(1000);
								mdb.resetBillMachine();
							} else if (doorState == 0) {
								opendoor = false;
								Intent start = new Intent();
								Util.WriteFile("close door2");
								start.setAction("Avm.START_BACKGROUND");
								context.sendBroadcast(start);
							}
						}
						oldDoorState = doorState;
						break;
					} else {
						drinkGate.checkGate_assist();
						SystemClock.sleep(delay);
					}
				} else {
					drinkGate.checkGate_assist();
					SystemClock.sleep(delay);
				}
			} else {
				drinkGate.checkGate_assist();
				SystemClock.sleep(delay);
			}
			if (i == sendTimes - 1) {
				Util.WriteFile("辅板出故障");
			}
		}
	}

	private void needLight_assit() {
		for (int i = 0; i < sendTimes; i++) {
			RxBuff = serialPort485.receiveData();
			if (RxBuff != null) {
				printReceive();
				getData();
				if (RxBuff[0] == 0x24) {
					// int length = RxBuff[2] / 16 * 10 + RxBuff[2] % 16;
					int length = 12;
					if ((RxBuff.length >= length) && (RxBuff[11] == 0x21)) {
						drinkGate.setNeedLight(false);
						break;
					} else {
						drinkGate.light_assist();
						SystemClock.sleep(delay);
					}
				} else {
					drinkGate.light_assist();
					SystemClock.sleep(delay);
				}
			} else {
				drinkGate.light_assist();
				SystemClock.sleep(delay);
			}
			if (i == sendTimes - 1) {
				drinkGate.setNeedDrinkOut_assist(false);
				System.out.println("辅板通信故障啦");
				mDao.updateCounterState(drinkGate.getLightCounter(), 4);
			}
		}
	}

	private void drinkOut_assist() {
		Util.WriteFile("drinkOut_assist()700");
		if(drinkGate.getRaster() == 0){
			Util.WriteFile("drinkOut_assist()702");
			drinkGate.setReceiveDrinkOut_assist(false);
			for (int i = 0; i < sendTimes; i++) {
				Util.WriteFile("drinkOut_assist()705");
				RxBuff = serialPort485.receiveData();
				if (RxBuff != null) {
					printReceive();
					getData();
					if (RxBuff[0] == 0x24) {
						// int length = RxBuff[2] / 16 * 10 + RxBuff[2] % 16;
						int length = 12;
						Util.WriteFile("drinkOut_assist()713");
						if ((RxBuff.length >= length) && (RxBuff[11] == 0x21)) {
							drinkGate.setNeedDrinkOut_assist(false);
							drinkGate.setReceiveDrinkOut_assist(true);
							Util.WriteFile("drinkOut_assist()717");
							synchronized (drinkGate) {
								drinkGate.notifyAll();
							}
							Util.WriteFile("drinkOut_assist()721");
							SystemClock.sleep(2500);
							break;
						} else {
							Util.WriteFile("drinkOut_assist()725");
							drinkGate.drinkOut_assist();
							SystemClock.sleep(delay);
						}
					} else {
						Util.WriteFile("drinkOut_assist()730");
						drinkGate.drinkOut_assist();
						SystemClock.sleep(delay);
					}
				} else {
					Util.WriteFile("drinkOut_assist()735");
					drinkGate.drinkOut_assist();
					SystemClock.sleep(delay);
				}
				if (i == sendTimes - 1) {
					drinkGate.setNeedDrinkOut_assist(false);
					mDao.updateCounterState(drinkGate.getCounter(), 4);
					Util.WriteFile("辅板出货通信故障");
				}
			}
		}else{
			Util.WriteFile("drinkOut_assist()746");
			drinkGate.setReceiveDrinkOut_assist(false);
			for (int i = 0; i < sendTimes; i++) {
				Util.WriteFile("drinkOut_assist()749");
				RxBuff = serialPort485.receiveData();
				if (RxBuff != null) {
					printReceive();
					getData();
					if (RxBuff[0] == 0x24) {
						// int length = RxBuff[2] / 16 * 10 + RxBuff[2] % 16;
						int length = 12;
						Util.WriteFile("drinkOut_assist()757");
						if ((RxBuff.length >= length) && (RxBuff[11] == 0x21)) {
							boolean waitAction = true;
							while(waitAction){
								Util.WriteFile("drinkOut_assist()761");
								drinkGate.drinkOut_assist_query();
								SystemClock.sleep(delay);
								RxBuff = serialPort485.receiveData();
								if (RxBuff != null) {
									StringBuilder str1 = new StringBuilder();
									for (byte aRec : RxBuff) {
										str1.append(Integer.toHexString(aRec&0xFF)).append(" ");
									}
									Util.WriteFile("drinkOut_assist()771："+ str1);
									printReceive();
									getData();
									if (RxBuff[3] == 0x00) {//动作执行中
									} else if (RxBuff[3] == 0x01 || RxBuff[3] == 0x03) {//出货成功
										drinkGate.setNeedDrinkOut_assist(false);
										drinkGate.setReceiveDrinkOut_assist(true);
										drinkGate.setOutGoodsSuccess(true);
										Util.WriteFile("drinkOut_assist()779");
										synchronized (drinkGate) {
											drinkGate.notifyAll();
										}
										Util.WriteFile("drinkOut_assist()783");
										waitAction = false;
									} else {//出货失败
										Transaction transaction = tDao.queryLastedTransaction();
										iDao.updateInfoState(transaction.getCounter(),transaction.getPositionID(),2);
										drinkGate.setNeedDrinkOut_assist(false);
										drinkGate.setReceiveDrinkOut_assist(true);
										drinkGate.setOutGoodsSuccess(false);
										Util.WriteFile("drinkOut_assist()791");
										synchronized (drinkGate) {
											drinkGate.notifyAll();
										}
										Util.WriteFile("drinkOut_assist()795");
										waitAction = false;
									}
								}
							}
							SystemClock.sleep(200);
							break;
						} else {
							Util.WriteFile("drinkOut_assist()803");
							drinkGate.drinkOut_assist();
							SystemClock.sleep(delay);
						}
					} else {
						Util.WriteFile("drinkOut_assist()808");
						drinkGate.drinkOut_assist();
						SystemClock.sleep(delay);
					}
				} else {
					Util.WriteFile("drinkOut_assist()813");
					drinkGate.drinkOut_assist();
					SystemClock.sleep(delay);
				}
				if (i == sendTimes - 1) {
					drinkGate.setNeedDrinkOut_assist(false);
					mDao.updateCounterState(drinkGate.getCounter(), 4);
					Util.WriteFile("辅板出货通信故障");
				}
			}
		}
	}
	private void keyTest(int counter) {
		byte[] drinkGateTXbuf = new byte[12];
		drinkGateTXbuf[0] = (byte)0x24;
		drinkGateTXbuf[1] = (byte)(0x80 + (counter - 2));
		drinkGateTXbuf[2] = (byte)0x0C;
		drinkGateTXbuf[3] = (byte)0x03;
		drinkGateTXbuf[4] = (byte)0x00;
		drinkGateTXbuf[5] = (byte)0x01;
		drinkGateTXbuf[6] = (byte)0x02;
		drinkGateTXbuf[7] = (byte)0x03;
		drinkGateTXbuf[8] = (byte)0x04;
		drinkGateTXbuf[9] = (byte)0x05;
		byte CRC = drinkGateTXbuf[0];
		for (int i = 1; i < 10; i++) {
			CRC = (byte) (CRC ^ drinkGateTXbuf[i]);
		}
		drinkGateTXbuf[10] = CRC;
		drinkGateTXbuf[11] = 0x21;
		serialPort485.sendData(drinkGateTXbuf, 12);
	}
	private void drinkCheck_assist(int ct) {
		int j = 0;
		for (j = 0; j < sendTimes; j++) {
			RxBuff = serialPort485.receiveData();
			if (RxBuff != null) {
				printReceive();
				getData();
				if (RxBuff[0] == 0x24) {
					// int length = RxBuff[2] / 16 * 10 +
					// RxBuff[2] % 16;
					int length = 12;
					if ((RxBuff.length >= length) && (RxBuff[11] == 0x21)) {
						break;
					} else {
						drinkGate.check_assist(ct);
						SystemClock.sleep(delay);
					}
				} else {
					drinkGate.check_assist(ct);
					SystemClock.sleep(delay);
				}
			} else {
				drinkGate.check_assist(ct);
				SystemClock.sleep(delay);
			}
		}
		if (j == sendTimes) {
			// if (assistState == 0) {
			System.out.println("辅板通信故障啦");
			iDao.updateInfoState(ct, 9);
			mDao.updateCounterState(ct, 0);
			// assistState = 1;
			// }
		} else {
			// if (assistState == 1) {
			iDao.updateInfoState(ct, 0);
			mDao.updateCounterState(ct, 1);
			// assistState = 0;
			// }
		}

	}

	private void SetTemper(int counter) {
		for (int i = 0; i < sendTimes; i++) {
			RxBuff = serialPort485.receiveData();
			if (RxBuff != null) {
				printReceive();
				getData();
				if (RxBuff[0] == 0x24) {
					// int length = RxBuff[2] / 16 * 10 + RxBuff[2] % 16;
					int length = 12;
					if ((RxBuff.length >= length) && (RxBuff[11] == 0x21)) {
						if (RxBuff[3] == 'T') {
							machineState.setLeftState(RxBuff[4]);
							machineState.setLeftTemperature(RxBuff[5]);
							machineState.setRightState(RxBuff[6]);
							machineState.setRightTemperature(RxBuff[7]);
							mDao.updateTemperature(machineState, counter);
							break;
						} else {
							temperControl.setTemperature(counter);
							SystemClock.sleep(delay);
						}
					} else {
						temperControl.setTemperature(counter);
						SystemClock.sleep(delay);
					}
				} else {
					temperControl.setTemperature(counter);
					SystemClock.sleep(delay);
				}
			} else {
				temperControl.setTemperature(counter);
				SystemClock.sleep(delay);
			}
			if (i == sendTimes - 1) {
				System.out.println("温控板通信故障啦counter:" + counter);
				// temperControl.setNeedSetTemper(false);
				mDao.updateCounterState(counter, 3);
			}
		}
	}

	private void QueryState(int counter) {
		for (int i = 0; i < sendTimes; i++) {
			RxBuff = serialPort485.receiveData();
			if (RxBuff != null) {
				printReceive();
				getData();
				if (RxBuff[0] == 0x24) {
					// int length = RxBuff[2] / 16 * 10 + RxBuff[2] % 16;
					int length = 12;
					if ((RxBuff.length >= length) && (RxBuff[11] == 0x21)) {
						if (RxBuff[3] == 'Q') {
							machineState.setYJTemp(RxBuff[4]);
							machineState.setYLTemp(RxBuff[5]);
							if (RxBuff[6] == 0x01) {
								machineState.setLFan(1);
							} else if (RxBuff[6] == 0x11) {
								machineState.setLFan(0);
							}

							if (RxBuff[7] == 0x01) {
								machineState.setRFan(1);
							} else if (RxBuff[7] == 0x11) {
								machineState.setRFan(0);
							}

							if (RxBuff[8] == 0x01) {
								machineState.setYJFan(1);
							} else if (RxBuff[8] == 0x11) {
								machineState.setYJFan(0);
							}

							if (RxBuff[9] == 0x01) {
								machineState.setYLFan(1);
							} else if (RxBuff[9] == 0x11) {
								machineState.setYLFan(0);
							}
							mDao.updateTemperControlState(machineState, counter);
							temperControl.setReceiveQueryState(true);
							break;
						} else {
							temperControl.queryState(counter);
							SystemClock.sleep(delay);
						}
					} else {
						temperControl.queryState(counter);
						SystemClock.sleep(delay);
					}
				} else {
					temperControl.queryState(counter);
					SystemClock.sleep(delay);
				}
			} else {
				temperControl.queryState(counter);
				SystemClock.sleep(delay);
			}
			if (i == sendTimes - 1) {
				// temperControl.setNeedQueryState(false);
				mDao.updateCounterState(counter, 3);
				System.out.println("温控板通信故障啦counter:" + counter);
			}
		}
	}

	private void shortRoadToEyes() {
		int change = 1;
		for (int i = 0; i < byNo; i++) {
			for (int j = 7; j >= 0; j--) {
				int a = (RxBuff[4 + i] >> j) & 1;
				int b = 8 - j + i * 8;
				drinkGate.getShortRoadMap().put(b, a);
				List<InfoBean> l = iDao.query(1, b);
				if (!l.isEmpty()) {
					InfoBean ib = l.get(0);
					if (a == 0) {
						if (ib.getState() == 0) {
							iDao.updateInfoState(1, b, 1);
						}
						List<InfoBean> list = iDao.queryByDrinkID(ib
								.getDrinkID());
						int aa = 0;
						for (InfoBean infoBean : list) {
							if (infoBean.getState() == 0) {
								aa = 1;
								break;
							}
						}
						if (aa == 0) {
							for (int k = 0; k < l.size(); k++) {
								int drinkid = l.get(k).getDrinkID();
								bigEyes.getShortLightMap().put(drinkid, 1);
							}
							change = 0;
						}
					} else if (a == 1) {
						if (ib.getState() == 1 && ib.getStock() > 1) {
							iDao.updateInfoState(1, b, 0);
						}
						for (int k = 0; k < l.size(); k++) {
							int drinkid = l.get(k).getDrinkID();
							bigEyes.getShortLightMap().put(drinkid, 0);
						}
						change = 0;
					}
				} else {
					InfoBean in = new InfoBean(0, b, b, 3500, 0, 1, 0, 0, 200,
							1);
					iDao.add(in);
				}
			}
		}
		for (int j = 7; j > 7 - remianBitNo; j--) {
			int a = (RxBuff[4 + byNo] >> j) & 1;
			int b = 8 - j + byNo * 8;
			drinkGate.getShortRoadMap().put(b, a);
			List<InfoBean> l = iDao.query(1, b);
			if (!l.isEmpty()) {
				InfoBean ib = l.get(0);
				if (a == 0) {
					if (ib.getState() == 0) {
						iDao.updateInfoState(1, b, 1);
					}
					List<InfoBean> list = iDao.queryByDrinkID(ib.getDrinkID());
					int aa = 0;
					for (InfoBean infoBean : list) {
						if (infoBean.getState() == 0) {
							aa = 1;
							break;
						}
					}
					if (aa == 0) {
						for (int k = 0; k < l.size(); k++) {
							int drinkid = l.get(k).getDrinkID();
							bigEyes.getShortLightMap().put(drinkid, 1);
						}
						change = 0;
					}
				} else if (a == 1) {
					if (ib.getState() == 1 && ib.getStock() > 1) {
						iDao.updateInfoState(1, b, 0);
					}
					for (int k = 0; k < l.size(); k++) {
						int drinkid = l.get(k).getDrinkID();
						bigEyes.getShortLightMap().put(drinkid, 0);
					}
					change = 0;
				}
			} else {
				InfoBean in = new InfoBean(0, b, b, 3500, 0, 1, 0, 0, 200, 1);
				iDao.add(in);
			}
		}
		if (change == 0) {
			bigEyes.setNeedControlShort(true);
			for (int k = 1; k <= 42; k++) {
				System.out.print(k
						+ ":"
						+ Integer.toHexString(drinkGate.getShortRoadMap()
						.get(k)) + " ");
			}
			for (int k = 1; k <= 42; k++) {
				System.out.print(k
						+ ":"
						+ Integer
						.toHexString(bigEyes.getShortLightMap().get(k))
						+ " ");
			}
		}
	}

	private void printReceive() {
		if (RxBuff.length > 0) {
			String a = Integer.toHexString(RxBuff[0]) + " ";
			for (int i = 1; i < RxBuff.length; i++) {
				a += Integer.toHexString(RxBuff[i]) + " ";
			}
			System.out.println(a + "  总字节：" + RxBuff.length);
		}
	}

	public void printReceive2() {
		if (RxBuff != null && RxBuff.length > 0) {
			String a = Integer.toHexString(RxBuff[0]) + " ";
			for (int i = 1; i < RxBuff.length; i++) {
				a += Integer.toHexString(RxBuff[i]) + " ";
			}
			Util.WriteFile(a);
		}
	}

	private void getData() {
		if (RxBuff.length >= 13) {
			boolean falg1 = true;
			boolean flag2 = false;
			int z = 0;
			for (int y = 0; y < RxBuff.length; y++) {
				if (falg1) {
					if (RxBuff[y] == 0x24) {
						flag2 = true;
						falg1 = false;
						z = y;
					}
				}
				if (flag2) {
					RxBuff[y - z] = RxBuff[y];
				}
			}
		}
	}

	private class VMContentObserver extends ContentObserver {

		public VMContentObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			// temperControl.updatePar();
			for (int k = 0; k < 6; k++) {
				MachineState state = mDao.queryMachineState(k);
				if (k == 0) {
					if (state.getLockRoad() == 1) {
						List<InfoBean> list = iDao.queryAll();
						int size = list.size();
						for (int i = 0; i < size; i++) {
							InfoBean bean = list.get(i);
							if (bean.getState() == 2) {
								bigEyes.getPouseLightMap().put(
										bean.getDrinkID(), 1);
							} else if (bean.getState() != 2) {
								bigEyes.getPouseLightMap().put(
										bean.getDrinkID(), 0);
							}
						}
						bigEyes.setNeedControlPouse(true);
					}
				} else if (k == 1) {
					if (boxUse[k] == 1) {
						int light = state.getLight();
						if (light == 1) {
							bigEyes.setLight(true);
						} else if (light == 0) {
							bigEyes.setLight(false);
						}
						bigEyes.setNeedControlLight(true);
					}
				} else {
					if (boxUse[k] == 1) {
						int light = state.getLight();
						if (light == 1) {
							drinkGate.setLight(true);
						} else if (light == 0) {
							drinkGate.setLight(false);
						}
						drinkGate.setLightCounter(k);
						drinkGate.setNeedLight(true);
					}
				}
			}
		}
	}
}
