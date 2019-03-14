package com.njust.major.bean;

public class Transaction {
	private int _id;
	private int complete;
	private int type;
	private String beginTime;
	private String endTime;
	private int drinkID;
	private int price;
	private int payedAll;
	private int charge;
	private int positionID;
	private int counter;
	private int successOrFailure;//0=失败，1=成功

	public Transaction(int _id, int complete, int type, String beginTime,
                       String endTime, int drinkID, int price, int payedAll, int charge,
                       int positionID, int counter, int successOrFailure) {
		super();
		this._id = _id;
		this.complete = complete;
		this.type = type;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.drinkID = drinkID;
		this.price = price;
		this.payedAll = payedAll;
		this.charge = charge;
		this.positionID = positionID;
		this.counter = counter;
		this.successOrFailure = successOrFailure;
	}

	public Transaction(int complete, int type, String beginTime,
                       String endTime, int drinkID, int price, int payedAll, int charge,
                       int positionID, int counter, int successOrFailure) {
		super();
		this.complete = complete;
		this.type = type;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.drinkID = drinkID;
		this.price = price;
		this.payedAll = payedAll;
		this.charge = charge;
		this.positionID = positionID;
		this.counter = counter;
		this.successOrFailure = successOrFailure;
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

	public int getPositionID() {
		return positionID;
	}

	public void setPositionID(int positionID) {
		this.positionID = positionID;
	}

	public Transaction(int complete, int type, String beginTime,
                       String endTime, int drinkID, int price, int payedAll, int charge) {
		super();
		this.complete = complete;
		this.type = type;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.drinkID = drinkID;
		this.price = price;
		this.payedAll = payedAll;
		this.charge = charge;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getDrinkID() {
		return drinkID;
	}

	public void setDrinkID(int drinkID) {
		this.drinkID = drinkID;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getPayedAll() {
		return payedAll;
	}

	public void setPayedAll(int payedAll) {
		this.payedAll = payedAll;
	}

	public int getCharge() {
		return charge;
	}

	public void setCharge(int charge) {
		this.charge = charge;
	}

	public Transaction(int complete, int type, String beginTime,
                       String endTime, int payedAll, int charge) {
		super();
		this.complete = complete;
		this.type = type;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.payedAll = payedAll;
		this.charge = charge;
	}

	public int getComplete() {
		return complete;
	}

	public void setComplete(int complete) {
		this.complete = complete;
	}

	public int getSuccessOrFailure() {
		return successOrFailure;
	}

	public void setSuccessOrFailure(int successOrFailure) {
		this.successOrFailure = successOrFailure;
	}

	public Transaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Transaction [_id=" + _id +
				", complete=" + complete +
				", type=" + type +
				", " + (beginTime != null ? "beginTime=" + beginTime + ", " : "")
				+ (endTime != null ? "endTime=" + endTime + ", " : "") +
				"drinkID=" + drinkID +
				", price=" + price +
				", payedAll=" + payedAll +
				", charge=" + charge +
				", positionID=" + positionID +
				", counter=" + counter +
				", successOrFailure=" + successOrFailure + "]";
	}
}
