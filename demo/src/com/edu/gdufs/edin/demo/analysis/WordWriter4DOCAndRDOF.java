package com.edu.gdufs.edin.demo.analysis;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.edu.gdufs.edin.demo.model.HibernateUtil;

public class WordWriter4DOCAndRDOF extends WordWriter {

	public WordWriter4DOCAndRDOF(WordsAnalyzingCounter wordsAnalyzingCounter) throws IOException {
		super(wordsAnalyzingCounter);
	}

	@Override
	public void write(CharNode root) throws IOException {
		double entropy = getEntropy(root);
		if(entropy>=ENTROPY_THRESHOLD&&root._count>=COUNT_THRESHOLD){
			_wordsAnalyzingCounter._lettersMap.put(root.getCharacter().toString(), root.getCount());
			_wordsAnalyzingCounter._wordsCount+=root.getCount();
			logger.warn("put letter:"+root.getCharacter().toString());
		}
		Set<Entry<Character, CharNode>> se = root._charNodeMap.entrySet();
		for(Iterator i = se.iterator();i.hasNext();){
			Entry<Character,CharNode> e = (Entry<Character,CharNode>)i.next();
			writeCountRecursion(e.getValue(),root._character.toString());
		}
	}
	
	private void writeCountRecursion(CharNode charNode,String prefixString) throws IOException{
		String tmp = prefixString+charNode.getCharacter();
		double entropy = getEntropy(charNode);
		if(entropy>=ENTROPY_THRESHOLD
				&&charNode._count>=COUNT_THRESHOLD
				&&!_wordsAnalyzingCounter._owordSet.contains(tmp)){
			Word word = new Word();
			word._word=tmp;
			word._count=charNode.getCount();
			word._rentropy=getEntropy(charNode);
			_wordsAnalyzingCounter._wordsMap.put(tmp, word);
			logger.warn("put word:"+tmp+"\tcount:"+word._count+"\trentropy:"+word._rentropy);
		}
		Set<Entry<Character, CharNode>> se = charNode._charNodeMap.entrySet();
		for(Iterator i = se.iterator();i.hasNext();){
			Entry<Character,CharNode> e = (Entry<Character,CharNode>)i.next();
			writeCountRecursion(e.getValue(),tmp);
		}
	}

}
