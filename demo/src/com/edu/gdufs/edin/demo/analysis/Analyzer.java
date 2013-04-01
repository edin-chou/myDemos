package com.edu.gdufs.edin.demo.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.gdufs.edin.demo.analysis.sort.ExtSort;
import com.edu.gdufs.edin.demo.model.HibernateUtil;
import com.edu.gdufs.edin.demo.model.News;
import com.edu.gdufs.edin.demo.model.NewsCounter;
import com.edu.gdufs.edin.demo.model.NwordsCounter;

public class Analyzer {
	
	final  Logger logger  =  LoggerFactory.getLogger(Analyzer.class);
	
	private static final int COUNTTHRESHOLD = 500;
	private static final String FROMSTRINGS = "新浪网社会频道,凤凰网社会频道";
	private static final String SAVE_PATH = "E:/排序";
	
	private Date _fromDate;
	private Date _toDate;

	public void startAnalyze(Date fromDate,Date toDate){
		
		_fromDate = fromDate;
		_toDate = toDate;
		
		
		Session session = HibernateUtil.currentSession();
		
		Transaction tx = session.beginTransaction();
		
		Query query = session.createQuery("from NwordsCounter nwc where nwc.fromDate=:fromDate and nwc.toDate=:toDate")
				.setDate("fromDate", _fromDate)
				.setDate("toDate", _toDate);

		NwordsCounter nwc = (NwordsCounter) query.uniqueResult();
		
		if(nwc==null){
			nwc = new NwordsCounter();
			nwc.setFromDate(_fromDate);
			nwc.setToDate(_toDate);
			nwc.setNlettersCount(0);
			session.save(nwc);
			tx.commit();
			HibernateUtil.closeSession();
		}else{
			nwc.setNlettersCount(0);
		}
		//篇数是否通过
		if(!checkNews()){
			
/*			MyCrawler crawler = new MyCrawler();
			//自定义过滤器，提取以种子连接为开头的链接
			Cobweb cobweb = new Cobweb4Sina();
			crawler.crawling(cobweb);
			
			MyCrawler crawler2 = new MyCrawler();
			//自定义过滤器，提取以种子连接为开头的链接
			Cobweb cobweb2 = new Cobweb4Ifeng();
			crawler2.crawling(cobweb2);*/
			
			logger.warn("analyzer not allowed!");
		}else{


			logger.warn("analyzer starts!");
			
			session = HibernateUtil.currentSession();
			
			tx = session.beginTransaction();

			int count1 = 0;
			
			String[] sortResults = null;
			
			ScrollableResults news;
			try {
				news = session.createQuery("from News n where n.date<=:todate and n.date>=:fromdate")
						.setDate("todate",_toDate)
						.setDate("fromdate", _fromDate)
						.setCacheMode(CacheMode.IGNORE)
						.scroll(ScrollMode.FORWARD_ONLY);
				ExtSort eSort = new ExtSort(SAVE_PATH);
				while(news.next()){
					News n = (News) news.get(0);
					String tmp[] = n.getContent().replaceAll("[^\u4E00-\u9FA5]+", " ").trim().split(" ");
					for(String s:tmp){
						eSort.addWord(s);
					}
				}
				HibernateUtil.closeSession();
				sortResults = eSort.finished();
				logger.warn(sortResults[0]+"\n"+sortResults[1]);
				
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			
			
			try{
				logger.warn("start analyzing...");
				String savePath1 = SAVE_PATH+"\\preAnalyzed"+System.nanoTime()+".txt";
				WordWriter ww = new WordWriter4DOCAndRDOF(savePath1,nwc);
				CharTree ct = new CharTree(ww);
				File f = new File(sortResults[0]);
				long start = System.currentTimeMillis();
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
				String tmp = br.readLine();
				while(tmp!=null){
					ct.addWords(new StringBuffer(tmp));
					tmp = br.readLine();
				}
				long end = System.currentTimeMillis();
				br.close();
				ct.close();
				logger.warn("total count:"+ct.getTotalCount()+"\t");
				logger.warn("Analyzing1 spends "+(end - start)+"ms");
				logger.warn("Analyzed1 data was saved in \""+savePath1+"\"");
				ww=null;
				ct=null;
				System.gc();
				
				String savePath2 = SAVE_PATH+"/postAnalyzed"+System.nanoTime()+".txt";
				WordWriter ww2 = new WordWriter4DOCAndLDOF(savePath2,nwc);
				CharTree ct2 = new CharTree(ww2);
				File f2 = new File(sortResults[1]);
				long start2 = System.currentTimeMillis();
				BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(f2)));
				String tmp2 = br2.readLine();
				while(tmp2!=null){
					ct2.addWords(new StringBuffer(tmp2));
					tmp2 = br2.readLine();
				}
				long end2 = System.currentTimeMillis();
				ct2.close();
				logger.warn("total count:"+ct2.getTotalCount()+"\t");
				logger.warn("Analyzing2 spends "+(end2 - start2)+"ms");
				logger.warn("Analyzed2 data was saved in \""+savePath2+"\"");
				
				
				
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			
		}
	}
	
	private boolean checkNews(){

		Session session = HibernateUtil.currentSession();
		
		Transaction tx = session.beginTransaction();
		
		Query query = session.getNamedQuery("getNewsCounters")
			.setDate("fromDate", _fromDate)
			.setDate("toDate", _toDate)
			.setString("fromStrings", FROMSTRINGS);
		
		List list = query.list();

		logger.warn("listcount is:"+list.size());
		Iterator i = list.iterator();
		
		int counter = 0;
		
		while(i.hasNext()){
			NewsCounter nc = (NewsCounter) i.next();
			counter+=nc.getCount();
		}
		logger.warn("newscount is:"+counter);
		if(counter>COUNTTHRESHOLD){
			return true;
		}else{
			return false;
		}

	}
	
	public static void main(String args[]){
		Analyzer a = new Analyzer();
		Calendar c = Calendar.getInstance();
		c.set(2013, 2, 30, 0, 0, 0);
		Date date1 = c.getTime();
		c.add(Calendar.DATE,-3);
		Date date2 = c.getTime();
		a.startAnalyze(date2, date1);
	}
	
}
