package com.edu.gdufs.edin.demo.crawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class LinkQueue {
	
	//已经访问过的页面，用于去重
	private Set visitedUrl = new HashSet();
	
	private Queue unVisitedUrl = new LinkedList<String>();
	
	public Queue getUnVisitedUrl(){
		return unVisitedUrl;
	}
	
	public void addVisitedUrl(String url){
		visitedUrl.add(url);
	}
	
	private void removeVisitedUrl(String url){
		visitedUrl.remove(url);
	}
	
	public Object unVisitedUrlDeQueue(){
		return unVisitedUrl.poll();
	}
	
	
	public void addUnvisitedUrl(String url){
		if(url != null && !url.trim().equals("")
				&& !visitedUrl.contains(url)
				&& !unVisitedUrl.contains(url))
			unVisitedUrl.add(url);
	}
	
	public int getVisitedUrlNum(){
		return visitedUrl.size();
	}
	
	public boolean unVisitedUrlsEmpty(){
		return unVisitedUrl.isEmpty();
	}

}
