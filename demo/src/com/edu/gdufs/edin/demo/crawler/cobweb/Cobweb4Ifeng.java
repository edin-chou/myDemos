package com.edu.gdufs.edin.demo.crawler.cobweb;

import org.htmlparser.NodeFilter;

import com.edu.gdufs.edin.demo.crawler.nodefilters.IfengFilter;
import com.edu.gdufs.edin.demo.crawler.nodefilters.SinaFilter;
import com.edu.gdufs.edin.demo.model.NewsCounter;

public class Cobweb4Ifeng implements Cobweb{

	private String[] _seeds={"http://news.ifeng.com/society/"};
	
	private final String _from = "凤凰网社会频道";
	
	private NewsCounter _newsCounter;

	@Override
	public boolean accept(String url){
	    if(url.startsWith("http://news.ifeng.com/society/")&&!url.matches("^.+[/s '].?")){
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
		return new IfengFilter();
	}

	@Override
	public String getCharSet() {
		// TODO Auto-generated method stub
		return "utf-8";
	}

	@Override
	public String getFrom() {
		// TODO Auto-generated method stub
		return _from;
	}


}
