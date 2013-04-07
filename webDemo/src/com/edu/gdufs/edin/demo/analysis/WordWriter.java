package com.edu.gdufs.edin.demo.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.gdufs.edin.demo.model.Nletter;
import com.edu.gdufs.edin.demo.model.NwordsCounter;

public class WordWriter {

	final  Logger logger  =  LoggerFactory.getLogger(WordWriter.class);
	
	protected final double ENTROPY_THRESHOLD = 1.5;
	protected final int COUNT_THRESHOLD = 3;
	protected final int MUTUALINFO_THRESHOLD = 0;
/*	protected final int DIF_DEFAULT = 3;*/
	protected WordsAnalyzingCounter _wordsAnalyzingCounter;
	
	public WordWriter(WordsAnalyzingCounter wordsAnalyzingCounter) throws IOException{
		_wordsAnalyzingCounter = wordsAnalyzingCounter;
	}

	public void write(CharNode root) throws IOException{}
	
	protected double getEntropy(CharNode charNode){
		double entropy = .0;
		for(Iterator i = charNode._charNodeMap.entrySet().iterator();i.hasNext();){
			Entry<Character,CharNode> e = (Entry<Character,CharNode>) i.next();
			double rate = e.getValue()._count/(double)charNode._count;
			entropy -= rate*Math.log(rate)/Math.log(2);
		}
		return entropy;
	}
}
