package com.edu.gdufs.edin.demo.crawler.cobweb;

import org.htmlparser.NodeFilter;

import com.edu.gdufs.edin.demo.model.NewsCounter;

public interface Cobweb {
	public boolean accept(String url);
	public String[] getSeeds();
	public NodeFilter getNodeFilter();
	public String getCharSet();
	public String getFrom();
	public NewsCounter getNewsCounter();
	public void setNewsCounter(NewsCounter newsCounter);
}
