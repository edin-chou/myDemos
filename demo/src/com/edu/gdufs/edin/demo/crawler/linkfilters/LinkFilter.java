package com.edu.gdufs.edin.demo.crawler.linkfilters;

import org.htmlparser.NodeFilter;

public interface LinkFilter {
	public boolean accept(String url);
	public String[] getSeeds();
	public NodeFilter getNodeFilter();
	public String getCharSet();
}
