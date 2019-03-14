package com.njust.major.bean;

public class Cash {
	private int _id;
	private String type;
	private int count;
	private int upBound;
	private int downBound;
	private String state;

	public Cash() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Cash(int _id, String type, int count, int upBound, int downBound,
                String state) {
		super();
		this._id = _id;
		this.type = type;
		this.count = count;
		this.upBound = upBound;
		this.downBound = downBound;
		this.state = state;
	}

	public Cash(String type, int count, int upBound, int downBound, String state) {
		super();
		this.type = type;
		this.count = count;
		this.upBound = upBound;
		this.downBound = downBound;
		this.state = state;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getUpBound() {
		return upBound;
	}

	public void setUpBound(int upBound) {
		this.upBound = upBound;
	}

	public int getDownBound() {
		return downBound;
	}

	public void setDownBound(int downBound) {
		this.downBound = downBound;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Cash [_id=" + _id + ", "
				+ (type != null ? "type=" + type + ", " : "") + "count="
				+ count + ", upBound=" + upBound + ", downBound=" + downBound
				+ ", " + (state != null ? "state=" + state : "") + "]";
	}
}
