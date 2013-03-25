package com.edu.gdufs.edin.demo.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.edu.gdufs.edin.demo.crawler.nodefilters.IfengFilter;
import com.edu.gdufs.edin.demo.crawler.nodefilters.SinaFilter;
import com.edu.gdufs.edin.demo.model.News;

public class HtmlparserTest {
	
	public static void main(String args[]) throws ParseException{
		
		String html = "";
		
		//下载
		try {
		
			String url = "http://news.ifeng.com/mainland/detail_2013_03/25/23462224_0.shtml";
			HttpClient httpclient = new DefaultHttpClient();
			//get方法请求对象

			System.out.println(">");
			System.out.println(url);
			System.out.println("<");
			
			HttpGet httpget = new HttpGet(url);
			//执行请求得到响应
			HttpResponse response = httpclient.execute(httpget);
			//验证响应状态
			System.out.println(response.getStatusLine());
			//若没有响应返回空
			if(response.getStatusLine().getStatusCode()!=200){
				return ;
			}
			//得到响应实体
			HttpEntity entity = response.getEntity();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(),"utf-8"));
			String tmp = br.readLine();
			StringBuffer sb = new StringBuffer();
			while(tmp!=null){
				sb.append(tmp);
				tmp = br.readLine();
			}
			html = sb.toString();
			System.out.println("downloaded!");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Parsing!");
		Parser parser = Parser.createParser(html,"utf-8");
		//OrFilter orFilter = new OrFilter(new TagNameFilter("span"),new TagNameFilter("div"));
		NodeFilter nodeFilter= new IfengFilter();
		
		try {
			News news = new News();
			NodeList nodes = parser.parse(nodeFilter);
			int num = nodes.size();
				for (int i = 0; i < num; i ++) {
				    TagNode node = (TagNode)nodes.elementAt(i);
				    String tagName = node.getTagName();
				    String tagId = node.getAttribute("id");
				    if(tagId!=null){
				    	if(tagId.equalsIgnoreCase("media_name")){
				    		news.setMediaid(node.getAttribute("content").toString());
				    	}else if(tagId.equalsIgnoreCase("pub_date")){
				    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
				    		String tmpStr = node.toPlainTextString();
				    		news.setDate(new Date(sdf.parse(tmpStr.substring(0, tmpStr.lastIndexOf('日')+1)).getTime()));
				    	}else if(tagId.equalsIgnoreCase("body_content")){
				    		news.setContent(node.toHtml());
				    	}
				    }else if(tagName.equalsIgnoreCase("title")){
				    	news.setTitle(node.toPlainTextString());
				    }
				}
			System.out.println(news.toString());
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
