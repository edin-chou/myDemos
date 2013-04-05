package com.edu.gdufs.edin.demo.analysis;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.edu.gdufs.edin.demo.model.Pword;

public class WordWriter4DOCAndLDOF extends WordWriter {

	public WordWriter4DOCAndLDOF(WordsAnalyzingCounter wordsAnalyzingCounter) throws IOException {
		super(wordsAnalyzingCounter);
	}

	@Override
	public void write(CharNode root) throws IOException {
		double entropy = getEntropy(root);
		//System.out.println(root._character+"\t"+entropy+"\t"+ root.getCount()+"\t");
		if(entropy>=ENTROPY_THRESHOLD&&root._count>=COUNT_THRESHOLD){
		}
		Set<Entry<Character, CharNode>> se = root._charNodeMap.entrySet();
		for(Iterator i = se.iterator();i.hasNext();){
			Entry<Character,CharNode> e = (Entry<Character,CharNode>)i.next();
			writeCountRecursion(e.getValue(),root._character.toString());
		}
	}
	
	private void writeCountRecursion(CharNode charNode,String prefixString) throws IOException{
		String tmp = charNode.getCharacter()+prefixString;
		double entropy = getEntropy(charNode);
		//System.out.println(tmp+"\t"+tRate+"\t"+entropy+"\t"+ charNode.getCount()+"\t"+_totalCount+"\t"+tmp.length()+"\t"+Math.pow(_totalCount, tmp.length()));
		if(entropy>=ENTROPY_THRESHOLD
				&&charNode._count>=COUNT_THRESHOLD
				&&!_wordsAnalyzingCounter._owordSet.contains(tmp)){
			Word word = _wordsAnalyzingCounter._wordsMap.get(tmp);
			if(word!=null&&((word._mutualinfo=getMutualInformation(word))>MUTUALINFO_THRESHOLD)){
				word._lentropy=getEntropy(charNode);
				//word._mutualinfo=getMutualInformation(word);
				_wordsAnalyzingCounter._wordsMap.put(tmp, word);
				logger.warn("get word:"+tmp
						+"\tcount:"+word._count
						+"\trentropy:"+word._rentropy
						+"\tmutualinfo:"+word._mutualinfo);
			}
		}
		Set<Entry<Character, CharNode>> se = charNode._charNodeMap.entrySet();
		for(Iterator i = se.iterator();i.hasNext();){
			Entry<Character,CharNode> e = (Entry<Character,CharNode>)i.next();
			writeCountRecursion(e.getValue(),tmp);
		}
	}
	
	private Double getMutualInformation(Word word){
		char[] charArr =word._word.toCharArray();
		int para1 = 0;
		int para2 = 0;
		for(int i=0;i<charArr.length-1;i++){
			para1+=Math.log(_wordsAnalyzingCounter._wordsCount)/Math.log(2);
		}
		
		for(int i=0;i<charArr.length;i++){
			Integer letterCount = _wordsAnalyzingCounter._lettersMap.get((new Character(charArr[i])).toString());
			if(letterCount==null){
				return .0;
			}
			para2+=Math.log(letterCount)/Math.log(2);
		}
		double result = ((double)para1+Math.log(word._count)/Math.log(2))-((double)para2);
		return result;
	}
}
