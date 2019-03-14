package com.njust.major.service;

import com.njust.major.SCM.BigEyes;
import com.njust.major.SCM.DrinkGate;
import com.njust.major.SCM.TemperControl;
import com.njust.major.bean.MdbBean;

public interface VMIService {
	public MdbBean getMdbBean();

	public BigEyes getBigEyes();

	public DrinkGate getDrinkGate();

	public TemperControl getTemperControl();

	public void setStop(boolean b);

	public int[] getBoxUse();

	void outGoods();
}
