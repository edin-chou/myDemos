package com.edu.gdufs.edin.demo.model;

import java.util.Date;

public class Pword {
	
	private Integer _id;
	private String _word;
	private int _count;
	private Date _fromDate;
	private Date _toDate;
	
	public Integer getId() {
		return _id;
	}
	public void setId(Integer _id) {
		this._id = _id;
	}
	public String getWord() {
		return _word;
	}
	public void setWord(String _word) {
		this._word = _word;
	}
	public int getCount() {
		return _count;
	}
	public void setCount(int _count) {
		this._count = _count;
	}
	public Date getFromDate() {
		return _fromDate;
	}
	public void setFromDate(Date _fromDate) {
		this._fromDate = _fromDate;
	}
	public Date getToDate() {
		return _toDate;
	}
	public void setToDate(Date _toDate) {
		this._toDate = _toDate;
	}
	
}
