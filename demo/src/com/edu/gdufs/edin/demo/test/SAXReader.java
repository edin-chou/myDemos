package com.edu.gdufs.edin.demo.test;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Comparator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.edu.gdufs.edin.demo.analysis.CharTree;
import com.edu.gdufs.edin.demo.analysis.WordWriter;
import com.edu.gdufs.edin.demo.analysis.WordWriter4DOCAndLDOF;
import com.edu.gdufs.edin.demo.analysis.WordWriter4DOCAndRDOF;
import com.edu.gdufs.edin.demo.analysis.sort.ExtSort;



public class SAXReader extends DefaultHandler {
	
	//记录当前节点值
	private String currentValue;
	//记录当前及节点
	private String currentElement;
	//外排序对象
	private ExtSort extSort;
	private ExtSort extSort2;
	//开始时间
	private long startTime;
	
	
	@Override
    public void characters(char[] ch, int start, int length) 
            throws SAXException { 
		for(int i = 0,n=ch.length;i<n;i++){
			if((int)ch[i]==65535){
				ch[i]=(char) 78;
			}
		}
		//System.out.println(new String(ch,start,length));
        currentValue += new String(ch,start,length);
    }
	
	@Override
	public void startDocument()throws SAXException{
		startTime = System.currentTimeMillis();
		extSort = new ExtSort("e:/test");
	}
	
	@Override
	public void startElement(String uri, String localName, String name, 
            Attributes attributes) throws SAXException {
		currentValue = "";
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		if(qName.equalsIgnoreCase("id")){
			//System.out.println("id:"+currentValue);
		}else if(qName.equalsIgnoreCase("article")){
			String[] tmp = currentValue.split("[^\u4E00-\u9FA5]+");
			for(String s:tmp){
				if(s.trim().equals(""))continue;
				//System.out.println(s);
				try {
					extSort.addWord(s);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//extSort2.addSentenceAsPreWord(s, 5);
				//System.out.println(s);
			}
			tmp=null;
		}
	}
	
	@Override
	public void endDocument()throws SAXException{
		super.endDocument();
		try {
			String[] sortResult = extSort.finished();
			System.out.println("start analyzing...");
			String savePath1 = "e:\\test\\preAnalyzed"+System.nanoTime()+".txt";
			WordWriter ww = new WordWriter4DOCAndRDOF(savePath1);
			CharTree ct = new CharTree(ww);
			File f = new File(sortResult[0]);
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
			File f2 = new File(sortResult[1]);
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
