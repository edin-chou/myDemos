package com.edu.gdufs.edin.demo.test;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.Calendar;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.gdufs.edin.demo.analysis.Analyzer;
import com.edu.gdufs.edin.demo.analysis.CharTree;
import com.edu.gdufs.edin.demo.analysis.WordWriter;
import com.edu.gdufs.edin.demo.analysis.WordWriter4DOCAndLDOF;
import com.edu.gdufs.edin.demo.model.HibernateUtil;
import com.edu.gdufs.edin.demo.model.NewsCounter;
import com.edu.gdufs.edin.demo.model.NwordsCounter;



public class Test{

	public static void main(String a[]) throws Exception{
	Logger logger  =  LoggerFactory.getLogger(Test.class);
/*		int inte =0;
		while(inte<257){
			System.out.print(inte+":");
			System.out.write(inte);
			inte++;
		}*/
		/*Test t = new Test();
		t.love();*/
		/*for(;true;Thread.sleep(1000))
			System.out.println((1900+new Date().getYear())/100);*/
		//System.out.println(5+new SimpleDateFormat("yyyyMd").format(new Date()));
		//System.out.println(1900+new Date().getYear());
		//while(true)
			//System.out.println(5+new SimpleDateFormat("yyyyMd").format(new Date()));
/*		String sentence = "一二三四五六七八sadf";
		int senLen=sentence.length();
		if(senLen<=0){
			return;
		}	
		for(int end = senLen;end>0;end--){
			for(int start = end-1;end-start<=5 && start>=0;start--){
				System.out.println(sentence.substring(start,end));
			}
		}*/
		
/*		BufferedReader br = new BufferedReader(new FileReader(new File("E:/test/result26170388875364.txt")));
		String s = br.readLine();
		while(s!=null){
			System.out.println(s);
			s=br.readLine();
		}*/
		
/*		String sentence = "一二三四五六七八";
		int MAXLEN=5;
		int senLen=sentence.length();
		if(senLen<=0){
			return;
		}
		for(int start=0,end;start<senLen;start++){
			end=start+MAXLEN;
			System.out.println(start+"+"+end+"+"+senLen);
			System.out.println(sentence.substring(start,end<senLen?end:senLen));
		}*/
		
		/*String sentence = "一二三四五六七八";
		int MAXLEN=5;
		int senLen=sentence.length();
		if(senLen<=0){
			return;
		}
		for(int end=senLen,start;end>0;end--){
			start=end-MAXLEN;
			System.out.println(sentence.substring(start<0?0:start,end));
		}*/
		
/*		String srcStr = "黑化肥发灰，灰化肥发黑。黑化肥发灰会挥发，灰化肥挥发会发黑";
		String[] tmp = srcStr.split("[^\u4E00-\u9FA5]+");
		ExtSort extSort = new ExtSort("e:/test",ComparatorFactory.getPreComparator());
		ExtSort extSort2 = new ExtSort("e:/test",ComparatorFactory.getPostComparator());
		for(String s:tmp){
			extSort.addSentenceAsPreWord(s, 6);
			extSort2.addSentenceAsPostWord(s, 6);
		}
			extSort.finished();
			extSort2.finished();
		*/
		
	/*	char _character = 'a';
		StringBuffer inputString = new StringBuffer("你好世界！");
		
		_character = inputString.charAt(0);
		System.out.println(_character);
		inputString.deleteCharAt(0);
		System.out.println(inputString);
		if(inputString.length()>0){
			_charNodeMap.put(inputString.charAt(0), new CharNode(inputString,HASHSIZE>>2));
		}*/
	
/*		HashMap<Character,CharNode> map = new HashMap<Character,CharNode>();
		StringBuffer sb = new StringBuffer("世界你好！");
		StringBuffer sb2 = new StringBuffer("世界你好！");
		map.put(sb.charAt(0), new CharNode(sb,16));
		CharNode cn = map.get(sb2.charAt(0));
		cn._character = '1';
		CharNode cn2 = map.get(sb2.charAt(0));
		System.out.println(cn2.getCharacter());*/
		
		/*Analyzer ana = new Analyzer4LDOF(new File("E:\\test\\result27758121415474.txt"),"E:\\test\\result27758121415474.txt");
		
		ana.execute();*/
		
		
		
/*		String xmlPath = "F:\\my projects\\java\\corpus\\data\\NLPIR_Weibo_Data.xml";
		//String xmlPath = "F:\\my projects\\java\\corpus\\data\\test.xml";
		long starttime = System.currentTimeMillis();
		System.out.println("start tasking!");
		try {
			SAXReader handler = new SAXReader();
			SAXParserFactory saxparserfactory = SAXParserFactory.newInstance();
			SAXParser saxparser = saxparserfactory.newSAXParser();
			saxparser.parse(new File(xmlPath), handler);
			long endtime = System.currentTimeMillis();
			System.out.println("all tasks spends "+(endtime-starttime)+"ms!!");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}*/
		
		
		
/*		System.out.println("start analyzing...");
		String savePath1 = "e:\\test\\preAnalyzed"+System.nanoTime()+".txt";
		WordWriter ww = new WordWriter4DOCAndRDOF(savePath1);
		CharTree ct = new CharTree(ww);
		File f = new File("e:\\test\\sortResult18542112985609.txt");
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
		System.out.println("total count:"+ct.getTotalCount()+"\t");
		System.out.println("Analyzing1 spends "+(end - start)+"ms");
		System.out.println("Analyzed1 data was saved in \""+savePath1+"\"");
		ww=null;
		ct=null;
		System.gc();
		
		String savePath2 = "e:\\test\\postAnalyzed"+System.nanoTime()+".txt";
		WordWriter ww2 = new WordWriter4DOCAndLDOF(savePath2);
		CharTree ct2 = new CharTree(ww2);
		File f2 = new File("e:\\test\\sortResult18602814229142.txt");
		long start2 = System.currentTimeMillis();
		BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(f2)));
		String tmp2 = br2.readLine();
		while(tmp2!=null){
			ct2.addWords(new StringBuffer(tmp2));
			tmp2 = br2.readLine();
		}
		long end2 = System.currentTimeMillis();
		ct2.close();
		System.out.println("total count:"+ct2.getTotalCount()+"\t");
		System.out.println("Analyzing2 spends "+(end2 - start2)+"ms");
		System.out.println("Analyzed2 data was saved in \""+savePath2+"\"");*/
		
		
	/*	String s = "http://news.sina.com.cn/society/' + data[i].url + '";
		SinaLinkFilter slf = new SinaLinkFilter();
	    if(!s.matches("^.+[/s ].?")){
	    	System.out.println(true);
	    }else{
	    	System.out.println(false);
	    }
		*/
/*		
		Session session = null;
		Transaction transaction = null;
		
		session = HibernateUtil.currentSession();
		transaction = session.beginTransaction();
		
		News news1 =new News();
		news1.setTitle("");
		news1.setSource("1");
		news1.setMediaid("");
		news1.setDate(new Date(2013,03,23));
		news1.setContent("");

		News news2 =new News();
		news2.setTitle("");
		news2.setSource("http://news.sina.com.cn/s/2013-03-23/021926615743.shtml");
		news2.setMediaid("");
		news2.setDate(new Date(2013,03,23));
		news2.setContent("");
		
		News news3 =new News();
		news3.setTitle("");
		news3.setSource("3");
		news3.setMediaid("");
		news3.setDate(new Date(2013,03,23));
		news3.setContent("");
		
		
		try{
			session.save(news1);
			transaction.commit();
			session.save(news2);
		}catch(HibernateException e){
			HibernateUtil.closeSession();
			session = HibernateUtil.currentSession();
			transaction = session.beginTransaction();
		}finally{
			session.save(news3);
			transaction.commit();
			session = HibernateUtil.currentSession();
			transaction = session.beginTransaction();
		}*/
		
		
	/*	Calendar calendar = Calendar.getInstance();
		calendar.set(2013, 2, 28);
		
		Session session = HibernateUtil.currentSession();
		Transaction transaction = session.beginTransaction();
		
		Query query = session.createQuery("from NewsCounter nc where nc.date=:date and nc.from=:from")
				.setDate("date",calendar.getTime())
				.setString("from","凤凰网");
		
		NewsCounter newsCounter = (NewsCounter)query.uniqueResult();
		
		if(newsCounter==null){
			newsCounter = new NewsCounter();
			newsCounter.setFrom("凤凰网");
			newsCounter.setDate(calendar.getTime());
			newsCounter.setCount(0);
			session.save(newsCounter);
			transaction.commit();
			HibernateUtil.closeSession();
		}
		
		session = HibernateUtil.currentSession();
		transaction = session.beginTransaction();
		session.update(newsCounter);
		transaction.commit();
		HibernateUtil.closeSession();
		System.out.println("get the NewsCounter:\n"+newsCounter.toString());*/
		

/*		Calendar calendar = Calendar.getInstance();
		calendar.set(2013, 2, 28, 0, 0, 0);
		System.out.println(calendar.getTime());*/
	
		String SAVE_PATH = "E:/排序";
		NwordsCounter nwc = new NwordsCounter();
		nwc.setId(1);
		nwc.setNlettersCount(590672);
		
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		String savePath2 = SAVE_PATH+"/postAnalyzed"+System.nanoTime()+".txt";
		WordWriter ww2 = new WordWriter4DOCAndLDOF(savePath2,nwc);
		CharTree ct2 = new CharTree(ww2);
		File f2 = new File("E:/排序/sortResult39749260229201.txt");
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
		
	}
	
}
