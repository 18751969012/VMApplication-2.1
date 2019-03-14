package com.njust.major.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.njust.major.bean.MachineState;
import com.njust.major.dao.MachineStateDao;

public class MachineStateDaoImpl implements MachineStateDao {
	private SQLiteDatabase db;
	private Context context;

	public MachineStateDaoImpl(Context context) {
		super();
		// TODO Auto-generated constructor stub
		this.context = context;
		db = SQLiteDatabase.openDatabase(
				"data/data/com.njust.major/databases/Major.db", null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	@Override
	public void updateState(int type, int state) {
		String sql1 = "";
		if (type == 1) {
			sql1 = "update MachineInfo set vmState=? where counter =0";
		} else if (type == 2) {
			sql1 = "update MachineInfo set billMachineState=? where counter =0";
		} else if (type == 3) {
			sql1 = "update MachineInfo set billMachineState=? where counter =0";
		}
		if (!sql1.equals("")) {
			Object[] bindArgs1 = new Object[] { "" + state };
			db.execSQL(sql1, bindArgs1);
		}

		// Uri uri = Uri.parse("content://vm/Mupdate");
		// ContentValues values = new ContentValues();
		// if(type==1){
		// values.put("vmState", state);
		// }else if(type==2){
		// values.put("billMachineState", state);
		// }else if(type==3){
		// values.put("coinMachineState", state);
		// }
		// int update = context.getContentResolver().update(uri, values,
		// "counter=?", new String[] { "" + 0 });
	}

	@Override
	public void updateCounterState(int counter, int state) {
		String sql1 = "update MachineInfo set counterState=? where counter =?";
		Object[] bindArgs1 = new Object[] { "" + state, "" + counter };
		db.execSQL(sql1, bindArgs1);

		// Uri uri = Uri.parse("content://vm/Mupdate");
		// ContentValues values = new ContentValues();
		// values.put("counterState", state);
		// int update = context.getContentResolver().update(uri, values,
		// "counter=?", new String[] { "" + counter });
	}

	public void updateLight(int type, int counter) {
		Uri uri = Uri.parse("content://vm/Mupdate");
		ContentValues values = new ContentValues();
		values.put("light", type);
		int update = context.getContentResolver().update(uri, values,
				"counter=?", new String[] { "" + counter });
	}

	@Override
	public void update(MachineState machineState) {
		Uri uri = Uri.parse("content://vm/Mupdate");
		ContentValues values = new ContentValues();
		values.put("leftState", machineState.getLeftState());
		values.put("leftSetTemp", machineState.getLeftSetTemp());
		values.put("rightState", machineState.getRightState());
		values.put("rightSetTemp", machineState.getRightSetTemp());
		values.put("fanType", machineState.getFanType());
		int update = context.getContentResolver().update(uri, values, "_id=?",
				new String[] { "1" });
	}

	@Override
	public MachineState queryMachineState(int counter) {
		MachineState machineState = new MachineState();
		Uri uri = Uri.parse("content://vm/Mquery");
		Cursor cursor = context.getContentResolver().query(uri, null,
				"counter=?", new String[] { "" + counter }, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToNext();
			if (counter == 0) {
				machineState.set_id(cursor.getInt(0));
				machineState.setCounter(cursor.getInt(1));
				machineState.setMachineid(cursor.getString(2));
				machineState.setVersion(cursor.getString(3));
				machineState.setVmState(cursor.getInt(4));
				machineState.setBillMachineState(cursor.getInt(5));
				machineState.setCoinMachineState(cursor.getInt(6));
				machineState.setBoxNo(cursor.getInt(7));
				machineState.setDoorState(cursor.getInt(8));
				machineState.setLockRoad(cursor.getInt(9));
				machineState.setInitFinish(cursor.getInt(30));
			} else if (counter == 1) {
				machineState.set_id(cursor.getInt(0));
				machineState.setCounter(cursor.getInt(1));
				machineState.setEyesType(cursor.getInt(10));
				machineState.setEyesNo(cursor.getInt(11));
				machineState.setRoadNo(cursor.getInt(12));
				machineState.setLight(cursor.getInt(13));
				machineState.setLeftState(cursor.getInt(14));
				machineState.setLeftSetTemp(cursor.getInt(15));
				machineState.setLeftTemperature(cursor.getInt(16));
				machineState.setRightState(cursor.getInt(17));
				machineState.setRightSetTemp(cursor.getInt(18));
				machineState.setRightTemperature(cursor.getInt(19));
				machineState.setFanType(cursor.getInt(20));
				machineState.setYJTemp(cursor.getInt(21));
				machineState.setYLTemp(cursor.getInt(22));
				machineState.setLFan(cursor.getInt(23));
				machineState.setRFan(cursor.getInt(24));
				machineState.setYJFan(cursor.getInt(25));
				machineState.setYLFan(cursor.getInt(26));
				machineState.setRaster(cursor.getInt(27));
				machineState.setLockBack(cursor.getInt(28));
				machineState.setTemperCtl(cursor.getInt(29));
				machineState.setUse(cursor.getInt(31));
				machineState.setCounterState(cursor.getInt(32));
			} else {
				machineState.set_id(cursor.getInt(0));
				machineState.setCounter(cursor.getInt(1));
				machineState.setLight(cursor.getInt(13));
				machineState.setLeftState(cursor.getInt(14));
				machineState.setLeftSetTemp(cursor.getInt(15));
				machineState.setLeftTemperature(cursor.getInt(16));
				machineState.setFanType(cursor.getInt(20));
				machineState.setYJTemp(cursor.getInt(21));
				machineState.setYLTemp(cursor.getInt(22));
				machineState.setLFan(cursor.getInt(23));
				machineState.setYJFan(cursor.getInt(25));
				machineState.setYLFan(cursor.getInt(26));
				machineState.setRaster(cursor.getInt(27));
				machineState.setLockBack(cursor.getInt(28));
				machineState.setTemperCtl(cursor.getInt(29));
				machineState.setUse(cursor.getInt(31));
				machineState.setCounterState(cursor.getInt(32));
			}
			cursor.close();
			// cursor.moveToNext();
			// machineState.setMachineid(cursor.getString(1));
			// machineState.setVersion(cursor.getString(2));
			// machineState.setVmState(cursor.getInt(3));
			// machineState.setBillMachineState(cursor.getInt(4));
			// machineState.setCoinMachineState(cursor.getInt(5));
			// machineState.setEyesType(cursor.getInt(6));
			// machineState.setEyesNo(cursor.getInt(7));
			// machineState.setRoadNo(cursor.getInt(8));
			// machineState.setLight(cursor.getInt(9));
			// machineState.setLeftState(cursor.getInt(10));
			// machineState.setLeftSetTemp(cursor.getInt(11));
			// machineState.setLeftTemperature(cursor.getInt(12));
			// machineState.setRightState(cursor.getInt(13));
			// machineState.setRightSetTemp(cursor.getInt(14));
			// machineState.setRightTemperature(cursor.getInt(15));
			// machineState.setFanType(cursor.getInt(16));
			// machineState.setYJTemp(cursor.getInt(17));
			// machineState.setYLTemp(cursor.getInt(18));
			// machineState.setLFan(cursor.getInt(19));
			// machineState.setRFan(cursor.getInt(20));
			// machineState.setYJFan(cursor.getInt(21));
			// machineState.setYLFan(cursor.getInt(22));
			// machineState.setLockRoad(cursor.getInt(24));
			// cursor.close();
		}
		return machineState;
	}

	@Override
	public void updateTemperature(MachineState machineState, int counter) {
		String sql1 = "update MachineInfo set leftState=?,leftTemperature=?,rightState=?,rightTemperature=? where counter ="
				+ counter + "";
		Object[] bindArgs1 = new Object[] { machineState.getLeftState(),
				machineState.getLeftTemperature(),
				machineState.getRightState(),
				machineState.getRightTemperature() };
		db.execSQL(sql1, bindArgs1);
	}

	@Override
	public void updateTemperControlState(MachineState machineState, int counter) {
		String sql1 = "update MachineInfo set YJTemp=?,YLTemp=?,LFan=?,RFan=?,YJFan=?,YLFan=? where counter ="
				+ counter + "";
		Object[] bindArgs1 = new Object[] { machineState.getYJTemp(),
				machineState.getYLTemp(), machineState.getLFan(),
				machineState.getRFan(), machineState.getYJFan(),
				machineState.getYLFan() };
		db.execSQL(sql1, bindArgs1);
	}

	@Override
	public void updateMachineID(String machineID) {
		String sql1 = "update MachineInfo set machineID=? where _id =1";
		Object[] bindArgs1 = new Object[] { "" + machineID };
		db.execSQL(sql1, bindArgs1);
	}

	@Override
	public void updateDoorState(int open) {
		String sql1 = "update MachineInfo set doorState=? where _id =1";
		Object[] bindArgs1 = new Object[] { "" + open };
		db.execSQL(sql1, bindArgs1);
	}

	@Override
	public void updateVersion(String version) {
		String sql1 = "update MachineInfo set machineVersion=? where _id =1";
		Object[] bindArgs1 = new Object[] { "" + version };
		db.execSQL(sql1, bindArgs1);
	}
}
