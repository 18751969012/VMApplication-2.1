package com.njust.major.thread;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.major.SerialPort;
import com.njust.major.SCM.BigEyes;
import com.njust.major.bean.Cash;
import com.njust.major.bean.MdbBean;
import com.njust.major.bean.Transaction;
import com.njust.major.dao.CashDao;
import com.njust.major.dao.MachineStateDao;
import com.njust.major.dao.TransactionDao;
import com.njust.major.dao.impl.CashDaoImpl;
import com.njust.major.dao.impl.MachineStateDaoImpl;
import com.njust.major.dao.impl.TransactionDaoImpl;
import com.njust.major.util.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MdbThread extends Thread {
	private Context context;
	private SerialPort mdbPort;
	public MdbBean mdbBean;
	private String RxbuffStr = "";
	private String RxData;
	private String RxData2;
	private String RxTemp;
	private int a = 0;
	private int b = 0;
	private SimpleDateFormat sdf;
	private TransactionDao tDao;
	private CashDao cDao;
	private MachineStateDao mDao;
	public Transaction transaction;
	private boolean flag = false;
	public int mdbBufferNo = 0;

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public MdbThread(Context context) {
		super();
		this.context = context;
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mdbPort = new SerialPort(4, 9600, 8, 'n', 1);
		mdbBean = new MdbBean(mdbPort);
		transaction = new Transaction();
		transaction.setComplete(1);
		tDao = new TransactionDaoImpl(context);
		cDao = new CashDaoImpl(context);
		mDao = new MachineStateDaoImpl(context);
	}

	@Override
	public void run() {
		super.run();
		while (true) {
			if (flag) {
				RxData = mdbPort.receiveData("gbk");
				if (b == 2) {
					a = 0;
					b = 0;
					Util.WriteFile("-------f");
					mdbBean.setReceivecheck(true);
				}
				if (RxData != null && !RxData.equals("")) {
					if (RxData.startsWith("30 09")) {
						RxData = RxData.substring(5);
					}
					if (RxData.startsWith(" 00") && RxData.length() <= 5) {
						RxData = "";
						SystemClock.sleep(2);
					}
					RxData2 = mdbPort.receiveData("gbk");
					if (RxData2 != null && !RxData2.equals("")) {
						RxData = RxData + RxData2;
						if (RxData.startsWith("30 09")) {
							RxData = RxData.substring(5);
						}
						// RxData2 = mdbPort.receiveData("gbk");
						// if (RxData2 != null && !RxData2.equals("")) {
						// RxData = RxData + RxData2;
						// }
					}
					if (RxData.endsWith("0") || RxData.endsWith("08")
							|| RxData.endsWith("08 ")
							|| RxData.endsWith("08 5") || RxData.endsWith("3")
							|| RxData.endsWith("30") || RxData.endsWith("30 ")
							|| RxData.endsWith("30 9")) {
						RxData2 = mdbPort.receiveData("gbk");
						if (RxData2 != null && !RxData2.equals("")) {
							RxData = RxData + RxData2;
						}
					}
					// if (RxData.contains("30")) {
					// RxData2 = mdbPort.receiveData("gbk");
					// if (RxData2 != null && !RxData2.equals("")) {
					// RxData = RxData + RxData2;
					// }
					// }
					Util.WriteFile("mdb:" + RxData);
					if (RxData.contains("30 09")) {
						mdbBufferNo = mdbBufferNo + 1;
					}
					RxbuffStr = RxData;
					myLogic(); // mdb接收数据处理
					RxbuffStr = "";
				} else {
					if (a == 10) {
						b++;
						System.out.println(b);
						Util.WriteFile("" + b);
					}
				}
			}
			SystemClock.sleep(2);
		}
	}

	public void initMdb() {
		initMdbPara();
		int doorState = mDao.queryMachineState(0).getDoorState();
		if (doorState == 0) {

			// 使能纸硬币器
			mdbBean.setBillType(mdbBean.ACCEPT_ALL_BILLS,
					mdbBean.ESCROW_ALL_BILLS);
			SystemClock.sleep(1000);
			mdbBean.setCoinType(true);
			SystemClock.sleep(1000);

		} else if (doorState == 1) {
			// 暂停纸硬币器
			mdbBean.setCoinType(false);
			SystemClock.sleep(1000);
			mdbBean.pouseBill();
			SystemClock.sleep(1000);
		}

		// 查询硬币器tube中硬币数量；
		mdbBean.checkCoin();
	}

	private void myLogic() {
		System.out.println("mdb:" + RxbuffStr);
		if (RxbuffStr.contains("08 01") && !RxbuffStr.contains("08 5")
				&& !RxbuffStr.contains("08 4") && !RxbuffStr.contains("30 9")) {
			// 退币按钮信号
			mdbBean.setHandleReback(true);
			return;
		}
		if (RxbuffStr.contains("08 02")) {
			// 找零中信号
			a = 10;
		}
		if (RxbuffStr.contains("30 8") || RxbuffStr.contains("30 9")
				|| RxbuffStr.contains("30 A") || RxbuffStr.contains("08 4")
				|| RxbuffStr.contains("08 5")) {

			// 初始化当前交易
			if(SCMThread.bigEyes.isBigEyesKeyDown()){
				try {
					Thread.sleep(850);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}//如果大眼睛被按下正在更新数据，需要等待
			SCMThread.bigEyes.setMdbPutInMoney(true);
			Transaction tst = tDao.queryLastedTransaction();
			if (tst == null || tst.getComplete() != 0) {
					mdbBean.setCurrentPay(0);
					transaction.setComplete(0);
					transaction.setType(2);
					String time = sdf.format(Calendar.getInstance().getTime());
					transaction.setBeginTime(time);
					transaction.setEndTime("");
					transaction.setDrinkID(0);
					transaction.setPrice(0);
					transaction.setPayedAll(0);
					transaction.setCharge(0);
					transaction.setPositionID(0);
					transaction.setCounter(0);
					tDao.addTransaction(transaction);
					SystemClock.sleep(20);
				SCMThread.bigEyes.setMdbPutInMoney(false);
			} else {
				// 有未完成交易，在原交易基础上更新数据
				mdbBean.setCurrentPay(tst.getPayedAll());
				transaction = tst;
				SCMThread.bigEyes.setMdbPutInMoney(false);
			}
			if (RxbuffStr.contains("30 8")) {
				// 收到缓存的纸币进入钱箱信号
				int index = RxbuffStr.indexOf("30 8");
				RxbuffStr = RxbuffStr.substring(index);
				if (!RxbuffStr.equals("") && RxbuffStr.length() >= 5) {
					String t = RxbuffStr.substring(4, 5);
					if ("9".compareTo(t) >= 0 && "9".compareTo(t) <= 9) {
						int billType = Integer.parseInt(t);
						switch (billType) {
						case 0:
							mdbBean.setOneYuanInBoxNumb(mdbBean
									.getOneYuanInBoxNumb() + 1);
							break;
						case 1:
							mdbBean.setFiveYuanInBoxNum(mdbBean
									.getFiveYuanInBoxNum() + 1);
							break;
						case 2:
							mdbBean.setTenYuanInBoxNum(mdbBean
									.getTenYuanInBoxNum() + 1);
							break;
						case 3:
							mdbBean.setTwentyYuanInBoxNum(mdbBean
									.getTwentyYuanInBoxNum() + 1);
							break;
						}
					}
					mdbBean.setEscrowIntoBox(true);
				}
			}
			if (RxbuffStr.contains("30 9")) {
				// 收到投入纸币进缓存的信号
				int index = RxbuffStr.indexOf("30 9");
				RxTemp = RxbuffStr.substring(index);
				if (!RxTemp.equals("") && RxTemp.length() >= 5) {
					int billType = Integer.parseInt(RxTemp.substring(4, 5));
					int addPay = 0;
					switch (billType) {
					case 0:
						addPay = 100;
						break;
					case 1:
						addPay = 500;
						break;
					case 2:
						addPay = 1000;
						break;
					case 3:
						addPay = 2000;
						break;
					}
					mdbBean.setCurrentPay(mdbBean.getCurrentPay() + addPay);
					RxbuffStr = RxbuffStr.substring(0, index)
							+ RxbuffStr.substring(index + 8);
				}
				mdbBean.setEscrowed(true);
			}
			if (RxbuffStr.contains("30 A")) {
				// 缓存的纸币退还信号
				int index = RxbuffStr.indexOf("30 A");
				RxbuffStr = RxbuffStr.substring(index);
				if (!RxbuffStr.equals("") && RxbuffStr.length() >= 5) {
					int billType = Integer.parseInt(RxbuffStr.substring(4, 5));
					int temp = 0;
					switch (billType) {
					case 0:
						temp = 100;
						break;
					case 1:
						temp = 500;
						break;
					case 2:
						temp = 1000;
						break;
					case 3:
						temp = 2000;
						break;
					}
					int cPay = mdbBean.getCurrentPay();
					if (cPay >= temp) {
						mdbBean.setCurrentPay(cPay - temp);
					}
				} else {
					RxbuffStr = RxbuffStr + mdbPort.receiveData("gbk");
					if (!RxbuffStr.equals("") && RxbuffStr.length() >= 5) {
						int billType = Integer.parseInt(RxbuffStr.substring(4,
								5));
						int temp = 0;
						switch (billType) {
						case 0:
							temp = 100;
							break;
						case 1:
							temp = 500;
							break;
						case 2:
							temp = 1000;
							break;
						case 3:
							temp = 2000;
							break;
						}
						int cPay = mdbBean.getCurrentPay();
						if (cPay >= temp) {
							mdbBean.setCurrentPay(cPay - temp);
						}
					}
				}
				mdbBean.setRebackEscrow(true);
			}
			if (RxbuffStr.contains("08 5")) {
				// 投入硬币（进入tube）
				int index = RxbuffStr.indexOf("08 5");
				RxbuffStr = RxbuffStr.substring(index);
				if (!RxbuffStr.equals("") && RxbuffStr.length() >= 5) {
					int billType = Integer.parseInt(RxbuffStr.substring(4, 5));
					int num = 0;
					if (RxbuffStr.length() >= 8) {
						num = Integer.parseInt(RxbuffStr.substring(6, 8), 16);
					}
					int addPay = 0;
					switch (billType) {
					case 0:
						addPay = 50;
						if (RxbuffStr.length() >= 8) {
							mdbBean.setFiveJiaoInTubeNum(num);
						}
						// fiveJiaoInTubeNum++;
						break;
					case 1:
						addPay = 100;
						if (RxbuffStr.length() >= 8) {
							mdbBean.setOneYuanInTubeNum(num);
						}
						// oneYuanInTubeNum++;
						break;
					}
					mdbBean.setCurrentPay(mdbBean.getCurrentPay() + addPay);
					RxbuffStr = RxbuffStr.substring(5);
					if (RxbuffStr.contains("08 5")) {
						index = RxbuffStr.indexOf("08 5");
						RxbuffStr = RxbuffStr.substring(index);
						if (!RxbuffStr.equals("") && RxbuffStr.length() >= 5) {
							billType = Integer.parseInt(RxbuffStr.substring(4,
									5));
							num = 0;
							if (RxbuffStr.length() >= 8) {
								num = Integer.parseInt(
										RxbuffStr.substring(6, 8), 16);
							}
							addPay = 0;
							switch (billType) {
							case 0:
								addPay = 50;
								if (RxbuffStr.length() >= 8) {
									mdbBean.setFiveJiaoInTubeNum(num);
								}
								// fiveJiaoInTubeNum++;
								break;
							case 1:
								addPay = 100;
								if (RxbuffStr.length() >= 8) {
									mdbBean.setOneYuanInTubeNum(num);
								}
								// oneYuanInTubeNum++;
								break;
							}
							mdbBean.setCurrentPay(mdbBean.getCurrentPay()
									+ addPay);
							RxbuffStr = RxbuffStr.substring(5);
							if (RxbuffStr.contains("08 5")) {
								index = RxbuffStr.indexOf("08 5");
								RxbuffStr = RxbuffStr.substring(index);
								if (!RxbuffStr.equals("")
										&& RxbuffStr.length() >= 5) {
									billType = Integer.parseInt(RxbuffStr
											.substring(4, 5));
									num = 0;
									if (RxbuffStr.length() >= 8) {
										num = Integer.parseInt(
												RxbuffStr.substring(6, 8), 16);
									}
									addPay = 0;
									switch (billType) {
									case 0:
										addPay = 50;
										if (RxbuffStr.length() >= 8) {
											mdbBean.setFiveJiaoInTubeNum(num);
										}
										// fiveJiaoInTubeNum++;
										break;
									case 1:
										addPay = 100;
										if (RxbuffStr.length() >= 8) {
											mdbBean.setOneYuanInTubeNum(num);
										}
										// oneYuanInTubeNum++;
										break;
									}
									mdbBean.setCurrentPay(mdbBean
											.getCurrentPay() + addPay);
								}
							}
						}
					}
				}
			}
			if (RxbuffStr.contains("08 4")) {
				// 投入硬币（tube满，进入后备箱）
				int index = RxbuffStr.indexOf("08 4");
				RxbuffStr = RxbuffStr.substring(index);
				if (!RxbuffStr.equals("") && RxbuffStr.length() >= 4) {
					int billType = Integer.parseInt(RxbuffStr.substring(4, 5));
					int num = 0;
					if (RxbuffStr.length() >= 8) {
						num = Integer.parseInt(RxbuffStr.substring(6, 8), 16);
					}
					int addPay = 0;
					switch (billType) {
					case 0:
						addPay = 50;
						if (RxbuffStr.length() >= 8) {
							mdbBean.setFiveJiaoInTubeNum(num);
						}
						// fiveJiaoInTubeNum++;
						break;
					case 1:
						addPay = 100;
						if (RxbuffStr.length() >= 8) {
							mdbBean.setOneYuanInTubeNum(num);
						}
						// oneYuanInTubeNum++;
						break;
					}
					mdbBean.setCurrentPay(mdbBean.getCurrentPay() + addPay);
					RxbuffStr = RxbuffStr.substring(5);
					if (RxbuffStr.contains("08 4")) {
						index = RxbuffStr.indexOf("08 4");
						RxbuffStr = RxbuffStr.substring(index);
						if (!RxbuffStr.equals("") && RxbuffStr.length() >= 5) {
							billType = Integer.parseInt(RxbuffStr.substring(4,
									5));
							num = 0;
							if (RxbuffStr.length() >= 8) {
								num = Integer.parseInt(
										RxbuffStr.substring(6, 8), 16);
							}
							addPay = 0;
							switch (billType) {
							case 0:
								addPay = 50;
								if (RxbuffStr.length() >= 8) {
									mdbBean.setFiveJiaoInTubeNum(num);
								}
								// fiveJiaoInTubeNum++;
								break;
							case 1:
								addPay = 100;
								if (RxbuffStr.length() >= 8) {
									mdbBean.setOneYuanInTubeNum(num);
								}
								// oneYuanInTubeNum++;
								break;
							}
							mdbBean.setCurrentPay(mdbBean.getCurrentPay()
									+ addPay);
							RxbuffStr = RxbuffStr.substring(5);
							if (RxbuffStr.contains("08 4")) {
								index = RxbuffStr.indexOf("08 4");
								RxbuffStr = RxbuffStr.substring(index);
								if (!RxbuffStr.equals("")
										&& RxbuffStr.length() >= 5) {
									billType = Integer.parseInt(RxbuffStr
											.substring(4, 5));
									num = 0;
									if (RxbuffStr.length() >= 8) {
										num = Integer.parseInt(
												RxbuffStr.substring(6, 8), 16);
									}
									addPay = 0;
									switch (billType) {
									case 0:
										addPay = 50;
										if (RxbuffStr.length() >= 8) {
											mdbBean.setFiveJiaoInTubeNum(num);
										}
										// fiveJiaoInTubeNum++;
										break;
									case 1:
										addPay = 100;
										if (RxbuffStr.length() >= 8) {
											mdbBean.setOneYuanInTubeNum(num);
										}
										// oneYuanInTubeNum++;
										break;
									}
									mdbBean.setCurrentPay(mdbBean
											.getCurrentPay() + addPay);
								}
							}
						}
					}
				}
			}
			int pay = mdbBean.getCurrentPay();
			if (transaction.getPayedAll() != pay) {
				// 投入金额有更新，更新到原交易
				transaction.setPayedAll(pay);
				Util.WriteFile("存储交易2：" + transaction.toString());//id号没更新，不过其他的都是最新交易的
				tDao.updateTransaction(transaction);

			}

			// 显示投入金额
			Intent intent = new Intent();
			intent.setAction("Serialport_Action_RECEIVE_MONEY");
			intent.putExtra("receive_total_money", pay);
			context.sendBroadcast(intent);
			Util.WriteFile("共投入现金：" + pay);
		}
		if (RxbuffStr.contains(" 00 00 00 00 00 00")) {
			if (RxbuffStr.startsWith(" ")
					&& RxbuffStr.substring(3, 4).equals(" ")) {
				// 查询tube中硬币数量反馈
				int fiveJ = Integer.parseInt(RxbuffStr.substring(7, 9).trim(),
						16);
				int onrY = Integer.parseInt(RxbuffStr.substring(10, 12).trim(),
						16);
				mdbBean.setFiveJiaoInTubeNum(fiveJ);
				mdbBean.setOneYuanInTubeNum(onrY);
				mdbBean.getFjInTube().setCount(fiveJ);
				mdbBean.getOyInTube().setCount(onrY);
				cDao.updateCash(mdbBean.getFjInTube());
				cDao.updateCash(mdbBean.getOyInTube());
				Util.WriteFile("tube   5角：" + mdbBean.getFiveJiaoInTubeNum()
						+ "  1元：" + mdbBean.getOneYuanInTubeNum());
				Util.WriteFile("更新硬币数量完毕");
			}
		}
	}

	private void updateCashInfo() {
		mdbBean.getFjInBox().setCount(mdbBean.getFiveJiaoBoxbeNum());
		mdbBean.getOyInBox().setCount(mdbBean.getOneYuanInBoxNum());
		mdbBean.getOyInBoxb().setCount(mdbBean.getOneYuanInBoxNumb());
		mdbBean.getFyInBox().setCount(mdbBean.getFiveYuanInBoxNum());
		mdbBean.getTyInBox().setCount(mdbBean.getTenYuanInBoxNum());
		mdbBean.getTwyInBox().setCount(mdbBean.getTwentyYuanInBoxNum());
		mdbBean.getFjInTube().setCount(mdbBean.getFiveJiaoInTubeNum());
		mdbBean.getOyInTube().setCount(mdbBean.getOneYuanInTubeNum());

		cDao.updateCash(mdbBean.getFjInBox());
		cDao.updateCash(mdbBean.getOyInBox());
		cDao.updateCash(mdbBean.getOyInBoxb());
		cDao.updateCash(mdbBean.getFyInBox());
		cDao.updateCash(mdbBean.getTyInBox());
		cDao.updateCash(mdbBean.getTwyInBox());
		cDao.updateCash(mdbBean.getFjInTube());
		cDao.updateCash(mdbBean.getOyInTube());
	}

	private void initMdbPara() {
		// 更新现金数量到数据库
		List<Cash> cashList = cDao.queryCash();
		mdbBean.setCashList(cashList);
		mdbBean.setFjInBox(cashList.get(0));
		mdbBean.setOyInBox(cashList.get(1));
		mdbBean.setOyInBoxb(cashList.get(2));
		mdbBean.setFyInBox(cashList.get(3));
		mdbBean.setTyInBox(cashList.get(4));
		mdbBean.setTwyInBox(cashList.get(5));
		mdbBean.setFjInTube(cashList.get(6));
		mdbBean.setOyInTube(cashList.get(7));

		mdbBean.setFiveJiaoBoxbeNum(mdbBean.getFjInBox().getCount());
		mdbBean.setOneYuanInBoxNum(mdbBean.getOyInBox().getCount());
		mdbBean.setOneYuanInBoxNumb(mdbBean.getOyInBoxb().getCount());
		mdbBean.setFiveYuanInBoxNum(mdbBean.getFyInBox().getCount());
		mdbBean.setTenYuanInBoxNum(mdbBean.getTyInBox().getCount());
		mdbBean.setTwentyYuanInBoxNum(mdbBean.getTwyInBox().getCount());
	}




}
