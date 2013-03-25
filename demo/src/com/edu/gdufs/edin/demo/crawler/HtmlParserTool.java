package com.edu.gdufs.edin.demo.crawler;

import java.util.HashSet;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.LinkRegexFilter;
import org.htmlparser.filters.LinkStringFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.parserapplications.filterbuilder.Filter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.edu.gdufs.edin.demo.crawler.linkfilters.LinkFilter;

public class HtmlParserTool {
	//获取一个网站上的链接，filter来过滤链接
	public static Set<String> extracLinks(String url,LinkFilter filter){
		Set<String> links = new HashSet<String>();
		
		try {
			Parser parser = new Parser(url);
			parser.setEncoding("gb2312");
			
			// 过滤<frame >标签的filter，用来提取frame 标签里的src 属性
			NodeFilter frameFilter = new NodeFilter(){
				public boolean accept(Node node){
					if(node.getText().startsWith("frame src=")){
						return true;
					}else{
						return false;
					}
				}
			};
			
			// OrFilter 来设置过滤<a> 标签和<frame> 标签
			OrFilter linkFilter = new OrFilter(new NodeClassFilter(LinkTag.class),frameFilter);
			// 得到所有经过过滤的标签
			NodeList list = parser.extractAllNodesThatMatch(linkFilter);
			for(int i = 0; i < list.size(); i++){
				Node tag = list.elementAt(i);
				if(tag instanceof LinkTag){		//<a> 标签
					LinkTag link = (LinkTag) tag;
					String linkUrl = link.getLink();	//URL
					if(filter.accept(linkUrl)){
						links.add(//java.net.URLEncoder.encode(linkUrl));
								linkUrl.replaceAll("\\?", "\\%3F")//转码
								.replaceAll("\\&", "\\%26")
								.replaceAll("\\|", "\\%124")
								.replaceAll("\\#", ""));
					};
				}else{							//<frame>标签
					// 提取frame 里src 属性的链接，如<frame src="test.html"/>
					String frame = tag.getText();
					int start = frame.indexOf("src=");
					frame = frame.substring(start);
					int end = frame.indexOf(" ");
					if (end == -1){
						end = frame.indexOf(">");
					}
					String frameUrl = frame.substring(5, end - 1);
					if (filter.accept(frameUrl)){
						links.add(frameUrl);
					}
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return links;
	}
}
