package com.edu.gdufs.edin.demo.analysis;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import com.edu.gdufs.edin.demo.model.HibernateUtil;
import com.edu.gdufs.edin.demo.model.Oword;
import com.edu.gdufs.edin.demo.model.Pword;

public class WordsAnalyzingCounter {

	//三天的新闻保守估计有10w个有效词
	private final int WORDSMAPSIZE = 100000;
	//三天的新闻保守估计有3000个有效字
	private final int LETTERSMAPSIZE = 3000;
	//前沿新词词库每次有1000个词
	public static final int PWORDMAPSIZE = 1000;
	
	protected int _wordsCount = 0;
	protected Map<String,Word> _wordsMap;
	protected Map<String,Integer> _lettersMap;
	//protected Map<String,Pword> _pwordMap;
	protected Set _owordSet;
	
	public WordsAnalyzingCounter(Date fromDate,Date toDate){
		_wordsMap=new HashMap<String,Word>(WORDSMAPSIZE);
		_lettersMap=new HashMap<String,Integer>(LETTERSMAPSIZE);
		getOwordSet();
		//getPwordMap( fromDate, toDate);
	}
	
	public WordsAnalyzingCounter(){
		_wordsMap=new HashMap<String,Word>(WORDSMAPSIZE);
		_lettersMap=new HashMap<String,Integer>(LETTERSMAPSIZE);
		getOwordSet();
	}
	
	private boolean getOwordSet(){
		try{
			Session sess = HibernateUtil.currentSession();
			Query query = sess.createQuery("from Oword");
			List list = query.list();
			Iterator i = list.iterator();
			_owordSet = new HashSet();
			while(i.hasNext()){
				Oword oword = (Oword)i.next();
				_owordSet.add(oword.getWord());
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
/*	private boolean getPwordMap(Date fromDate,Date toDate){
		try{
			Calendar c= Calendar.getInstance();
			c.setTime(fromDate);
			c.add(Calendar.DATE, -1);
			Date fromDateTmp=c.getTime();
			c.setTime(toDate);
			c.add(Calendar.DATE, -1);
			Date toDateTmp=c.getTime();
			Session sess = HibernateUtil.currentSession();
			Query query = sess.createQuery("from Pword pw where pw.fromDate=:fromDate and pw.toDate=:toDate")
					.setDate("fromDate", fromDateTmp)
					.setDate("toDate", toDateTmp);
			List list = query.list();
			Iterator i = list.iterator();
			_pwordMap = new HashMap(PWORDMAPSIZE);
			while(i.hasNext()){
				Pword pword = (Pword)i.next();
				_pwordMap.put(pword.getWord(), pword);
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	*/
	
	
}
