package com.edu.gdufs.edin.demo.model;

import java.util.Date;

public class NewsCounter {
	
	private Integer _id;
	private Date _date;
	private String _from;
	private Integer _count;
	
	public Integer getId() {
		return _id;
	}
	public void setId(Integer id) {
		this._id = id;
	}
	public Date getDate() {
		return _date;
	}
	public void setDate(Date date) {
		this._date = new Date(date.getTime());
	}
	public String getFrom() {
		return _from;
	}
	public void setFrom(String from) {
		this._from = from;
	}
	public Integer getCount() {
		return _count;
	}
	public void setCount(Integer count) {
		this._count = count;
	}
	public void increase(){
		this._count++;
	}
	public String toString(){
		return "_id:"+(_id==null?"":_id)+"\n"
				+"_date:"+(_date==null?"":_date.toString())+"\n"
						+"_from:"+(_from==null?"":_from)+"\n"
								+"_count:"+(_count==null?"":_count);
		
	}
}
