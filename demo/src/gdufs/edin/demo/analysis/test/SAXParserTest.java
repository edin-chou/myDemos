package gdufs.edin.demo.analysis.test;

import gdufs.edin.demo.analysis.sort.SAXReader;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class SAXParserTest {
	
	
	public static void main(String[] args){
		String xmlPath = "F:\\my projects\\java\\corpus\\data\\NLPIR_Weibo_Data_mini.xml";
		//String xmlPath = "F:\\my projects\\java\\corpus\\data\\test.xml";
		long starttime = System.currentTimeMillis();
		System.out.println("start!");
		try {
			SAXReader handler = new SAXReader();
			SAXParserFactory saxparserfactory = SAXParserFactory.newInstance();
			SAXParser saxparser = saxparserfactory.newSAXParser();
			saxparser.parse(new File(xmlPath), handler);
			long endtime = System.currentTimeMillis();
			System.out.println("spends "+(endtime-starttime)+"ms!!");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

}
