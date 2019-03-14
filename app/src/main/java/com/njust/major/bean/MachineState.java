package com.njust.major.bean;

public class MachineState {
	private int _id;
	private int counter;
	private String machineid;
	private String version;
	private int vmState;
	private int billMachineState;
	private int coinMachineState;
	private int boxNo;
	private int doorState;
	private int lockRoad;
	private int eyesType;
	private int eyesNo;
	private int RoadNo;
	private int light;
	private int leftState;
	private int leftSetTemp;
	private int leftTemperature;
	private int rightState;
	private int rightSetTemp;
	private int rightTemperature;
	private int fanType;
	private int YJTemp;
	private int YLTemp;
	private int LFan;
	private int RFan;
	private int YJFan;
	private int YLFan;
	private int Raster;
	private int LockBack;
	private int temperCtl;
	private int initFinish;
	private int use;
	private int counterState;

	public MachineState() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MachineState(int _id, int counter, String machineid, String version,
                        int vmState, int billMachineState, int coinMachineState, int boxNo,
                        int doorState, int lockRoad, int eyesType, int eyesNo, int roadNo,
                        int light, int leftState, int leftSetTemp, int leftTemperature,
                        int rightState, int rightSetTemp, int rightTemperature,
                        int fanType, int yJTemp, int yLTemp, int lFan, int rFan, int yJFan,
                        int yLFan, int raster, int lockBack, int temperCtl, int initFinish,
                        int use) {
		super();
		this._id = _id;
		this.counter = counter;
		this.machineid = machineid;
		this.version = version;
		this.vmState = vmState;
		this.billMachineState = billMachineState;
		this.coinMachineState = coinMachineState;
		this.boxNo = boxNo;
		this.doorState = doorState;
		this.lockRoad = lockRoad;
		this.eyesType = eyesType;
		this.eyesNo = eyesNo;
		RoadNo = roadNo;
		this.light = light;
		this.leftState = leftState;
		this.leftSetTemp = leftSetTemp;
		this.leftTemperature = leftTemperature;
		this.rightState = rightState;
		this.rightSetTemp = rightSetTemp;
		this.rightTemperature = rightTemperature;
		this.fanType = fanType;
		YJTemp = yJTemp;
		YLTemp = yLTemp;
		LFan = lFan;
		RFan = rFan;
		YJFan = yJFan;
		YLFan = yLFan;
		Raster = raster;
		LockBack = lockBack;
		this.temperCtl = temperCtl;
		this.initFinish = initFinish;
		this.use = use;
	}

	public int getUse() {
		return use;
	}

	public int getCounterState() {
		return counterState;
	}

	public void setCounterState(int counterState) {
		this.counterState = counterState;
	}

	public void setUse(int use) {
		this.use = use;
	}

