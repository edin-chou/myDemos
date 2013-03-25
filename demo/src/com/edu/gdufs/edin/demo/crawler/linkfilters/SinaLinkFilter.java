package com.edu.gdufs.edin.demo.crawler.linkfilters;

import org.htmlparser.NodeFilter;

import com.edu.gdufs.edin.demo.crawler.nodefilters.SinaFilter;


public class SinaLinkFilter implements LinkFilter {
	
	private String[] _seeds={"http://news.sina.com.cn/society/"};

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

}
