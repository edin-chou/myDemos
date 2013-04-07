package com.edu.gdufs.edin.demo.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

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
import com.edu.gdufs.edin.demo.model.Nword;
import com.edu.gdufs.edin.demo.model.NwordsCounter;

public class Analyzer {
	
	final  Logger logger  =  LoggerFactory.getLogger(Analyzer.class);

	private static final int COUNTTHRESHOLD = 500;
	private static final int WORD_TO_SAVE_THRESHOLD = 2000;
	private static final String FROMSTRINGS = "新浪网社会频道,凤凰网社会频道";
	private static final String SAVE_PATH = "E:/排序/";
	
	private Date _fromDate;
	private Date _toDate;
	private WordsAnalyzingCounter _wordsAnalyzingCounter;

	public boolean startAnalyze(Date fromDate,Date toDate){

		_fromDate = fromDate;
		_toDate = toDate;
		_wordsAnalyzingCounter = new WordsAnalyzingCounter();
	
		//篇数是否通过
		int newsCount = checkNews();
			
		logger.warn("analyzer starts!");
			
		Session session = HibernateUtil.currentSession();
		
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
			logger.warn("\npreSort results save in:"+sortResults[0]+"\npostSort results save in"+sortResults[1]);	
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		try{
			logger.warn("start analyzing...");
			WordWriter ww = new WordWriter4DOCAndRDOF(_wordsAnalyzingCounter);
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
			f.delete();
			logger.warn("total count:"+ct.getTotalCount()+"\t");
			logger.warn("Analyzing1 spends "+(end - start)+"ms");
			ww=null;
			ct=null;
			System.gc();
				
			WordWriter ww2 = new WordWriter4DOCAndLDOF(_wordsAnalyzingCounter);
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
			br2.close();
			ct2.close();
			f2.delete();
			logger.warn("total count:"+ct2.getTotalCount()+"\t");
			logger.warn("Analyzing2 spends "+(end2 - start2)+"ms");
		}catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;

	}
	
	
	public void writeToTxt(){
		
		logger.warn("start final sorting...");
		
		File f = new File("e:/排序/result"+System.currentTimeMillis()+".txt");
		try {
			long start3 = System.currentTimeMillis();
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			ComparatorImp c = new ComparatorImp();
			ArrayList al= new ArrayList(_wordsAnalyzingCounter._wordsMap.entrySet());
			Collections.sort(al, c);
			Iterator i = al.iterator();
			int word_to_save_threshold_counter = 0;
			
			while(i.hasNext()){
				Entry<String,Word> entry = (Entry<String,Word>)i.next();
				Word w = entry.getValue();
				if(w!=null&&w._lentropy>0&&w._rentropy>0&&word_to_save_threshold_counter++<WORD_TO_SAVE_THRESHOLD){
					bw.write(w._word+/*"\t"
							+w._count+"\t"
							+w._mutualinfo+"\t"
							+w._lentropy+"\t"
							+w._rentropy+*/"\n"
							);
				}
			}
			bw.flush();
			bw.close();
			long end3 = System.currentTimeMillis();
			System.out.println("final sorting spends:"+(end3-start3)+"ms");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ;
	}
	
	public void writeToDataBase(){
		ComparatorImp c = new ComparatorImp();
		ArrayList al= new ArrayList(_wordsAnalyzingCounter._wordsMap.entrySet());
		Collections.sort(al, c);
		Iterator i = al.iterator();
		int word_to_save_threshold_counter = 0;
		Session sess = HibernateUtil.currentSession();
		Query q = sess.createQuery("from NwordsCounter nwc where nwc.fromDate=:fromDate and nwc.toDate=:toDate")
			.setDate("fromDate", _fromDate)
			.setDate("toDate",_toDate);
		NwordsCounter nwc = (NwordsCounter)q.uniqueResult();
		if(nwc!=null){
			q = sess.createQuery("delete Nword nw where nw.nwordsCountId=:nowrdsCountId")
					.setInteger("nwordsCountId", nwc.getId());
		}else{
			nwc=new NwordsCounter();
			nwc.setFromDate(_fromDate);
			nwc.setToDate(_toDate);
			sess.save(nwc);
		}
		
		while(i.hasNext()){
			Entry<String,Word> entry = (Entry<String,Word>)i.next();
			Word w = entry.getValue();
			if(w!=null&&w._lentropy>0&&w._rentropy>0&&word_to_save_threshold_counter++<WORD_TO_SAVE_THRESHOLD){
				Nword nword = new Nword();
				nword.setWord(w._word);
				nword.setRentropy(w._rentropy);
				nword.setLentropy(w._rentropy);
				nword.setMutualInfo(w._mutualinfo);
				nword.setNwordCount(w._count);
				nword.setNwordsCountId(nwc.getId());
				sess.save(nword);
			}
		}
		
		HibernateUtil.closeSession();
	}
	
	private int checkNews(){

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
		return counter;
	}


	
	public static void main(String args[]){
		for(int i = 7;i>0;i--){
			Analyzer a = new Analyzer();
			Calendar c = Calendar.getInstance();
			c.set(2013, 3, i, 0, 0, 0);
			Date date1 = c.getTime();
			c.add(Calendar.DATE,-3);
			Date date2 = c.getTime();
			a.startAnalyze(date2, date1);
			a.writeToDataBase();
		}
		
/*		Analyzer a = new Analyzer();
		Calendar c = Calendar.getInstance();
		c.set(2013, 3, 7, 0, 0, 0);
		Date date1 = c.getTime();
		c.add(Calendar.DATE,-3);
		Date date2 = c.getTime();
		a.startAnalyze(date2, date1);
		a.writeToTxt();*/
	}
	
}
class ComparatorImp implements Comparator<Entry<String,Word>> {

	@Override
	public int compare(Entry<String,Word> o1, Entry<String,Word> o2) {
		return o2.getValue()._count-o1.getValue()._count;
	}
}
