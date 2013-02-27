package saxExecutor;

import java.io.IOException;
import java.util.Comparator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import util.ComparatorFactory;
import util.ExtSort;


public class SAXReader extends DefaultHandler {
	
	//记录当前节点值
	private String currentValue;
	//记录当前及节点
	private String currentElement;
	//外排序对象
	private ExtSort extSort;
	private ExtSort extSort2;
	
	
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
		extSort = new ExtSort("e:/test",ComparatorFactory.getPostComparator());
		extSort2 = new ExtSort("e:/test",ComparatorFactory.getPreComparator());
	}
	
	@Override
	public void startElement(String uri, String localName, String name, 
            Attributes attributes) throws SAXException {
		currentValue = "";
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		if(qName.equalsIgnoreCase("id")){
			System.out.println("id:"+currentValue);
		}else if(qName.equalsIgnoreCase("article")){
			try {
				String[] tmp = currentValue.split("[^\u4E00-\u9FA5]+");
				for(String s:tmp){
					if(s.trim().equals(""))continue;
					//System.out.println(s);
					extSort.addSentenceAsPostWord(s, 5);
					extSort2.addSentenceAsPreWord(s, 5);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void endDocument()throws SAXException{
		super.endDocument();
		try {
			System.out.println(extSort.finished());
			System.out.println(extSort2.finished());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
