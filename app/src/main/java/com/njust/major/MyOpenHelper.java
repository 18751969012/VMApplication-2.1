package com.njust.major;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {
	private Context context;
	private int eyesType = 2;
	private int eyesNo = 24;
	private int RoadNo = 24;
	private int boxNo = 4;
	private int gateActionTime = 200;
	private int Raster = 0;
	private int LockBack = 0;
	private int temperCtl = 0;

	public MyOpenHelper(Context context) {
		super(context, "Major.db", null, 2);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String DrinkPosition_table = "create table DrinkPosition(_id integer primary key autoincrement,drinkID integer,positionID integer,price integer,stock tinyint,state tinyint,littleCount integer,totalCount integer,gateActionTime integer,counter integer)";
		db.execSQL(DrinkPosition_table);

		String CashboxInfo_table = "create table CashboxInfo(_id integer primary key autoincrement,type nvarchar(10),count smallint,upBound smallint,downBound smallint,state nvarchar(10))";
		db.execSQL(CashboxInfo_table);

		String LogInfo_table = "create table LogInfo(_id integer primary key autoincrement,time datetime,type nvarchar(10),detail nvarchar(2000))";
		db.execSQL(LogInfo_table);

		// String TransanctionInfo_table =
		// "create table TransanctionInfo(_id integer primary key autoincrement,complete integer default 0,type nvarchar(10) ,beginTime datetime default CURRENT_TIMESTAMP,endTime datetime default CURRENT_TIMESTAMP, drinkID nvarchar(10),price money,payedAll money default 0,charge money default 0)";
		String TransanctionInfo_table = "create table TransanctionInfo(_id integer primary key autoincrement,complete integer default 0,type integer default 0 ,beginTime datetime default CURRENT_TIMESTAMP,endTime datetime, drinkID integer,price integer,payedAll integer default 0,charge integer default 0,positionID integer,counter integer,successOrFailure integer default 1)";
		db.execSQL(TransanctionInfo_table);

		// String MachineInfo_table =
		// "create table MachineInfo(_id integer primary key autoincrement,machineID nvarchar(20),machineName nvarchar(50),machineVersion nvarchar(20),machineType nvarchar(50),latitude nvarchar(10),longitude nvarchar(10),ip nvarchar(20),port nvarchar(10),leftState nvarchar(10),leftSetTemp nvarchar(10),leftTemperature nvarchar(10),rightState nvarchar(10),rightSetTemp nvarchar(10),rightTemperature nvarchar(10),sendBySecond nvarchar(10),fanType integet,YJTemp nvarchar(10),YLTemp nvarchar(10),LFan nvarchar(10),RFan nvarchar(10),YJFan nvarchar(10),YLFan nvarchar(10))";
		String MachineInfo_table = "create table MachineInfo(_id integer primary key autoincrement,counter integer,machineID nvarchar(20),machineVersion nvarchar(20),vmState integer,billMachineState integer,coinMachineState integer,boxNo integer,doorState integer,lockRoad integer,eyesType integer,eyesNo integer,RoadNo integer,light integer,leftState integer,leftSetTemp integer,leftTemperature integer,rightState integer,rightSetTemp integer,rightTemperature integer,fanType integer,YJTemp integer,YLTemp integer,LFan integer,RFan integer,YJFan integer,YLFan integer,Raster integer,LockBack integer,temperCtl integer,initFinish integer,use integer,counterState integer)";

		db.execSQL(MachineInfo_table);

		initData(db);
	}
	private static final String CHANGE_OLD_TABLE_TRANSANCTIONINFO = "alter table TransanctionInfo rename to _TransanctionInfo";
	private static final String CREATE_NEW_TABLE = "create table TransanctionInfo(_id integer primary key autoincrement,complete integer default 0,type integer default 0 ,beginTime datetime default CURRENT_TIMESTAMP,endTime datetime, drinkID integer,price integer,payedAll integer default 0,charge integer default 0,positionID integer,counter integer,successOrFailure integer default 1)";
	private static final String INSERT_DATA = "insert into TransanctionInfo(_id,complete,type,beginTime,endTime,drinkID,price,payedAll,charge,positionID,counter) select _id,complete,type,beginTime,endTime,drinkID,price,payedAll,charge,positionID,counter from _TransanctionInfo";
	private static final String DROP_TABLE = "drop table _TransanctionInfo";

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch(newVersion){
			case 2:
				db.execSQL(CHANGE_OLD_TABLE_TRANSANCTIONINFO); //第一步将旧表改为临时表

				db.execSQL(CREATE_NEW_TABLE); //第二步创建新表(新添加的字段或去掉 的字段)

				db.execSQL(INSERT_DATA); //第三步将旧表中的原始数据保存到新表中以防遗失

				db.execSQL(DROP_TABLE); //第四步删除临时备份表
				break;
		}

	}

	public void initData(SQLiteDatabase db) {
		// loadConfig();
		// String MachineInfo =
		// "insert into MachineInfo(machineID,machineVersion,vmState,billMachineState,coinMachineState,eyesType,eyesNo,RoadNo,light,leftState,leftSetTemp,leftTemperature,rightState,rightSetTemp,rightTemperature,fanType,YJTemp,YLTemp,LFan,RFan,YJFan,YLFan,doorState,lockRoad) values('01090001','ADH816ASV3.0.0.07',1,1,1,"
		// + eyesType
		// + ","
		// + eyesNo
		// + ","
		// + RoadNo
		// + ",1,0,-4,-2,1,37,36,1,40,0,1,1,1,0,0,0)";
		String MachineInfo = "insert into MachineInfo("
				+ "counter,machineID,machineVersion,vmState,billMachineState,coinMachineState,boxNo,doorState,lockRoad,initFinish) values("
				+ "0,'00000000','2.3',1,1,1," + boxNo + ",1,0,0)";
		db.execSQL(MachineInfo);
		String MachineInfo1 = "insert into MachineInfo("
				+ "counter,eyesType,eyesNo,RoadNo,light,leftState,leftSetTemp,leftTemperature,rightState,rightSetTemp,rightTemperature,fanType,YJTemp,YLTemp,LFan,RFan,YJFan,YLFan,temperCtl,use,counterState) values("
				+ "1," + eyesType + "," + eyesNo + "," + RoadNo
				+ ",0,2,15,0,2,37,0,1,0,0,1,1,1,0,1,0,0)";
		db.execSQL(MachineInfo1);
		for (int i = 2; i < 6; i++) {
			// for (int i = 2; i < boxNo + 2; i++) {
			// String MachineInfo2 = "insert into MachineInfo("
			// +
			// "counter,light,leftState,leftSetTemp,leftTemperature,fanType,YJTemp,YLTemp,LFan,YJFan,YLFan,Raster,LockBack,temperCtl) values("
			// + "" + i + ",0,2,15,0,1,0,0,1,1,0," + Raster + ","
			// + LockBack + "," + temperCtl + ")";
			String MachineInfo2 = "insert into MachineInfo("
					+ "counter,light,leftState,leftSetTemp,leftTemperature,fanType,YJTemp,YLTemp,LFan,YJFan,YLFan,Raster,LockBack,temperCtl,use,counterState) values("
					+ "" + i + ",0,2,15,0,1,0,0,1,1,0,0,0,0,0,0)";
			db.execSQL(MachineInfo2);
		}
		// for (int i = 1; i <= eyesNo; i++) {
		for (int i = 1; i <= 42; i++) {
			String sql = "insert into DrinkPosition(drinkID,positionID,price,stock,state,littleCount,totalCount,gateActionTime,counter) values ("
					+ i + "," + i + ",3500,0,1,0,0," + gateActionTime + ",1)";
			db.execSQL(sql);
		}
		// for (int i = eyesNo + 1; i <= RoadNo; i++) {
		// String sql =
		// "insert into DrinkPosition(drinkID,positionID,price,stock,state,littleCount,totalCount,gateActionTime,counter) values (0,"
		// + i + ",350,0,1,0,0," + gateActionTime + ",1)";
		// db.execSQL(sql);
		// }

		// for (int i = 2; i < boxNo + 2; i++) {
		for (int i = 2; i < 6; i++) {
			for (int j = 1; j <= 100; j++) {
				String sql = "insert into DrinkPosition(drinkID,positionID,price,stock,state,littleCount,totalCount,gateActionTime,counter) values ("
						+ j
						+ ","
						+ j
						+ ",2500,0,1,0,0,"
						+ gateActionTime
						+ ","
						+ i + ")";
				db.execSQL(sql);
			}
		}

		String CashInit_5jiao = "insert into CashboxInfo(type,count,upBound,downBound,state) values('5角',0,200,0,'正常')";
		db.execSQL(CashInit_5jiao);
		String CashInit_1yuan = "insert into CashboxInfo(type,count,upBound,downBound,state) values('1元',0,200,0,'正常')";
		db.execSQL(CashInit_1yuan);
		String CashInit_1yuanZhipi = "insert into CashboxInfo(type,count,upBound,downBound,state) values('1元纸',0,200,0,'正常')";
		db.execSQL(CashInit_1yuanZhipi);
		String CashInit_5yuan = "insert into CashboxInfo(type,count,upBound,downBound,state) values('5元',0,100,0,'正常')";
		db.execSQL(CashInit_5yuan);
		String CashInit_10yuan = "insert into CashboxInfo(type,count,upBound,downBound,state) values('10元',0,100,0,'正常')";
		db.execSQL(CashInit_10yuan);
		String CashInit_20yuan = "insert into CashboxInfo(type,count,upBound,downBound,state) values('20元',0,100,0,'正常')";
		db.execSQL(CashInit_20yuan);
		String TubeCashInit_5jiao = "insert into CashboxInfo(type,count,upBound,downBound,state) values('5角管子',0,200,10,'缺')";
		db.execSQL(TubeCashInit_5jiao);
		String TubeCashInit_1yuan = "insert into CashboxInfo(type,count,upBound,downBound,state) values('1元管子',0,200,10,'缺')";
		db.execSQL(TubeCashInit_1yuan);
	}


}
