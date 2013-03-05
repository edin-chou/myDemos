package com.edu.gdufs.edin.demo.analysis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class HeadCharNode {
	
	private final int HASHSIZE = 512;
	private Character _character;
	private int _count;
	private HashMap<Character,CharNode> _charNodeMap;

	public HeadCharNode(StringBuffer inputString){
		_character = inputString.charAt(0);
		_charNodeMap = new HashMap<Character,CharNode>(HASHSIZE);
		_count = 1;
		inputString.deleteCharAt(0);
		if(inputString.length()>0){
			_charNodeMap.put(inputString.charAt(0), new CharNode(inputString,HASHSIZE>>2));
		}
	}
	public HeadCharNode(){
		_charNodeMap = new HashMap<Character,CharNode>(HASHSIZE);
		_count = 0;
	}
	
	//新增一个词，第一个字必须相同，否则返回false
	public boolean addWords(StringBuffer inputString) throws Exception{
		if(_character!=null &&_character.equals(inputString.charAt(0))){
			inputString.deleteCharAt(0);
			_count++;
			if(inputString.length()>0){
				CharNode cn = _charNodeMap.get(inputString.charAt(0));
				if(cn!=null){
					cn.addWords(inputString);
				}else{
					_charNodeMap.put(inputString.charAt(0),new CharNode(inputString,HASHSIZE>>2));
				}
			}
			return true;
		}else if(_character==null){
			_character = inputString.charAt(0);
			inputString.deleteCharAt(0);
			_count++;
			if(inputString.length()>0){
				CharNode cn = _charNodeMap.get(inputString.charAt(0));
				if(cn!=null){
					cn.addWords(inputString);
				}else{
					_charNodeMap.put(inputString.charAt(0),new CharNode(inputString,HASHSIZE>>2));
				}
			}
			return true;
					
		}else{
			return false;
			
		}
	}
	
	public void writeCount(BufferedWriter Writer) throws IOException{
		Writer.write(_character+"\t"+_count+"\n");
		Set<Entry<Character, CharNode>> se = _charNodeMap.entrySet();
		for(Iterator i = se.iterator();i.hasNext();){
			Entry<Character,CharNode> e = (Entry<Character,CharNode>)i.next();
			writeCountRecursion(e.getValue(),Writer,_character.toString());
		}
		Writer.flush();
	}
	
	private void writeCountRecursion(CharNode charNode,BufferedWriter Writer,String prefixString) throws IOException{
		String tmp = prefixString+charNode.getCharacter();
		Writer.write(tmp+"\t"+charNode.getCount()+"\n");
		Set<Entry<Character, CharNode>> se = charNode.getCharNodeMap().entrySet();
		for(Iterator i = se.iterator();i.hasNext();){
			Entry<Character,CharNode> e = (Entry<Character,CharNode>)i.next();
			writeCountRecursion(e.getValue(),Writer,tmp);
		}
	}
	
	
	

}