	public MachineState(String machineid, String version, int vmState,
                        int billMachineState, int coinMachineState, int eyesType,
                        int eyesNo, int roadNo, int light, int leftState, int leftSetTemp,
                        int leftTemperature, int rightState, int rightSetTemp,
                        int rightTemperature, int fanType, int yJTemp, int yLTemp,
                        int lFan, int rFan, int yJFan, int yLFan) {
		super();
		this.machineid = machineid;
		this.version = version;
		this.vmState = vmState;
		this.billMachineState = billMachineState;
		this.coinMachineState = coinMachineState;
		this.eyesType = eyesType;
		this.eyesNo = eyesNo;
		this.RoadNo = roadNo;
		this.light = light;
		this.leftState = leftState;
		this.leftSetTemp = leftSetTemp;
		this.leftTemperature = leftTemperature;
		this.rightState = rightState;
		this.rightSetTemp = rightSetTemp;
		this.rightTemperature = rightTemperature;
		this.fanType = fanType;
		this.YJTemp = yJTemp;
		this.YLTemp = yLTemp;
		this.LFan = lFan;
		this.RFan = rFan;
		this.YJFan = yJFan;
		this.YLFan = yLFan;

	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(int boxNo) {
		this.boxNo = boxNo;
	}

	public int getDoorState() {
		return doorState;
	}

	public void setDoorState(int doorState) {
		this.doorState = doorState;
	}

	public int getRaster() {
		return Raster;
	}

	public void setRaster(int raster) {
		Raster = raster;
	}

	public int getLockBack() {
		return LockBack;
	}

	public void setLockBack(int lockBack) {
		LockBack = lockBack;
	}

	public int getTemperCtl() {
		return temperCtl;
	}

	public void setTemperCtl(int temperCtl) {
		this.temperCtl = temperCtl;
	}

	public int getLockRoad() {
		return lockRoad;
	}

	public void setLockRoad(int lockRoad) {
		this.lockRoad = lockRoad;
	}

	public String getMachineid() {
		return machineid;
	}

	public void setMachineid(String machineid) {
		this.machineid = machineid;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getVmState() {
		return vmState;
	}

	public void setVmState(int vmState) {
		this.vmState = vmState;
	}

	public int getBillMachineState() {
		return billMachineState;
	}

	public void setBillMachineState(int billMachineState) {
		this.billMachineState = billMachineState;
	}

	public int getCoinMachineState() {
		return coinMachineState;
	}

	public void setCoinMachineState(int coinMachineState) {
		this.coinMachineState = coinMachineState;
	}

	public int getEyesType() {
		return eyesType;
	}

	public void setEyesType(int eyesType) {
		this.eyesType = eyesType;
	}

	public int getEyesNo() {
		return eyesNo;
	}

	public void setEyesNo(int eyesNo) {
		this.eyesNo = eyesNo;
	}

	public int getRoadNo() {
		return RoadNo;
	}

	public void setRoadNo(int roadNo) {
		RoadNo = roadNo;
	}

	public int getLight() {
		return light;
	}

	public void setLight(int light) {
		this.light = light;
	}

	public int getLeftState() {
		return leftState;
	}

	public void setLeftState(int leftState) {
		this.leftState = leftState;
	}

	public int getLeftSetTemp() {
		return leftSetTemp;
	}

	public void setLeftSetTemp(int leftSetTemp) {
		this.leftSetTemp = leftSetTemp;
	}

	public int getLeftTemperature() {
		return leftTemperature;
	}

	public void setLeftTemperature(int leftTemperature) {
		this.leftTemperature = leftTemperature;
	}

	public int getRightState() {
		return rightState;
	}

	public void setRightState(int rightState) {
		this.rightState = rightState;
	}

	public int getRightSetTemp() {
		return rightSetTemp;
	}

	public void setRightSetTemp(int rightSetTemp) {
		this.rightSetTemp = rightSetTemp;
	}

	public int getRightTemperature() {
		return rightTemperature;
	}

	public void setRightTemperature(int rightTemperature) {
		this.rightTemperature = rightTemperature;
	}

	public int getFanType() {
		return fanType;
	}

	public void setFanType(int fanType) {
		this.fanType = fanType;
	}

	public int getYJTemp() {
		return YJTemp;
	}

	public void setYJTemp(int yJTemp) {
		YJTemp = yJTemp;
	}

	public int getYLTemp() {
		return YLTemp;
	}

	public void setYLTemp(int yLTemp) {
		YLTemp = yLTemp;
	}

	public int getLFan() {
		return LFan;
	}

	public void setLFan(int lFan) {
		LFan = lFan;
	}

	public int getRFan() {
		return RFan;
	}

	public void setRFan(int rFan) {
		RFan = rFan;
	}

	public int getYJFan() {
		return YJFan;
	}

	public void setYJFan(int yJFan) {
		YJFan = yJFan;
	}

	public int getYLFan() {
		return YLFan;
	}

	public void setYLFan(int yLFan) {
		YLFan = yLFan;
	}

	public int getInitFinish() {
		return initFinish;
	}

	public void setInitFinish(int initFinish) {
		this.initFinish = initFinish;
	}

	@Override
	public String toString() {
		return "MachineState [_id=" + _id + ", counter=" + counter + ", "
				+ (machineid != null ? "machineid=" + machineid + ", " : "")
				+ (version != null ? "version=" + version + ", " : "")
				+ "vmState=" + vmState + ", billMachineState="
				+ billMachineState + ", coinMachineState=" + coinMachineState
				+ ", boxNo=" + boxNo + ", doorState=" + doorState
				+ ", lockRoad=" + lockRoad + ", eyesType=" + eyesType
				+ ", eyesNo=" + eyesNo + ", RoadNo=" + RoadNo + ", light="
				+ light + ", leftState=" + leftState + ", leftSetTemp="
				+ leftSetTemp + ", leftTemperature=" + leftTemperature
				+ ", rightState=" + rightState + ", rightSetTemp="
				+ rightSetTemp + ", rightTemperature=" + rightTemperature
				+ ", fanType=" + fanType + ", YJTemp=" + YJTemp + ", YLTemp="
				+ YLTemp + ", LFan=" + LFan + ", RFan=" + RFan + ", YJFan="
				+ YJFan + ", YLFan=" + YLFan + ", Raster=" + Raster
				+ ", LockBack=" + LockBack + ", temperCtl=" + temperCtl
				+ ", initFinish=" + initFinish + ", use=" + use + "]";
	}

}
