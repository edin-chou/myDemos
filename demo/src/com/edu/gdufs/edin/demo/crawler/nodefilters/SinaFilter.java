package com.edu.gdufs.edin.demo.crawler.nodefilters;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TagNode;

public class SinaFilter implements NodeFilter {

	@Override
	public boolean accept(Node node) {
        if (node instanceof Tag == false) return false; // 结点不是Tag则返回
        if (((Tag)node).isEndTag()) return false; // 结点是结束标记则返回
        TagNode tmpNode = (TagNode)node;
        String tagName = tmpNode.getTagName();
        String tagId = tmpNode.getAttribute("id");
        String name = tmpNode.getAttribute("name");
        if(name!=null&&name.equalsIgnoreCase("mediaid")&&tagName.equalsIgnoreCase("meta")){
        	tmpNode.setAttribute("id","media_name");
        	return true;
        }else if(tagId!=null){
        	if(tagId.equalsIgnoreCase("pub_date")&&tagName.equalsIgnoreCase("span")){
        		tmpNode.setAttribute("id","pub_date");
        		return true;
        	}
        	if(tagId.equalsIgnoreCase("artibody")&&tagName.equalsIgnoreCase("div")){
        		tmpNode.setAttribute("id","body_content");
        		return true;        		
        	}
        }else if(tagName.equalsIgnoreCase("title")){
        	return true;
        }
        return false;
	}

}
