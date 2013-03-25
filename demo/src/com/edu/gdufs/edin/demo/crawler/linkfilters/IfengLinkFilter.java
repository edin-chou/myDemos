package com.edu.gdufs.edin.demo.crawler.linkfilters;

import org.htmlparser.NodeFilter;

import com.edu.gdufs.edin.demo.crawler.nodefilters.IfengFilter;
import com.edu.gdufs.edin.demo.crawler.nodefilters.SinaFilter;

public class IfengLinkFilter implements LinkFilter{

	private String[] _seeds={"http://news.ifeng.com/society/"};

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

}
