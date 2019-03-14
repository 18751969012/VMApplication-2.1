package com.njust.major.dao;

import com.njust.major.bean.InfoBean;

import java.util.List;

public interface InfoDao {
	public List<InfoBean> query(int counter, int positionID);

	public void update(InfoBean bean);

	public void add(InfoBean bean);

	public List<InfoBean> queryAll();

	public List<InfoBean> queryByDrinkID(int drinkID);

	public void updateInfoState(int counter, int positionID, int state);

	public void updateInfoState(int counter, int state);

	public void updateInfoStock(int counter, int positionID);
}
