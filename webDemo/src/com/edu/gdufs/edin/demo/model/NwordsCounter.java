package com.edu.gdufs.edin.demo.model;

import java.util.Date;

public class NwordsCounter {
	
	private Integer _id;
	private Date _fromDate;
	private Date _toDate;
	private Integer _nlettersCount;
	private Integer _nwordsCount;
	
	public Integer getId() {
		return _id;
	}
	public void setId(Integer id) {
		this._id = id;
	}
	public Date getFromDate() {
		return _fromDate;
	}
	public void setFromDate(Date fromDate) {
		this._fromDate = fromDate;
	}
	public Date getToDate() {
		return _toDate;
	}
	public void setToDate(Date toDate) {
		this._toDate = toDate;
	}
	public Integer getNlettersCount() {
		return _nlettersCount;
	}
	public void setNlettersCount(Integer _nlettersCount) {
		this._nlettersCount = _nlettersCount;
	}
	public Integer getNwordsCount() {
		return _nwordsCount;
	}
	public void setNwordsCount(Integer _nwordsCount) {
		this._nwordsCount = _nwordsCount;
	}

	
	

}
