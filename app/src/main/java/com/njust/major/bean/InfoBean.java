package com.njust.major.bean;

public class InfoBean {
	private int _id;
	private int positionID;
	private int drinkID;
	private int price;
	private int stock;
	private int state;
	private int littleCount;
	private int totalCount;
	private int gateActionTime;
	private int counter;

	public InfoBean(int _id, int positionID, int drinkID, int price, int stock,
			int state, int littleCount, int totalCount, int gateActionTime,
			int counter) {
		super();
		this._id = _id;
		this.positionID = positionID;
		this.drinkID = drinkID;
		this.price = price;
		this.stock = stock;
		this.state = state;
		this.littleCount = littleCount;
		this.totalCount = totalCount;
		this.gateActionTime = gateActionTime;
		this.counter = counter;
	}

	public InfoBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getLittleCount() {
		return littleCount;
	}

	public void setLittleCount(int littleCount) {
		this.littleCount = littleCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getGateActionTime() {
		return gateActionTime;
	}

	public void setGateActionTime(int gateActionTime) {
		this.gateActionTime = gateActionTime;
	}

	public InfoBean(int _id, int positionID, int drinkID, int price, int stock,
			int state) {
		super();
		this._id = _id;
		this.positionID = positionID;
		this.drinkID = drinkID;
		this.price = price;
		this.stock = stock;
		this.state = state;
	}

	public InfoBean(int positionID, int drinkID, int price, int stock, int state) {
		super();
		this.positionID = positionID;
		this.drinkID = drinkID;
		this.price = price;
		this.stock = stock;
		this.state = state;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getPositionID() {
		return positionID;
	}

	public void setPositionID(int positionID) {
		this.positionID = positionID;
	}

	public int getDrinkID() {
		return drinkID;
	}

	public void setDrinkID(int drinkID) {
		this.drinkID = drinkID;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	@Override
	public String toString() {
		return "InfoBean [_id=" + _id + ", positionID=" + positionID
				+ ", drinkID=" + drinkID + ", price=" + price + ", stock="
				+ stock + ", state=" + state + ", littleCount=" + littleCount
				+ ", totalCount=" + totalCount + ", gateActionTime="
				+ gateActionTime + ", counter=" + counter + "]";
	}

}
