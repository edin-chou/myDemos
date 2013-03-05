package com.edu.gdufs.edin.demo.analysis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class CharNode {
	
	private final int MINHASHSIZE = 4;
	private Character _character;
	private int _count;
	private int _hashSize;
	
	public Character getCharacter() {
		return _character;
	}

	public int getCount() {
		return _count;
	}

	public Map<Character, CharNode> getCharNodeMap() {
		return _charNodeMap;
	}

	private Map<Character,CharNode> _charNodeMap;

	public CharNode(StringBuffer inputString,int hashSize){
		_character = inputString.charAt(0);
		_charNodeMap = new HashMap<Character,CharNode>(_hashSize);
		_count = 1;
		int tmp = hashSize>>2;
		_hashSize = tmp>MINHASHSIZE?tmp:MINHASHSIZE;
		inputString.deleteCharAt(0);
		if(inputString.length()>0){
			_charNodeMap.put(inputString.charAt(0), new CharNode(inputString,_hashSize));
		}
	}
	
	public boolean addWords(StringBuffer inputString) throws Exception{
		if(!_character.equals(inputString.charAt(0))){
				throw new Exception("Wrong inputString");
		}
		inputString.deleteCharAt(0);
		_count++;
		if(inputString.length()>0){
			CharNode cn = _charNodeMap.get(inputString.charAt(0));
			if(cn!=null){
				cn.addWords(inputString);
			}else{
				_charNodeMap.put(inputString.charAt(0),new CharNode(inputString,_hashSize));
			}
		}
		return true;	
	}

	
}
