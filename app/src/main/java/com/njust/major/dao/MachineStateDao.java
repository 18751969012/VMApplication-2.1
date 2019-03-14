package com.njust.major.dao;

import com.njust.major.bean.MachineState;

public interface MachineStateDao {
	public void updateLight(int type, int counter);

	public void update(MachineState machineState);

	public MachineState queryMachineState(int counter);

	public void updateMachineID(String machineID);
	
	public void updateVersion(String version);

	public void updateState(int type, int state);

	public void updateCounterState(int counter, int state);

	public void updateTemperature(MachineState machineState, int counter);

	public void updateTemperControlState(MachineState machineState, int counter);

	public void updateDoorState(int open);
}
