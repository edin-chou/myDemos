package com.edu.gdufs.edin.demo.analysis.sort;


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



public class SAXReader extends DefaultHandler {
	
	//记录当前节点值
	private String currentValue;
	//记录当前及节点
	private String currentElement;
	//外排序对象
	private ExtSort extSort;
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
		extSort = new ExtSort("e:/test",ComparatorFactory.getPreComparator());
		//extSort2 = new ExtSort("e:/test",ComparatorFactory.getPreComparator());
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
					extSort.addSentenceAsPreWord(s, 5);
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
			String sortResult = extSort.finished();
			System.out.println("Sorting spends "+(System.currentTimeMillis()-startTime)+"ms");
			System.out.println("Sorted data was saved in \""+sortResult+"\"");
			//System.out.println(extSort2.finished());
			
			System.out.println("start analyzing...");
			String outputFileName = "e:\\test\\analyzed"+System.nanoTime()+".txt";
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputFileName))));
			CharTree ct = new CharTree(bw);
			File f = new File(sortResult);
			long start = System.currentTimeMillis();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String tmp = br.readLine();
			while(tmp!=null){
				ct.addWords(new StringBuffer(tmp));
				tmp = br.readLine();
			}
			long end = System.currentTimeMillis();
			ct.close();
			System.out.println("total count:"+ct.getTotalCount()+"\t");
			System.out.println("Analyzing spends "+(end - start)+"ms");
			System.out.println("Analyzed data was saved in \""+outputFileName+"\"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
