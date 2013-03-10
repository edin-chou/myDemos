package com.edu.gdufs.edin.demo.analysis;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class CharTree {
	
	private final int INITHASHSIZE = 512;
	private CharNode _root;
	private WordWriter _wordWriter;
	private long _totalCount;
	
	
	public CharTree(WordWriter ww){
		_root = new CharNode(INITHASHSIZE);
		_totalCount=0;
		_wordWriter = ww;
	}

	public long getTotalCount() {
		return _totalCount;
	}
	
	public void close() throws IOException{
		write(_root,_wordWriter);
		_root = null;
		_wordWriter = null;
		_totalCount =0;
		System.gc();
	}
	
	
	//增加一个词到词库,若根词变换则返回false,不变则返回true
	public boolean addWords(StringBuffer inputString) throws Exception{
		boolean returnValue = false;
		inputString.length();
		/*if(inputString.length()>3&&inputString.substring(0, 4).equals("不是昨天")){
			System.out.println(1);
		}*/
		if(_root._character!=null){
			if(_root._character.equals(inputString.charAt(0))){
				inputString.deleteCharAt(0);
				_root._count++;
				_totalCount++;
				if(inputString.length()>0){
					CharNode cn = _root._charNodeMap.get(inputString.charAt(0));
					if(cn!=null){
						addNodes(cn, inputString);
						returnValue = true;
					}else{
						_root._charNodeMap.put(inputString.charAt(0),new CharNode(inputString,INITHASHSIZE>>4));
						returnValue = true;
					}
				}
			}else{
				if(_wordWriter==null){
					throw new Exception("please set the WordWriter!!");
				}else{
					write(_root,_wordWriter);
					_root = new CharNode(INITHASHSIZE);
					_root._character=inputString.charAt(0);
					addWords(inputString);
					returnValue = false;
				}
			}
		}else{
			_root._character = inputString.charAt(0);
			inputString.deleteCharAt(0);
			_root._count++;
			_totalCount++;
			if(inputString.length()>0){
				_root._charNodeMap.put(inputString.charAt(0), new CharNode(inputString,_root._childHashSize));
			}
			returnValue = false;
		}
		return returnValue;
	}
	
	//用于递归调用
	public boolean addNodes(CharNode charNode,StringBuffer inputString){
		boolean returnValue = false;
		inputString.deleteCharAt(0);
		charNode._count++;
		if(inputString.length()>0){
			CharNode cn = charNode._charNodeMap.get(inputString.charAt(0));
			if(cn!=null){
				addNodes(cn,inputString);
				returnValue = true; 
			}else{
				charNode._charNodeMap.put(inputString.charAt(0), new CharNode(inputString,charNode._childHashSize));
				returnValue = false;
			}
		}
		return returnValue;
	}
	
	public void write(CharNode root,WordWriter wordWriter) throws IOException{
		wordWriter.write(root);
	}
	
/*	public void writeCount(CharNode root,BufferedWriter Writer) throws IOException{
		double entropy = getEntropy(root);
		//System.out.println(root._character+"\t"+entropy+"\t"+ root.getCount()+"\t");
		if(entropy>=ENTROPY_THRESHOLD&&root._count>=COUNT_THRESHOLD){
			Writer.write(root._character+"\t"+entropy+"\t"+root._count+"\n");
		}
		Set<Entry<Character, CharNode>> se = root._charNodeMap.entrySet();
		for(Iterator i = se.iterator();i.hasNext();){
			Entry<Character,CharNode> e = (Entry<Character,CharNode>)i.next();
			writeCountRecursion(e.getValue(),Writer,root._character.toString());
		}
		Writer.flush();
	}
	
	private void writeCountRecursion(CharNode charNode,BufferedWriter Writer,String prefixString) throws IOException{
		String tmp = prefixString+charNode.getCharacter();
		double entropy = getEntropy(charNode);
		//System.out.println(tmp+"\t"+tRate+"\t"+entropy+"\t"+ charNode.getCount()+"\t"+_totalCount+"\t"+tmp.length()+"\t"+Math.pow(_totalCount, tmp.length()));
		if(entropy>=ENTROPY_THRESHOLD&&charNode._count>=COUNT_THRESHOLD){
			Writer.write(tmp+"\t"+entropy+"\t"+charNode._count+"\n");
		}
		Set<Entry<Character, CharNode>> se = charNode._charNodeMap.entrySet();
		for(Iterator i = se.iterator();i.hasNext();){
			Entry<Character,CharNode> e = (Entry<Character,CharNode>)i.next();
			writeCountRecursion(e.getValue(),Writer,tmp);
		}
	}
	
	private double getEntropy(CharNode charNode){
		double entropy = .0;
		for(Iterator i = charNode._charNodeMap.entrySet().iterator();i.hasNext();){
			Entry<Character,CharNode> e = (Entry<Character,CharNode>) i.next();
			double rate = e.getValue()._count/(double)charNode._count;
			entropy -= rate*Math.log(rate)/Math.log(2);
		}
		return entropy;
	}*/
	

}
