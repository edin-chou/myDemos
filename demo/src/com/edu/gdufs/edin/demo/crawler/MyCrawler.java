package com.edu.gdufs.edin.demo.crawler;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.gdufs.edin.demo.crawler.cobweb.Cobweb4Ifeng;
import com.edu.gdufs.edin.demo.crawler.cobweb.Cobweb;
import com.edu.gdufs.edin.demo.crawler.cobweb.Cobweb4Sina;
import com.edu.gdufs.edin.demo.model.HibernateUtil;
import com.edu.gdufs.edin.demo.model.NewsCounter;

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
	public void crawling(Cobweb cobweb,Date date){
		
		//初始化 URL 队列
		initCrawlerWithSeeds(cobweb.getSeeds());
		
		//获取爬虫计数器信息
		Session session = HibernateUtil.currentSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("from NewsCounter nc where nc.date=:date and nc.from=:from")
				.setDate("date",date)
				.setString("from",cobweb.getFrom());
		NewsCounter newsCounter = (NewsCounter)query.uniqueResult();
		
		if(newsCounter==null){
			newsCounter = new NewsCounter();
			newsCounter.setFrom(cobweb.getFrom());
			newsCounter.setDate(date);
			newsCounter.setCount(0);
			session.save(newsCounter);
			transaction.commit();
			HibernateUtil.closeSession();
		}
		
		cobweb.setNewsCounter(newsCounter);

		//初始化可持续化存储类
		Storage stor = new Storage();
		//循环条件：待抓取的链接不空且抓取的网页不多于MAXCOUNT
		while(!linkQueue.unVisitedUrlsEmpty()
				&&linkQueue.getVisitedUrlNum()<MAXCOUNT){
			//对头URL出队列
			String visitUrl = (String) linkQueue.unVisitedUrlDeQueue();
			if(visitUrl==null)
				continue;
			//实现可持续化存储（存入数据库）
			int flag = stor.downloadFile(visitUrl,cobweb);
			//该URL放入已经访问的URL中
			linkQueue.addVisitedUrl(visitUrl);
			//提取下载网页中的URL
			if(flag>=Storage.SAVESRATE_UNSAVED){
				Set<String> links = HtmlParserTool.extracLinks(visitUrl, cobweb);
				//新的未访问的URL入队
				for(String link:links){
					linkQueue.addUnvisitedUrl(link);
				}
			}
			logger.warn("linkQueue count:"+linkQueue.getVisitedUrlNum());
		}
		//关闭持续化存储对象
		stor.close();
		
		session = HibernateUtil.currentSession();
		transaction = session.beginTransaction();
		session.update(newsCounter);
		transaction.commit();
		HibernateUtil.closeSession();
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
		Cobweb cobweb = new Cobweb4Sina();
		Calendar calendar = Calendar.getInstance();
		calendar.set(2013, 2, 29, 0, 0, 0);
		crawler.crawling(cobweb,calendar.getTime());
	}
}
