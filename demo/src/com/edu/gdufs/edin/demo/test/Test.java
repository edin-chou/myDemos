package com.edu.gdufs.edin.demo.test;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.edu.gdufs.edin.demo.analysis.CharTree;
import com.edu.gdufs.edin.demo.analysis.WordWriter;
import com.edu.gdufs.edin.demo.analysis.WordWriter4DOCAndLDOF;
import com.edu.gdufs.edin.demo.analysis.WordWriter4DOCAndRDOF;

public class Test{


	public static void main(String a[]) throws Exception{
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
		
		
		
		System.out.println("start analyzing...");
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
		System.out.println("Analyzed2 data was saved in \""+savePath2+"\"");
		
		
	}
}
