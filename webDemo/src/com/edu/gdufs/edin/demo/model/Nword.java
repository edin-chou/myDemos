package com.edu.gdufs.edin.demo.model;

import java.util.List;

public class Nword {
	
	private Integer _id;
	private String _word;
	private Double _lentropy;
	private Double _rentropy;
	private Integer _nwordCount;
	private Double _mutualInfo;
	private Integer _nwordsCountId;
	private int _newsTotalCount;
	private List<News> _newsList;
	
	
	
	public int getNewsTotalCount() {
		return _newsTotalCount;
	}
	public void setNewsTotalCount(int _newsTotalCount) {
		this._newsTotalCount = _newsTotalCount;
	}
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
	public Double getLentropy() {
		return _lentropy;
	}
	public void setLentropy(Double _lentropy) {
		this._lentropy = _lentropy;
	}
	public Double getRentropy() {
		return _rentropy;
	}
	public void setRentropy(Double _rentropy) {
		this._rentropy = _rentropy;
	}
	public Double getMutualInfo() {
		return _mutualInfo;
	}
	public void setMutualInfo(Double _mutualInfo) {
		this._mutualInfo = _mutualInfo;
	}
	public Integer getNwordsCountId() {
		return _nwordsCountId;
	}
	public void setNwordsCountId(Integer _nwordsCountId) {
		this._nwordsCountId = _nwordsCountId;
	}
	public Integer getNwordCount() {
		return _nwordCount;
	}
	public void setNwordCount(Integer _nwordCount) {
		this._nwordCount = _nwordCount;
	}
	public List<News> getNewsList() {
		return _newsList;
	}
	public void setNewsList(List<News> _newsList) {
		this._newsList = _newsList;
	}
	
	
	

}
