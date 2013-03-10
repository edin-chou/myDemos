package com.edu.gdufs.edin.demo.analysis;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class WordWriter4DOCAndLDOF extends WordWriter {

	public WordWriter4DOCAndLDOF(String outputFileName) throws IOException {
		super(outputFileName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void write(CharNode root) throws IOException {
		double entropy = getEntropy(root);
		//System.out.println(root._character+"\t"+entropy+"\t"+ root.getCount()+"\t");
		if(entropy>=ENTROPY_THRESHOLD&&root._count>=COUNT_THRESHOLD){
			_writer.write(root._character+"\t"+entropy+"\t"+root._count+"\n");
		}
		Set<Entry<Character, CharNode>> se = root._charNodeMap.entrySet();
		for(Iterator i = se.iterator();i.hasNext();){
			Entry<Character,CharNode> e = (Entry<Character,CharNode>)i.next();
			writeCountRecursion(e.getValue(),root._character.toString());
		}
		_writer.flush();
	}
	
	private void writeCountRecursion(CharNode charNode,String prefixString) throws IOException{
		String tmp = charNode.getCharacter()+prefixString;
		double entropy = getEntropy(charNode);
		//System.out.println(tmp+"\t"+tRate+"\t"+entropy+"\t"+ charNode.getCount()+"\t"+_totalCount+"\t"+tmp.length()+"\t"+Math.pow(_totalCount, tmp.length()));
		if(entropy>=ENTROPY_THRESHOLD&&charNode._count>=COUNT_THRESHOLD){
			_writer.write(tmp+"\t"+entropy+"\t"+charNode._count+"\n");
		}
		Set<Entry<Character, CharNode>> se = charNode._charNodeMap.entrySet();
		for(Iterator i = se.iterator();i.hasNext();){
			Entry<Character,CharNode> e = (Entry<Character,CharNode>)i.next();
			writeCountRecursion(e.getValue(),tmp);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		_writer.flush();
		_writer.close();
		super.finalize();
	}
	
	
}
