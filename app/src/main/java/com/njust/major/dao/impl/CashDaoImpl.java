package com.njust.major.dao.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.njust.major.bean.Cash;
import com.njust.major.dao.CashDao;

import java.util.ArrayList;
import java.util.List;

public class CashDaoImpl implements CashDao {
	private SQLiteDatabase db;
	private Context context;

	public CashDaoImpl(Context context) {
		this.context = context;
		db = SQLiteDatabase.openDatabase(
				"data/data/com.njust.major/databases/Major.db", null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	@Override
	public void updateCash(Cash cash) {
		String sql1 = "update CashboxInfo set count=?,upBound=?,downBound=?,state=? where type=?";

		String state = null;
		if (cash.getCount() < cash.getDownBound()) {
			state = "缺";
		} else if (cash.getCount() >= cash.getDownBound()
				&& cash.getCount() <= cash.getUpBound()) {
			state = "满";
		} else {
			state = "正常";
		}
		Object[] bindArgs1 = new Object[] { cash.getCount(), cash.getUpBound(),
				cash.getDownBound(), state, cash.getType() };
		db.execSQL(sql1, bindArgs1);
	}

	@Override
	public Cash selestCash(String Type) {
		Cash cash = null;
		String sql = "select *from CashboxInfo where type =?";
		String[] Args = new String[] { Type };
		Cursor cursor = db.rawQuery(sql, Args);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				int _id = cursor.getInt(0);
				String type = cursor.getString(1);
				int count = cursor.getInt(2);
				int upBound = cursor.getInt(3);
				int downBound = cursor.getInt(4);
				String state = cursor.getString(5);
				cash = new Cash(_id, type, count, upBound, downBound, state);
			}
		}
		return cash;
	}

	@Override
	public List<Cash> queryCash() {
		List<Cash> list = new ArrayList<Cash>();
		String sql = "select *from CashboxInfo";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				int _id = cursor.getInt(0);
				String type = cursor.getString(1);
				int count = cursor.getInt(2);
				int upBound = cursor.getInt(3);
				int downBound = cursor.getInt(4);
				String state = cursor.getString(5);
				Cash cash = new Cash(_id, type, count, upBound, downBound,
						state);
				list.add(cash);
			}
		}
		return list;
	}

}
