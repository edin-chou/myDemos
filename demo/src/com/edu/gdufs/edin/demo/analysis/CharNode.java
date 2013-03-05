package com.edu.gdufs.edin.demo.analysis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class CharNode {
	
	protected final int MINHASHSIZE = 4;
	protected Character _character;
	protected int _count;
	protected int _childHashSize;
	protected Map<Character,CharNode> _charNodeMap;
	
	public Character getCharacter() {
		return _character;
	}
	public void setCharacter(Character _character) {
		this._character = _character;
	}
	public int getCount() {
		return _count;
	}
	public void setCount(int _count) {
		this._count = _count;
	}
	public int getChildHashSize() {
		return _childHashSize;
	}
	public void setChildHashSize(int _childHashSize) {
		this._childHashSize = _childHashSize;
	}
	public Map<Character, CharNode> getCharNodeMap() {
		return _charNodeMap;
	}
	public void setCharNodeMap(Map<Character, CharNode> _charNodeMap) {
		this._charNodeMap = _charNodeMap;
	}
	
	public int getMINHASHSIZE() {
		return MINHASHSIZE;
	}

	public CharNode(int hashSize){
		_count = 0;
		_charNodeMap = new HashMap<Character,CharNode>(hashSize);
		_childHashSize = hashSize>>4;
	}
	
	
	
	
	
	
	
	
	
	public CharNode(StringBuffer inputString,int hashSize){
		_character = inputString.charAt(0);
		_charNodeMap = new HashMap<Character,CharNode>(hashSize);
		_count = 1;
		int tmp = hashSize>>2;
		_childHashSize = tmp>MINHASHSIZE?tmp:MINHASHSIZE;
		inputString.deleteCharAt(0);
		if(inputString.length()>0){
			_charNodeMap.put(inputString.charAt(0), new CharNode(inputString,_childHashSize));
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
				_charNodeMap.put(inputString.charAt(0),new CharNode(inputString,_childHashSize));
			}
		}
		return true;	
	}

	
}
