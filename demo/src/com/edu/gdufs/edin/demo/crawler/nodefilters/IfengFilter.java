package com.edu.gdufs.edin.demo.crawler.nodefilters;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

public class IfengFilter implements NodeFilter {

	@Override
	public boolean accept(Node node) {
        if (node instanceof Tag == false) return false; // 结点不是Tag则返回
        if (((Tag)node).isEndTag()) return false; // 结点是结束标记则返回
        TagNode tmpNode = (TagNode)node;
        String tagName = tmpNode.getTagName();
        String tagId = tmpNode.getAttribute("id");
        if(tagId!=null){
        	if(tagId.equalsIgnoreCase("artical_sth")&&tagName.equalsIgnoreCase("div")){
        		TagNode tmpNode2 = (TagNode)tmpNode.getChildren().elementAt(1).getChildren().elementAt(1);
        		tmpNode2.setAttribute("id","pub_date");
        		TagNode tmpNode3 = (TagNode)tmpNode.getChildren().elementAt(1).getChildren().elementAt(4).getFirstChild();
        		tmpNode3.setAttribute("id","media_name");
        		tmpNode3.setAttribute("content",tmpNode3.toPlainTextString());
        		return false;
        	}
        	if(tagId.equalsIgnoreCase("main_content")&&tagName.equalsIgnoreCase("div")){
        		tmpNode.setAttribute("id","body_content");
        		return true;        		
        	}
        	if(tagId.equalsIgnoreCase("pub_date")||tagId.equalsIgnoreCase("media_name")){
            	return true;
            }
        }else if(tagName.equalsIgnoreCase("title")){
        	return true;
        }
        return false;
	}

}
