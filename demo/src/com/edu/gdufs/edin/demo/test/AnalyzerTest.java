package com.edu.gdufs.edin.demo.test;


import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.hibernate.CacheMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.edu.gdufs.edin.demo.model.HibernateUtil;
import com.edu.gdufs.edin.demo.model.News;

public class AnalyzerTest {
	
	public static void main(String args[]) throws ParseException{
		Session session = HibernateUtil.currentSession();
		
		Transaction tx = session.beginTransaction();

		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2013,2, 29,0,0,0);
		long date1 = calendar.getTime().getTime();
		calendar.add(Calendar.DATE, 0);
		long date2 = calendar.getTime().getTime();

		System.out.println(new Date(date1));
		System.out.println(new Date(date2));
		
		ScrollableResults news = session.createQuery("from News n where n.date<=:date1 and n.date>=:date2")
				.setDate("date1",new Date(date1))
				.setDate("date2", new Date(date2))
				.setCacheMode(CacheMode.IGNORE)
				.scroll(ScrollMode.FORWARD_ONLY);
		int count = 0;
		while(news.next()){
			News n = (News) news.get(0);
			System.out.println(n.toString());
			System.out.println("count:"+(++count));
			System.out.println("====================");
		}
		HibernateUtil.closeSession();
	}

}
