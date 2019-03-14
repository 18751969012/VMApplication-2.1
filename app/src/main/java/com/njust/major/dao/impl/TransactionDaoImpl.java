package com.njust.major.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.njust.major.bean.Transaction;
import com.njust.major.dao.TransactionDao;
import com.njust.major.util.Util;

public class TransactionDaoImpl implements TransactionDao {
	private Context context;

	public TransactionDaoImpl(Context context) {
		super();
		this.context = context;
	}

	public void deleteTransaction() {
		Uri uri = Uri.parse("content://vm/t/Tdelete");
		int delete = context.getContentResolver().delete(uri, null, null);
		Util.WriteFile("清空了 " + delete + " 交易记录");
	}

	@Override
	public void addTransaction(Transaction transaction) {
		Uri uri = Uri.parse("content://vm/t/Tinsert");
		ContentValues values = new ContentValues();
		values.put("complete", transaction.getComplete());
		values.put("type", transaction.getType());
		values.put("beginTime", transaction.getBeginTime());
		values.put("endTime", transaction.getEndTime());
		values.put("drinkID", transaction.getDrinkID());
		values.put("price", transaction.getPrice());
		values.put("payedAll", transaction.getPayedAll());
		values.put("charge", transaction.getCharge());
		values.put("positionID", transaction.getPositionID());
		values.put("counter", transaction.getCounter());
		values.put("successOrFailure", transaction.getSuccessOrFailure());
		Uri insert = context.getContentResolver().insert(uri, values);

		// SQLiteDatabase database = myOpenHelper.getWritableDatabase();
		// String sql =
		// "insert into TransanctionInfo(complete,type,beginTime,endTime,drinkID,price,payedAll,charge) values(?,?,?,?,?,?,?,?)";
		// int complete = 0;
		// if (transaction.isComplete()) {
		// complete = 1;
		// }
		//
		// Object[] bindArgs = new Object[] { complete, transaction.getType(),
		// transaction.getBeginTime(), transaction.getEndTime(),
		// transaction.getDrinkID(), transaction.getPrice(),
		// transaction.getPayedAll(), transaction.getCharge() };
		// database.execSQL(sql, bindArgs);
		// database.close();
	}

	@Override
	public void updateTransaction(Transaction transaction) {
		Uri uri = Uri.parse("content://vm/t/Tupdate");
		ContentValues values = new ContentValues();
		values.put("complete", transaction.getComplete());
		values.put("type", transaction.getType());
		values.put("endTime", transaction.getEndTime());
		values.put("drinkID", transaction.getDrinkID());
		values.put("price", transaction.getPrice());
		values.put("payedAll", transaction.getPayedAll());
		values.put("charge", transaction.getCharge());
		values.put("positionID", transaction.getPositionID());
		values.put("counter", transaction.getCounter());
		values.put("successOrFailure", transaction.getSuccessOrFailure());
		int update = context.getContentResolver().update(uri, values,
				"beginTime=?", new String[] { transaction.getBeginTime() });

		// SQLiteDatabase database = myOpenHelper.getWritableDatabase();
		// String sql =
		// "update TransanctionInfo set complete=?,type=?,endTime=?,drinkID=?,price=?,payedAll=?,charge=? where beginTime=?";
		// int complete = 0;
		// if (transaction.isComplete()) {
		// complete = 1;
		// }
		//
		// Object[] bindArgs = new Object[] { complete, transaction.getType(),
		// transaction.getEndTime(), transaction.getDrinkID(),
		// transaction.getPrice(), transaction.getPayedAll(),
		// transaction.getCharge(), transaction.getBeginTime() };
		// database.execSQL(sql, bindArgs);
		// database.close();
	}

	@Override
	public Transaction queryLastedTransaction() {
		Uri uri = Uri.parse("content://vm/t/Tquery");
		Cursor cursor = context.getContentResolver().query(uri, null, null,
				null, "_id desc");
		Transaction transaction = null;
		// String sql
		// ="select *from TransanctionInfo order by _id desc limit 0,1";
		// Cursor cursor = db.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToNext();
			// int _id = cursor.getInt(0);
			// int complete=cursor.getInt(1);
			int _id = cursor.getInt(0);
			int complete = cursor.getInt(1);
			int type = cursor.getInt(2);
			String beginTime = cursor.getString(3);
			String endTime = cursor.getString(4);
			int drinkID = cursor.getInt(5);
			int price = cursor.getInt(6);
			int payedAll = cursor.getInt(7);
			int charge = cursor.getInt(8);
			int positionID = cursor.getInt(9);
			int counter = cursor.getInt(10);
			int successOrFailure = cursor.getInt(11);
			transaction = new Transaction(_id, complete, type, beginTime,
					endTime, drinkID, price, payedAll, charge, positionID,
					counter, successOrFailure);
			cursor.close();
		}
		return transaction;
	}
}
