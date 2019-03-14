package com.njust.major.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.njust.major.bean.InfoBean;
import com.njust.major.dao.InfoDao;

import java.util.ArrayList;
import java.util.List;

public class InfoDaoImpl implements InfoDao {
	private Context context;

	public InfoDaoImpl(Context context) {
		super();
		this.context = context;
	}

	@Override
	public List<InfoBean> query(int counter, int positionID) {
		List<InfoBean> list = new ArrayList<InfoBean>();
		InfoBean bean = null;
		Uri uri = Uri.parse("content://vm/Iquery");
		String[] Args = new String[] { "" + positionID, "" + counter };
		Cursor cursor = context.getContentResolver().query(uri, null,
				"positionID=? and counter=?", Args, null);

		// String sql = "select *from DrinkPosition where positionID =?";
		// String[] Args = new String[] { positionID };
		// Cursor cursor = db.rawQuery(sql, Args);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				int _id = cursor.getInt(0);
				int drinkID = cursor.getInt(1);
				int price = cursor.getInt(3);
				int stock = cursor.getInt(4);
				int state = cursor.getInt(5);
				int littleCount = cursor.getInt(6);
				int totalCount = cursor.getInt(7);
				int gateActionTime = cursor.getInt(8);
				bean = new InfoBean(_id, positionID, drinkID, price, stock,
						state, littleCount, totalCount, gateActionTime, counter);
				list.add(bean);
			}
			cursor.close();
		}
		return list;
	}

	@Override
	public void update(InfoBean bean) {
		Uri uri = Uri.parse("content://vm/Iupdate");
		ContentValues values = new ContentValues();
		values.put("drinkID", bean.getDrinkID());
		values.put("price", bean.getPrice());
		values.put("stock", bean.getStock());
		values.put("state", bean.getState());
		values.put("littleCount", bean.getLittleCount());
		values.put("totalCount", bean.getTotalCount());
		values.put("gateActionTime", bean.getGateActionTime());
		int update = context.getContentResolver().update(
				uri,
				values,
				"positionID=? and counter=?",
				new String[] { "" + bean.getPositionID(),
						"" + bean.getCounter() });

		// String sql1 =
		// "update DrinkPosition set drinkID=?,price=?,stock=?,state=? where positionID=?";
		// Object[] bindArgs1 = new Object[] { bean.getDrinkID(),
		// bean.getPrice(),
		// bean.getStock(), bean.getState(), bean.getPositionID() };
		// db.execSQL(sql1, bindArgs1);
	}

	@Override
	public void add(InfoBean bean) {
		Uri uri = Uri.parse("content://vm/Iinsert");
		ContentValues values = new ContentValues();
		values.put("drinkID", bean.getDrinkID());
		values.put("positionID", bean.getPositionID());
		values.put("price", bean.getPrice());
		values.put("stock", bean.getStock());
		values.put("state", bean.getState());
		values.put("littleCount", bean.getLittleCount());
		values.put("totalCount", bean.getTotalCount());
		values.put("gateActionTime", bean.getGateActionTime());
		values.put("counter", bean.getCounter());
		Uri insert = context.getContentResolver().insert(uri, values);

		// String sql =
		// "insert into DrinkPosition(drinkID,positionID,price,stock,state) values(?,?,?,?,?)";
		// Object[] bindArgs = new Object[] { bean.getDrinkID(),
		// bean.getPositionID(), bean.getPrice(), bean.getStock(),
		// bean.getState() };
		// db.execSQL(sql, bindArgs);
	}

	@Override
	public List<InfoBean> queryAll() {
		List<InfoBean> list = new ArrayList<InfoBean>();
		Uri uri = Uri.parse("content://vm/Iquery");
		Cursor cursor = context.getContentResolver().query(uri, null,
				"counter=?", new String[] { "" + 1 }, null);

		// String sql = "select *from DrinkPosition";
		// Cursor cursor = db.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				int _id = cursor.getInt(0);
				int drinkID = cursor.getInt(1);
				int positionID = cursor.getInt(2);
				int price = cursor.getInt(3);
				int stock = cursor.getInt(4);
				int state = cursor.getInt(5);
				int littleCount = cursor.getInt(6);
				int totalCount = cursor.getInt(7);
				int gateActionTime = cursor.getInt(8);
				int counter = cursor.getInt(9);
				InfoBean bean = new InfoBean(_id, positionID, drinkID, price,
						stock, state, littleCount, totalCount, gateActionTime,
						counter);
				list.add(bean);
			}
			cursor.close();
		}
		return list;
	}

	// @Override
	// public InfoBean queryByDrinkID(String drinkID) {
	// InfoBean bean = null;
	// if (drinkID != null) {
	// Uri uri = Uri.parse("content://vm/Iquery");
	// String[] Args = new String[] { drinkID };
	// Cursor cursor = context.getContentResolver().query(uri, null,
	// "drinkID=?", Args, null);
	// if (cursor != null && cursor.getCount() > 0) {
	// cursor.moveToNext();
	// int _id = cursor.getInt(0);
	// String positionID = cursor.getString(2);
	// String price = cursor.getString(3);
	// int stock = cursor.getInt(4);
	// int state = cursor.getInt(5);
	// int littleCount = cursor.getInt(6);
	// int totalCount = cursor.getInt(7);
	// int gateActionTime = cursor.getInt(8);
	// bean = new InfoBean(_id, positionID, drinkID, price, stock,
	// state, littleCount, totalCount, gateActionTime);
	// }
	// }
	// return bean;
	// }
	@Override
	public List<InfoBean> queryByDrinkID(int drinkID) {
		List<InfoBean> list = new ArrayList<InfoBean>();
		InfoBean bean = null;
		Uri uri = Uri.parse("content://vm/Iquery");
		String[] Args = new String[] { "" + drinkID };
		Cursor cursor = context.getContentResolver().query(uri, null,
				"drinkID=? and counter=1", Args, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				int _id = cursor.getInt(0);
				int positionID = cursor.getInt(2);
				int price = cursor.getInt(3);
				int stock = cursor.getInt(4);
				int state = cursor.getInt(5);
				int littleCount = cursor.getInt(6);
				int totalCount = cursor.getInt(7);
				int gateActionTime = cursor.getInt(8);
				int counter = cursor.getInt(9);
				bean = new InfoBean(_id, positionID, drinkID, price, stock,
						state, littleCount, totalCount, gateActionTime, counter);
				list.add(bean);
			}
			cursor.close();
		}
		return list;
	}

	@Override
	public void updateInfoState(int counter, int positionID, int state) {
		Uri uri = Uri.parse("content://vm/Iupdate");
		ContentValues values = new ContentValues();
		// values.put("stock", 0);
		values.put("state", state);
		int update = context.getContentResolver().update(uri, values,
				"positionID=? and counter=?",
				new String[] { "" + positionID, "" + counter });
	}

	public void updateInfoState(int counter, int state) {
		Uri uri = Uri.parse("content://vm/Iupdate");
		ContentValues values = new ContentValues();
		values.put("state", state);
		int update = context.getContentResolver().update(uri, values,
				"state!=1 and state!=2 and counter=?", new String[] { "" + counter });
	}

	@Override
	public void updateInfoStock(int counter, int positionID) {
		InfoBean bean = query(counter, positionID).get(0);
		int stock = bean.getStock();
		int littleCount = bean.getLittleCount();
		int totalCount = bean.getTotalCount();
		int state = 0;
		if (stock <= 1) {
			state = 1;
		}
		Uri uri = Uri.parse("content://vm/Iupdate");
		ContentValues values = new ContentValues();
		values.put("stock", stock - 1);
		values.put("littleCount", littleCount + 1);
		values.put("totalCount", totalCount + 1);
		values.put("state", state);
		int update = context.getContentResolver().update(uri, values,
				"positionID=? and counter=?",
				new String[] { "" + positionID, "" + counter });
	}

}
