package com.edu.gdufs.edin.demo.crawler;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.filters.*;

public class MyCrawler {
	
	private LinkQueue linkQueue = new LinkQueue();
	
	/**
	 * 使用种子初始化URL队列
	 * @param seeds
	 */
	
	private void initCrawlerWithSeeds(String[] seeds){
		for(int i=0;i<seeds.length;i++){
			linkQueue.addUnvisitedUrl(seeds[i]);
		}
	}
	
	/**
	 * 抓取过程
	 * @param seeds
	 */
	public void crawling(String[] seeds,LinkFilter filter){
		
		//初始化 URL 队列
		initCrawlerWithSeeds(seeds);
		//循环条件：待抓取的链接不空且抓取的网页不多于1000
		
		while(!linkQueue.unVisitedUrlsEmpty()
				&&linkQueue.getVisitedUrlNum()<=1000){
			//对头URL出队列
			String visitUrl = (String) linkQueue.unVisitedUrlDeQueue();
			if(visitUrl==null)
				continue;
			//下载网页
			String tmpPath = DownLoadFile.downloadFile(visitUrl);
			//该URL放入已经访问的URL中
			linkQueue.addUnvisitedUrl(visitUrl);
			//提取下载网页中的URL
			if(tmpPath!=null){
				Set<String> links = HtmlParserTool.extracLinks(visitUrl, filter);
				//新的未访问的URL入队
				for(String link:links){
					linkQueue.addUnvisitedUrl(link);
				}
			}
		}
	}
		
	//main 方法入口
	public static void main(String[] args){

/*		//自定义过滤器，提取以种子连接为开头的链接
		LinkFilter filter = new LinkFilter(){
			public boolean accept(String url){
				if(url == null){
					return false;
				}
				//正则检查url合法性
				String regEx = "^(http|https|ftp)//://([a-zA-Z0-9//.//-]+(//:[a-zA-"
						+ "Z0-9//.&%//$//-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
						+ "2}|[1-9]{1}[0-9]{1}|[1-9])//.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
						+ "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)//.(25[0-5]|2[0-4][0-9]|"
						+ "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)//.(25[0-5]|2[0-"
						+ "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
						+ "-9//-]+//.)*[a-zA-Z0-9//-]+//.[a-zA-Z]{2,4})(//:[0-9]+)?(/"
						+ "[^/][a-zA-Z0-9//.//,//?//'///////+&%//$//=~_//-@]*)*$";
				Pattern p = Pattern.compile(regEx);
				Matcher matcher = p.matcher(url);
			    if(!matcher.matches()){
			    	return true;
			    }
				return false;
			}
		};*/
		
		
		MyCrawler crawler = new MyCrawler();
		//自定义过滤器，提取以种子连接为开头的链接
		LinkFilter filter = new LinkFilter(){
			public boolean accept(String url){
			    if(url.startsWith("http://news.ifeng.com/s")&&!url.matches("//s")){
			    	return true;
			    }
				return false;
			}
		};
		crawler.crawling(new String[]{"http://news.ifeng.com/society/"},filter);
	}
}
