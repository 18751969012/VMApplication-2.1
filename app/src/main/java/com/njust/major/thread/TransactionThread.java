package com.njust.major.thread;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.njust.major.CTimer;
import com.njust.major.MyTimer;
import com.njust.major.SCM.BigEyes;
import com.njust.major.SCM.DrinkGate;
import com.njust.major.SCM.TemperControl;
import com.njust.major.VMApplication;
import com.njust.major.bean.InfoBean;
import com.njust.major.bean.MdbBean;
import com.njust.major.bean.Transaction;
import com.njust.major.dao.CashDao;
import com.njust.major.dao.InfoDao;
import com.njust.major.dao.TransactionDao;
import com.njust.major.dao.impl.CashDaoImpl;
import com.njust.major.dao.impl.InfoDaoImpl;
import com.njust.major.dao.impl.TransactionDaoImpl;
import com.njust.major.util.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TransactionThread extends Thread {
	private Context context;
	private boolean flag = false;
	private boolean flag_receiveOutGoods = false;
	private Transaction transaction;
	private MyTimer myTimer;
	private InfoDao iDao;
	private TransactionDao tDao;
	private CashDao cDao;
	public List<InfoBean> positionList;
	private int payedAll = 0;
	private int oldPayed = 0;
	private BigEyes bigEyes;
	private DrinkGate drinkGate;
	private TemperControl temperControl;
	private MdbThread mdbThread;
	private MdbBean mdb;
	private SimpleDateFormat sdf;
	private SCMThread scmThread;
	private CTimer cTimer;
	private CTimer cTimer2;
	private int resetBillMachineNo = 0;
	private boolean resetBill = false;
	private Timer mTimer = new Timer();
	private TimerTask mTimerTask;












	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public void setFlag_receiveOutGoods(boolean flag) {
		this.flag_receiveOutGoods = flag;
	}

	public TransactionThread(Context context, SCMThread scmThread,
                             MdbThread mdbThread) {
		super();
		this.context = context;
		this.scmThread = scmThread;
		this.mdbThread = mdbThread;
		this.mdb = mdbThread.mdbBean;
		myTimer = new MyTimer(context);
		iDao = new InfoDaoImpl(context);
		tDao = new TransactionDaoImpl(context);
		cDao = new CashDaoImpl(context);
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		bigEyes = scmThread.bigEyes;
		drinkGate = scmThread.drinkGate;
		temperControl = scmThread.temperControl;
		Uri uri = Uri.parse("content://vm/t");
		context.getContentResolver().registerContentObserver(uri, true,
				new VMContentObserver(new Handler()));
		cTimer = new CTimer(20000);
		cTimer2 = new CTimer(3000);
	}

	public void initTransaction() {
		updateCashInfo();
		Transaction transaction = tDao.queryLastedTransaction();
		if (transaction != null) {
			if (transaction.get_id() > 65530) {
				tDao.deleteTransaction();
			} else if (transaction.getComplete() == 0) {
				transaction.setType(1);
				transaction.setComplete(3);
				Util.WriteFile("存储交易5：" + transaction.toString());

				tDao.updateTransaction(transaction);
				if (mdbThread.transaction != null) {
					mdbThread.transaction.setComplete(1);
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
			transaction = tDao.queryLastedTransaction();
			Log.i("happy","已经在onChange里查询了最新交易"+ transaction.toString());
			Util.WriteFile("transaction发生变动：" + transaction);
			// System.out.println(transaction);
			if (transaction != null) {
				if (transaction.getType() != 9) {
					if (transaction.getComplete() == 1
							|| transaction.getComplete() == 2) {
						if (transaction.getCharge() != transaction
								.getPayedAll()) {
							Intent intent = new Intent();
							intent.setAction("Serialport_Action_Out_Goods");
							intent.putExtra("transaction_id", transaction.get_id());
							context.sendBroadcast(intent);
							mdbThread.mdbBufferNo = 0;
						}

						Intent intent1 = new Intent();
						intent1.setAction("Serialport_Action_RECEIVE_MONEY");
						intent1.putExtra("receive_total_money", 0);
						context.sendBroadcast(intent1);
						mdbThread.mdbBufferNo = 0;
						if (scmThread.getBoxUse()[1] == 1) {
							bigEyes.setPressID(0);
							bigEyes.shutDownAllSaleLight();
							bigEyes.setNeedControlSale(true);
							bigEyes.setReceiveControlSale(false);
							// while (!bigEyes.isReceiveControlSale()) {
							// }
							bigEyes.setNeedQueryButton(true);
							drinkGate.setNeedCheckHavingDrink(true);
						}
						temperControl.setNeedQueryState(true);
						temperControl.setNeedSetTemper(true);
					} else if (transaction.getComplete() == 0) {
						if (transaction.getPositionID() == 0) {
							bigEyes.setPressID(0);
						}
						if (transaction.getPositionID() != 0) {
							myTimer.stopTimer();
							myTimer.startTimer();
						}
						if (transaction.getPositionID() != 0
								&& (transaction.getPayedAll() >= transaction
										.getPrice())) {
							if (isChargeEnough(transaction.getPayedAll()
									- transaction.getPrice())) {
								// mdb.setCoinType(false);
								// if (!mdb.isEscrowed()) {
								// mdb.pouseBill();
								// SystemClock.sleep(500);
								// }
								myTimer.stopTimer();
								scmThread.setStop(true);
								bigEyes.setNeedQueryButton(false);
								drinkGate.setNeedCheckHavingDrink(false);
								temperControl.setNeedQueryState(false);
								temperControl.setNeedSetTemper(false);
							} else {
								Intent intent = new Intent();
								intent.setAction("Serialport_Action_NoCharge");
								context.sendBroadcast(intent);
							}
						}
					}
				}
			}
			System.out.println(transaction);
		}
	}

	@Override
	public void run() {
		super.run();
		while (true) {
			Log.i("happy","VMApplication的值为：" + VMApplication.Flag);
			if(VMApplication.Flag){
				transaction = tDao.queryLastedTransaction();
				Log.i("happy","已经在主线程里查询了最新交易" + transaction.toString());
				Util.WriteFile("交易信息：" + transaction);
				if (transaction != null) {
					if (transaction.getType() != 9) {
						if (transaction.getComplete() == 1 || transaction.getComplete() == 2) {
							if (scmThread.getBoxUse()[1] == 1) {
								bigEyes.setPressID(0);
								bigEyes.shutDownAllSaleLight();
								bigEyes.setNeedControlSale(true);
								bigEyes.setReceiveControlSale(false);
								// while (!bigEyes.isReceiveControlSale()) {
								// }
								bigEyes.setNeedQueryButton(true);
								drinkGate.setNeedCheckHavingDrink(true);
							}
							temperControl.setNeedQueryState(true);
							temperControl.setNeedSetTemper(true);
						} else if (transaction.getComplete() == 0) {
							if (transaction.getPositionID() == 0) {
								bigEyes.setPressID(0);
							}
							if (transaction.getPositionID() != 0) {
								myTimer.stopTimer();
								myTimer.startTimer();
							}
							if (transaction.getPositionID() != 0 && (transaction.getPayedAll() >= transaction.getPrice()) && isChargeEnough(transaction.getPayedAll() - transaction.getPrice())) {
								myTimer.stopTimer();
								scmThread.setStop(true);
								bigEyes.setNeedQueryButton(false);
								drinkGate.setNeedCheckHavingDrink(false);
								temperControl.setNeedQueryState(false);
								temperControl.setNeedSetTemper(false);
							}
						}
					}
				}
				VMApplication.Flag = false;
			}
			if (flag) {
				runMyLogic();
			}
			SystemClock.sleep(100);
		}
	}

	public void runMyLogic() {
		if (transaction != null && transaction.getComplete() == 0) {
			if (transaction.getType() != 9) {
				payedAll = transaction.getPayedAll();
				if (payedAll > 0) {
					myTimer.stopTimer();
				}
				if ((payedAll > oldPayed) && (transaction.getPositionID() == 0)) {
					positionList = iDao.queryAll();
					for (InfoBean infoBean : positionList) {
						if (infoBean.getState() == 0) {
							if (payedAll >= infoBean.getPrice()) {
								if (isChargeEnough(payedAll
										- infoBean.getPrice())) {
									bigEyes.getSaleLightMap().put(
											infoBean.getDrinkID(), 1);
								}
							}
						}
					}
					oldPayed = payedAll;
					bigEyes.setReceiveControlSale(false);
					bigEyes.setNeedControlSale(true);
				}
				if (transaction.getPositionID() != 0
						&& (payedAll >= transaction.getPrice())) {
					if (isChargeEnough(payedAll - transaction.getPrice())) {
						int positionID = transaction.getPositionID();
						int counter = transaction.getCounter();
						int gateActionTime = iDao.query(counter, positionID)
								.get(0).getGateActionTime();
						drinkGate.setGateActionTime(gateActionTime);
						drinkGate.setCounter(counter);
						drinkGate.setPressedDrinkPositionID(positionID);

						String log = "出貨：" + transaction.getCounter() + " "
								+ transaction.getPositionID();
						iDao.updateInfoStock(counter, positionID);
//						// 出货前先把纸币从缓存收入纸币箱
//						if (mdb.isEscrowed()) {
//							for(int i=0;i<5;i++){
//								mdb.acceptBill(true);
//								SystemClock.sleep(300);
//								if (mdb.isEscrowIntoBox()) {
//									Util.WriteFile("escrowIntoBox1");
//									mdb.setEscrowed(false);
//									mdb.setEscrowIntoBox(false);
//									mdb.pouseBill();
//									SystemClock.sleep(100);
//									break;
//								}
//							}
//						}
						int sucess = 1;
						if (counter == 1) {
							drinkGate.setReceiveDrinkOut(false);
							drinkGate.setNeedDrinkOut(true);
							synchronized (drinkGate) {
//								SystemClock.sleep(1000);
								try {
									drinkGate.wait(5500);
									Util.WriteFile("drinkGate.isReceiveDrinkOut()"
											+ drinkGate.isReceiveDrinkOut());
									if (!drinkGate.isReceiveDrinkOut()) {
										throw new InterruptedException();
									}
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									Util.WriteFile("货道板不回");
									scmThread.printReceive2();
									log += "  不成功";
									sucess = 0;
									drinkGate.setReceiveDrinkOut(true);
								}
							}
							while (!drinkGate.isReceiveDrinkOut()) {
							}
							drinkGate.setReceiveDrinkOut(false);
						} else if (counter == 2 || counter == 3 || counter == 4) {
							drinkGate.setCounter(counter);
							drinkGate.setReceiveDrinkOut_assist(false);
							drinkGate.setNeedDrinkOut_assist(true);
							synchronized (drinkGate) {
								SystemClock.sleep(1000);
								try {
									drinkGate.wait(4500);
									Util.WriteFile("drinkGate.isReceiveDrinkOut_assist()"
											+ drinkGate
													.isReceiveDrinkOut_assist());
									if (!drinkGate.isReceiveDrinkOut_assist()) {
										throw new InterruptedException();
									}
									if(!drinkGate.isOutGoodsSuccess()){
										sucess = 0;
									}
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									Util.WriteFile("辅板不回");
									scmThread.printReceive2();
									log += "  不成功";
									sucess = 0;
									drinkGate.setReceiveDrinkOut_assist(true);
								}
							}
							while (!drinkGate.isReceiveDrinkOut_assist()) {
							}

							drinkGate.setReceiveDrinkOut_assist(false);
							drinkGate.setOutGoodsSuccess(true);
						}
						Util.WriteFile(log);

						int charge = 0;
//						SystemClock.sleep(3000);
						drinkGate.setPressedDrinkPositionID(0);
						bigEyes.setPressID(0);
						mdb.setCoinType(false);
						if (!mdb.isEscrowed()) {
							SystemClock.sleep(200);
							mdb.pouseBill();
							Util.WriteFile("pouseBill351");
							SystemClock.sleep(200);
						}
						payedAll = transaction.getPayedAll();
						if (sucess == 1) {
							// iDao.updateInfoStock(counter, positionID);
							charge = payedAll - transaction.getPrice();
							Util.WriteFile("payedAll:" + payedAll
									+ "　transaction.getPrice()："
									+ transaction.getPrice());
							if (mdb.isEscrowed()) {
								SystemClock.sleep(200);
								mdb.acceptBill(true);
								Util.WriteFile("acceptBill364");
								SystemClock.sleep(200);
								if (mdb.isEscrowIntoBox()) {
									Util.WriteFile("escrowIntoBox1");
								}
								SystemClock.sleep(200);
								mdb.setEscrowIntoBox(false);
								mdb.pouseBill();
								SystemClock.sleep(200);
							}
							mdb.setEscrowed(false);
							if (charge > 0) {
								SystemClock.sleep(200);
								mdb.setReceivecheck(false);
								mdb.coinReback(charge);
							}
//							SystemClock.sleep(300);
							transaction.setSuccessOrFailure(1);
							transaction.setComplete(1);
						} else if (sucess == 0) {
							charge = payedAll - transaction.getPrice();
							Util.WriteFile("payedAll:" + payedAll
									+ "　transaction.getPrice()："
									+ transaction.getPrice());
							if (mdb.isEscrowed()) {
//								SystemClock.sleep(400);
								mdb.acceptBill(true);
//								SystemClock.sleep(500);
								if (mdb.isEscrowIntoBox()) {
									Util.WriteFile("escrowIntoBox2");
								}
								// while (!mdb.escrowIntoBox) {
								// }
								SystemClock.sleep(150);
								mdb.setEscrowIntoBox(false);
								mdb.pouseBill();
								SystemClock.sleep(150);
							}
							mdb.setEscrowed(false);
							if (charge > 0) {
								SystemClock.sleep(200);
								mdb.setReceivecheck(false);
								mdb.coinReback(charge);
							}
							transaction.setSuccessOrFailure(0);
							transaction.setComplete(1);
						}

						String endTime = sdf.format(Calendar.getInstance()
								.getTime());
						transaction.setEndTime(endTime);
						transaction.setCharge(charge);
						transaction.setPositionID(positionID);
						// tDao.updateTransaction(transaction);

						oldPayed = 0;
						mdb.setCurrentPay(0);
						String detail = "" + transaction.getComplete() + "  "
								+ transaction.getCounter()
								+ transaction.getPositionID() + ",价格"
								+ transaction.getPrice() + ",支付"
								+ transaction.getPayedAll() + ",找零"
								+ transaction.getCharge() + "  ~" + charge;
						Util.WriteFile("存储交易6：" + transaction.toString());

						tDao.updateTransaction(transaction);
						// lDao.writeLog("工单", detail);
						if (transaction.getType() == 2) {
							Util.WriteFile("现金交易完成：" + detail);
							System.out.println("现金交易完成");
						} else if (transaction.getType() == 3) {
							Util.WriteFile("微信交易完成：" + detail);
							System.out.println("微信交易完成");
						} else if (transaction.getType() == 4) {
							Util.WriteFile("支付宝交易完成：" + detail);
							System.out.println("支付宝交易完成");
						} else if (transaction.getType() == 5) {
							Util.WriteFile("客非卡交易完成：" + detail);
							System.out.println("支客非卡交易完成");
						} else if (transaction.getType() == 6) {
							Util.WriteFile("备用易完成：" + detail);
							System.out.println("备用交易完成");
						}
						if (sucess == 1 && charge > 0) {
							Util.WriteFile("chargeOK");
							cTimer.startTimer();
							SystemClock.sleep(200);
							while (!mdb.isReceivecheck()) {
								if (cTimer.isTimeOut()) {
									mdb.setReceivecheck(true);
									Util.WriteFile("asd");
								}
								SystemClock.sleep(100);
							}
							cTimer.stopTimer();
							Util.WriteFile("ReceicchargeOK");
							mdb.setReceivecheck(false);
							Util.WriteFile("checkOK");
						}if (sucess == 0 && charge > 0) {
							Util.WriteFile("chargeOK2");
							while (!mdb.isReceivecheck()) {
							}
							Util.WriteFile("ReceicchargeOK2");
							mdb.setReceivecheck(false);
							mdb.setChargeComplete(false);
							mdb.chechReback();
							SystemClock.sleep(10);
							if (mdb.isChargeComplete()) {
								Util.WriteFile("receiveChargeComplete");
							}
							// while (!mdb.chargeComplete) {
							// }
							Util.WriteFile("checkOK3");
							mdb.setChargeComplete(false);
						}
						Util.WriteFile("发送检测硬币数量");
						mdb.checkCoin();
						SystemClock.sleep(800);
						Util.WriteFile("开启硬币投入");
						mdb.setCoinType(true);
						updateCashInfo();
						SystemClock.sleep(50);
						Util.WriteFile("开启纸币投入");
						mdb.setBillType(mdb.ACCEPT_ALL_BILLS,
										mdb.ESCROW_ALL_BILLS);
						SystemClock.sleep(50);
						bigEyes.setNeedQueryButton(true);
						drinkGate.setNeedCheckHavingDrink(true);
						scmThread.setStop(false);
					}
				}
			} else if (transaction.getType() == 9) {
				testOutGoods();
			}
		}
		handleReback();
	}

	private void handleReback() {
		if (mdb.isHandleReback()) {
			if (transaction != null && transaction.getComplete() == 0
					&& transaction.getPayedAll() != 0) {
				// if (mdb.transaction.getComplete() == 0) {
				mdb.setCoinType(false);
				myTimer.stopTimer();
				if (mdb.isEscrowed()) {
					SystemClock.sleep(100);
					mdb.acceptBill(false);

					cTimer2.startTimer();
					SystemClock.sleep(100);
					while (!mdb.isRebackEscrow()) {
						if (cTimer2.isTimeOut()) {
							mdb.setRebackEscrow(true);
							Util.WriteFile("timeout");
							mdb.setCurrentPay(0);
						}
						SystemClock.sleep(100);
					}
					cTimer2.stopTimer();
					SystemClock.sleep(100);
					mdb.pouseBill();
					Util.WriteFile("rebackEscrow2");

					mdb.setEscrowed(false);
					mdb.setRebackEscrow(false);
				} else {
//					SystemClock.sleep(300);
					mdb.pouseBill();
					SystemClock.sleep(100);
				}
				payedAll = mdb.getCurrentPay();
				// payedAll = transaction.getPayedAll();
				if (payedAll > 0) {
//					SystemClock.sleep(350);
					mdb.setReceivecheck(false);
					mdb.coinReback(payedAll);
					Util.WriteFile("chargeOK");
					cTimer.startTimer();
					SystemClock.sleep(100);
					while (!mdb.isReceivecheck()) {
						if (cTimer.isTimeOut()) {
							mdb.setReceivecheck(true);
							Util.WriteFile("asd");
						}
						SystemClock.sleep(100);
					}
					cTimer.stopTimer();
					Util.WriteFile("ReceicchargeOK");
					mdb.setReceivecheck(false);
					Util.WriteFile("checkOK");
					mdb.setChargeComplete(false);
				}
				String endTime = sdf.format(Calendar.getInstance().getTime());
				transaction.setEndTime(endTime);
				transaction.setPayedAll(payedAll);
				transaction.setCharge(payedAll);
				transaction.setComplete(1);
				Util.WriteFile("存储交易4：" + transaction.toString());

				tDao.updateTransaction(transaction);
				oldPayed = 0;
				mdb.setCurrentPay(0);
				System.out.println("手动退钱");
				Util.WriteFile("result" + transaction.getPayedAll() + " "
						+ transaction.getCharge());
				Intent intent = new Intent();
				intent.setAction("Avm.cancelTransaction");
				context.sendBroadcast(intent);
				bigEyes.setNeedQueryButton(true);
				drinkGate.setNeedCheckHavingDrink(true);
				scmThread.setStop(false);
     			SystemClock.sleep(50);
				mdb.checkCoin();
				SystemClock.sleep(800);
				mdb.setCoinType(true);
				updateCashInfo();
				SystemClock.sleep(50);
				mdb.setBillType(mdb.ACCEPT_ALL_BILLS, mdb.ESCROW_ALL_BILLS);
			}
			if (/*(transaction != null && transaction.getComplete() == 0 && transaction.getPayedAll() == 0)||*/
					(transaction != null && transaction.getComplete() != 0 && mdbThread.mdbBufferNo > 10)) {
				resetBillMachineNo++;
				if(resetBillMachineNo == 1){
					mdb.resetBillMachine();
					SystemClock.sleep(50);
					mdb.setBillType(mdb.ACCEPT_ALL_BILLS,
							mdb.ESCROW_ALL_BILLS);
					SystemClock.sleep(50);
					timeStart();
				}
			}
			mdb.setHandleReback(false);
		}
	}
	private void timeStart(){
		if(mTimerTask != null){
			mTimerTask.cancel();
			mTimerTask = null;
		}
		final int[] t = {0};
		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				t[0]++;
				if(t[0] == 25){//定时5s
					Util.WriteFile("5s定时到,再次开始重启纸币器开关");
					resetBillMachineNo=0;
					t[0] = 0;
					mTimerTask.cancel();
					mTimerTask = null;
				}
			}
		};
		mTimer.schedule(mTimerTask,200,200);
		Util.WriteFile("定时开启，5s后再次开始重启纸币器开关");
	}
	private void testOutGoods() {

		int positionID = transaction.getPositionID();
		int counter = transaction.getCounter();
		int gateActionTime = iDao.query(counter, positionID).get(0)
				.getGateActionTime();
		Util.WriteFile("positionID："+positionID+"  counter:"+counter+"  gateActionTime:"+gateActionTime);
		drinkGate.setGateActionTime(gateActionTime);
		drinkGate.setCounter(counter);
		drinkGate.setPressedDrinkPositionID(positionID);
		if (counter == 1) {
			drinkGate.setReceiveDrinkOut(false);
			drinkGate.setNeedDrinkOut(true);
			synchronized (drinkGate) {
				Util.WriteFile("testOutGoods610");
				try {
					Util.WriteFile("testOutGoods612");
					drinkGate.wait(5500);
					Util.WriteFile("testOutGoods614");
					if (!drinkGate.isReceiveDrinkOut()) {
						throw new InterruptedException();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					drinkGate.setReceiveDrinkOut(true);
				}
			}
			Util.WriteFile("testOutGoods623");
			drinkGate.setReceiveDrinkOut(false);
		} else if (counter == 2 || counter == 3 || counter == 4) {
			drinkGate.setCounter(counter);
			drinkGate.setReceiveDrinkOut_assist(false);
			drinkGate.setNeedDrinkOut_assist(true);
			synchronized (drinkGate) {
				Util.WriteFile("testOutGoods630");
				SystemClock.sleep(1000);
				try {
					Util.WriteFile("testOutGoods633");
					drinkGate.wait(4500);
					Util.WriteFile("testOutGoods635");
					if (!drinkGate.isReceiveDrinkOut_assist()) {
						throw new InterruptedException();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					drinkGate.setReceiveDrinkOut_assist(true);
				}
			}
			Util.WriteFile("testOutGoods644");
			drinkGate.setReceiveDrinkOut_assist(false);
			drinkGate.setOutGoodsSuccess(true);
		}

		drinkGate.setPressedDrinkPositionID(0);

		String endTime = sdf.format(Calendar.getInstance().getTime());
		transaction.setEndTime(endTime);
		transaction.setSuccessOrFailure(1);
		transaction.setComplete(1);
		Util.WriteFile("存储交易7：" + transaction.toString());
		tDao.updateTransaction(transaction);
		String detail = "" + transaction.getCounter()
				+ transaction.getPositionID() + ",价格" + transaction.getPrice()
				+ ",支付" + transaction.getPayedAll() + ",找零"
				+ transaction.getCharge();
		Util.WriteFile("测试出货完成：" + detail);
		System.out.println("测试出货完成");
		bigEyes.setNeedQueryButton(true);
		drinkGate.setNeedCheckHavingDrink(true);
		drinkGate.setOutGoodsSuccess(true);
	}

	private boolean isChargeEnough(int charge) {
		boolean resulst = false;
		if (charge % 50 != 0) {
			return resulst;
		}
		int _fiveJiaoNum = mdb.getFiveJiaoInTubeNum();
		int _oneYuanNum = mdb.getOneYuanInTubeNum();
		if (_fiveJiaoNum < 0) {
			mdb.setFiveJiaoInTubeNum(0);
		}
		if (_oneYuanNum < 0) {
			mdb.setOneYuanInTubeNum(0);
		}
		if (charge % 100 == 0) {
			if (_oneYuanNum >= (charge / 100)) {
				resulst = true;
			} else if (((charge / 100) - _oneYuanNum) * 2 <= _fiveJiaoNum) {
				resulst = true;
			}
		} else {
			if (_oneYuanNum >= (charge / 100)) {
				if (_fiveJiaoNum >= 1) {
					resulst = true;
				}
			} else if ((charge - _oneYuanNum * 100) / 50 <= _fiveJiaoNum) {
				resulst = true;
			}
		}
		return resulst;
	}

	private void updateCashInfo() {
		mdb.getFjInBox().setCount(mdb.getFiveJiaoBoxbeNum());
		mdb.getOyInBox().setCount(mdb.getOneYuanInBoxNum());
		mdb.getOyInBoxb().setCount(mdb.getOneYuanInBoxNumb());
		mdb.getFyInBox().setCount(mdb.getFiveYuanInBoxNum());
		mdb.getTyInBox().setCount(mdb.getTenYuanInBoxNum());
		mdb.getTwyInBox().setCount(mdb.getTwentyYuanInBoxNum());
		mdb.getFjInTube().setCount(mdb.getFiveJiaoInTubeNum());
		mdb.getOyInTube().setCount(mdb.getOneYuanInTubeNum());

		cDao.updateCash(mdb.getFjInBox());
		cDao.updateCash(mdb.getOyInBox());
		cDao.updateCash(mdb.getOyInBoxb());
		cDao.updateCash(mdb.getFyInBox());
		cDao.updateCash(mdb.getTyInBox());
		cDao.updateCash(mdb.getTwyInBox());
		cDao.updateCash(mdb.getFjInTube());
		cDao.updateCash(mdb.getOyInTube());
	}
}
