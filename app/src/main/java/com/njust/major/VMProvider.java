package com.njust.major;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.njust.major.bean.InfoBean;

import java.util.List;

public class VMProvider extends ContentProvider {

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	private static final int TINSERTSUCESS = 0;
	private static final int TDELETESUCESS = 1;
	private static final int TUPDATESUCESS = 2;
	private static final int TQUERYSUCESS = 3;
	private static final int IINSERTSUCESS = 4;
	private static final int IDELETESUCESS = 5;
	private static final int IUPDATESUCESS = 6;
	private static final int IQUERYSUCESS = 7;
	private static final int MUPDATESUCESS = 8;
	private static final int MQUERYSUCESS = 9;
	private static final int CUPDATESUCESS = 10;
	private static final int CQUERYSUCESS = 11;

	private MyOpenHelper myOpenHelper;
	private SQLiteDatabase aqldb;
	static {
		sURIMatcher.addURI("vm", "t/Tinsert", TINSERTSUCESS);
		sURIMatcher.addURI("vm", "t/Tdelete", TDELETESUCESS);
		sURIMatcher.addURI("vm", "t/Tupdate", TUPDATESUCESS);
		sURIMatcher.addURI("vm", "t/Tquery", TQUERYSUCESS);
		sURIMatcher.addURI("vm", "Iinsert", IINSERTSUCESS);
		sURIMatcher.addURI("vm", "Idelete", IDELETESUCESS);
		sURIMatcher.addURI("vm", "Iupdate", IUPDATESUCESS);
		sURIMatcher.addURI("vm", "Iquery", IQUERYSUCESS);
		sURIMatcher.addURI("vm", "Mupdate", MUPDATESUCESS);
		sURIMatcher.addURI("vm", "Mquery", MQUERYSUCESS);
		sURIMatcher.addURI("vm", "Cupdate", CUPDATESUCESS);
		sURIMatcher.addURI("vm", "Cquery", CQUERYSUCESS);
	}

	@Override
	public boolean onCreate() {
		myOpenHelper = new MyOpenHelper(getContext());
		aqldb = myOpenHelper.getWritableDatabase();
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
		int code = sURIMatcher.match(uri);
		if (code == TQUERYSUCESS) {
			SQLiteDatabase db = myOpenHelper.getReadableDatabase();
			Cursor cursor = db.query("TransanctionInfo", projection, selection,
					selectionArgs, null, null, sortOrder);
			return cursor;
		} else if (code == IQUERYSUCESS) {
			SQLiteDatabase db = myOpenHelper.getReadableDatabase();
			Cursor cursor = db.query("DrinkPosition", projection, selection,
					selectionArgs, null, null, sortOrder);
			return cursor;
		} else if (code == MQUERYSUCESS) {
			SQLiteDatabase db = myOpenHelper.getReadableDatabase();
			Cursor cursor = db.query("MachineInfo", projection, selection,
					selectionArgs, null, null, sortOrder);
			return cursor;
		} else if (code == CQUERYSUCESS) {
			SQLiteDatabase db = myOpenHelper.getReadableDatabase();
			Cursor cursor = db.query("CashboxInfo", projection, selection,
					selectionArgs, null, null, sortOrder);
			return cursor;
		} else {
			throw new IllegalArgumentException("路径不匹配");
		}
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int code = sURIMatcher.match(uri);
		if (code == TINSERTSUCESS) {
			long insert = aqldb.insert("TransanctionInfo", null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			Uri uri2 = Uri.parse("insert/" + insert);
			return uri2;
		} else if (code == IINSERTSUCESS) {
			long insert = aqldb.insert("DrinkPosition", null, values);
			Uri uri2 = Uri.parse("insert/" + insert);
			return uri2;
		} else {
			throw new IllegalArgumentException("·路径不匹配");
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int code = sURIMatcher.match(uri);
		if (code == TDELETESUCESS) {
			int delete = aqldb.delete("TransanctionInfo", selection,
					selectionArgs);
			return delete;
		} else if (code == IDELETESUCESS) {
			int delete = aqldb
					.delete("DrinkPosition", selection, selectionArgs);
			return delete;
		} else {
			throw new IllegalArgumentException("·路径不匹配");
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
		int code = sURIMatcher.match(uri);
		if (code == TUPDATESUCESS) {
			int update = aqldb.update("TransanctionInfo", values, selection,
					selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return update;
		} else if (code == IUPDATESUCESS) {
			int update = aqldb.update("DrinkPosition", values, selection,
					selectionArgs);
			return update;
		} else if (code == MUPDATESUCESS) {
			int update = aqldb.update("MachineInfo", values, selection,
					selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return update;
		} else if (code == CUPDATESUCESS) {
			int update = aqldb.update("CashboxInfo", values, selection,
					selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return update;
		} else {
			throw new IllegalArgumentException("路径不匹配");
		}
	}

}
