package com.njust.major.dao;

import com.njust.major.bean.Transaction;

public interface TransactionDao {
	public void deleteTransaction();

	public void addTransaction(Transaction transaction);

	public void updateTransaction(Transaction transaction);

	public Transaction queryLastedTransaction();

}
