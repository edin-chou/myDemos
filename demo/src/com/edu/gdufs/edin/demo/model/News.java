package com.edu.gdufs.edin.demo.model;

import java.sql.Date;

public class News {

	private Integer _id;
	private Date _date;
	private String _title;
	private String _source;
	private String _mediaid;
	private String _content;
	
	public Integer getId() {
		return _id;
	}
	public void setId(Integer _id) {
		this._id = _id;
	}
	public Date getDate() {
		return _date;
	}
	public void setDate(Date _date) {
		this._date = new Date(_date.getTime());
	}
	public String getTitle() {
		return _title;
	}
	public void setTitle(String _title) {
		this._title = _title;
	}
	public String getSource() {
		return _source;
	}
	public void setSource(String _source) {
		this._source = _source;
	}
	public String getMediaid() {
		return _mediaid;
	}
	public void setMediaid(String _mediaid) {
		this._mediaid = _mediaid;
	}
	public String getContent() {
		return _content;
	}
	public void setContent(String _content) {
		this._content = _content;
	}
	
	public String toString(){
		return "title:"+(_title==null?"":_title)+"\n"
				+"date:"+(_date==null?"":_date.toString())+"\n"
						+"mediaid:"+(_mediaid==null?"":_mediaid)+"\n"
								+"source:"+(_source==null?"":_source)+"\n"
										+"content:"+(_content==null?"":_content);
		
	}
	
	
}
