package com.edu.gdufs.edin.demo.crawler.cobweb;

import java.util.LinkedList;
import java.util.List;

import org.htmlparser.NodeFilter;

public interface Cobweb {
	
	public List idList = new LinkedList();
	public boolean accept(String url);
	public String[] getSeeds();
	public NodeFilter getNodeFilter();
	public String getCharSet();
	public String getFrom();
	
}
