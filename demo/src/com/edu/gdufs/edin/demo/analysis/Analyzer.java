package com.edu.gdufs.edin.demo.analysis;

import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.hibernate.CacheMode;
import org.hibernate.HibernateException;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.gdufs.edin.demo.crawler.MyCrawler;
import com.edu.gdufs.edin.demo.model.HibernateUtil;
import com.edu.gdufs.edin.demo.model.News;
import com.edu.gdufs.edin.demo.model.NewsCounter;

public abstract class Analyzer {
	
	final  Logger logger  =  LoggerFactory.getLogger(Analyzer.class);
	
	private static final int COUNTTHRESHOLD = 500;
	
	private Date _fromDate;
	private Date _toDate;

	public void startAnalyze(Date fromDate,Date toDate){

		_fromDate = fromDate;
		_toDate = toDate;
		
		while(!checkNews()){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fromDate);
			while(calendar.getTime().before(toDate)){
				MyCrawler crawler = new MyCrawler();
				crawler.synCrawling(calendar.getTime());
				calendar.add(Calendar.DATE, 1);
			}
		}
		
		Session session = HibernateUtil.currentSession();
		
		Transaction tx = session.beginTransaction();
		
		ScrollableResults news;
		try {
			news = session.createQuery("from News n where n.date<=:todate and n.date>=:fromdate")
					.setDate("todate",_toDate)
					.setDate("fromdate", _fromDate)
					.setCacheMode(CacheMode.IGNORE)
					.scroll(ScrollMode.FORWARD_ONLY);
			
			while(news.next()){
				News n = (News) news.get(0);
				System.out.println("title:"+n.getTitle());
				
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} 
		

		
	}
	
	private boolean checkNews(){

		Session session = HibernateUtil.currentSession();
		
		Transaction tx = session.beginTransaction();
		
		boolean returnValue = true;
		
		ScrollableResults newsCounter;
		try {
			newsCounter = session.createQuery("from NewsCounter nc where nc.date>=:fromdate and nc.date<=:todate")
					.setDate("fromdate",_fromDate)
					.setDate("todate",_toDate)
					.setCacheMode(CacheMode.IGNORE)
					.scroll(ScrollMode.FORWARD_ONLY);
			
			int NewsCount=0;
			while(newsCounter.next()){
				NewsCounter nc = (NewsCounter) newsCounter.get(0);
				NewsCount+=nc.getCount();
			}
			if(NewsCount>COUNTTHRESHOLD){
				returnValue = true;
			}
				
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returnValue = false;
		} finally{
			return returnValue;
		}
		

	}
	
}
