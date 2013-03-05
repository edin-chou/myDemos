package com.edu.gdufs.edin.demo.analysis;

public class CharTree {
	
	private final int INITHASHSIZE = 512;
	private CharNode _root;

	public CharTree(){
		_root = new CharNode(INITHASHSIZE);
	}
	
	//增加一个词到词库
	public boolean addWords(StringBuffer inputString) throws Exception{
		if(_root._character!=null){
			
			
		}else{
			_root._character = inputString.charAt(0);
			inputString.deleteCharAt(0);
			_root._count++;
			if(inputString.length()>0){
				_root._charNodeMap.put(inputString.charAt(0), new CharNode(inputString,_root._childHashSize));
			}
			return true;
		}
		
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
	//用于递归调用
	public boolean addNodes(CharNode charNode,StringBuffer inputString){
		return false;
	}

}
