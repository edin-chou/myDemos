package com.edu.gdufs.edin.demo.analysis;

import java.util.Map;

public class Node {
	
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

}
