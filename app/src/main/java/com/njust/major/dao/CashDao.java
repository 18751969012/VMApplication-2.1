package com.njust.major.dao;

import com.njust.major.bean.Cash;

import java.util.List;

public interface CashDao {
	public void updateCash(Cash cash);

	public Cash selestCash(String type);

	public List<Cash> queryCash();
}
