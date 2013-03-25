package com.edu.gdufs.edin.demo.crawler;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.gdufs.edin.demo.crawler.linkfilters.IfengLinkFilter;
import com.edu.gdufs.edin.demo.crawler.linkfilters.LinkFilter;

public class MyCrawler {
	
	final  Logger logger  =  LoggerFactory.getLogger(MyCrawler.class);
	
	private static final int MAXCOUNT = 1000;
	
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
	public void crawling(LinkFilter filter){
		
		//初始化 URL 队列
		initCrawlerWithSeeds(filter.getSeeds());
		//初始化可持续化存储类
		Storage stor = new Storage();
		//循环条件：待抓取的链接不空且抓取的网页不多于MAXCOUNT
		while(!linkQueue.unVisitedUrlsEmpty()
				&&linkQueue.getVisitedUrlNum()<=MAXCOUNT){
			//对头URL出队列
			String visitUrl = (String) linkQueue.unVisitedUrlDeQueue();
			if(visitUrl==null)
				continue;
			//实现可持续化存储（存入数据库）
			String tmpPath = stor.downloadFile(visitUrl,filter.getNodeFilter(),filter.getCharSet());
			//该URL放入已经访问的URL中
			linkQueue.addVisitedUrl(visitUrl);
			//提取下载网页中的URL
			if(tmpPath!=null){
				Set<String> links = HtmlParserTool.extracLinks(visitUrl, filter);
				//新的未访问的URL入队
				for(String link:links){
					linkQueue.addUnvisitedUrl(link);
				}
			}
			logger.warn("linkQueue count:"+linkQueue.getVisitedUrlNum());
		}
		//关闭持续化存储对象
		stor.close();
	}
		
	//main 方法入口
	public static void main(String[] args){

/*		MyCrawler crawler = new MyCrawler();
		//自定义过滤器，提取以种子连接为开头的链接
		LinkFilter filter = new SinaLinkFilter();
		crawler.crawling(filter);
*/
		MyCrawler crawler = new MyCrawler();
		//自定义过滤器，提取以种子连接为开头的链接
		LinkFilter filter = new IfengLinkFilter();
		crawler.crawling(filter);
		
		
	}
}
