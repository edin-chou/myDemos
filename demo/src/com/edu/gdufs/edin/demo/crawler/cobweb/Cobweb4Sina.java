package com.edu.gdufs.edin.demo.crawler.cobweb;

import org.htmlparser.NodeFilter;

import com.edu.gdufs.edin.demo.crawler.nodefilters.SinaFilter;
import com.edu.gdufs.edin.demo.model.NewsCounter;


public class Cobweb4Sina implements Cobweb {
	
	private String[] _seeds={"http://news.sina.com.cn/society/"};
	
	private final String _from = "新浪网社会频道";

	private NewsCounter _newsCounter;

	@Override
	public boolean accept(String url){
	    if(url.startsWith("http://news.sina.com.cn/s")&&!url.matches("^.+[/s '].?")){
	    	return true;
	    }
		return false;
	}

	@Override
	public String[] getSeeds() {
		// TODO Auto-generated method stub
		return _seeds;
	}

	@Override
	public NodeFilter getNodeFilter() {
		// TODO Auto-generated method stub
		return new SinaFilter();
	}

	@Override
	public String getCharSet() {
		// TODO Auto-generated method stub
		return "gb2312";
	}

	@Override
	public String getFrom() {
		// TODO Auto-generated method stub
		return _from;
	}
	
	@Override
	public NewsCounter getNewsCounter() {
		// TODO Auto-generated method stub
		return _newsCounter;
	}

	@Override
	public void setNewsCounter(NewsCounter newsCounter) {
		// TODO Auto-generated method stub
		_newsCounter=newsCounter;
	}
}
