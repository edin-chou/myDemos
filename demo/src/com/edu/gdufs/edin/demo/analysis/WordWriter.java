package com.edu.gdufs.edin.demo.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Map.Entry;

import com.edu.gdufs.edin.demo.model.Nletter;
import com.edu.gdufs.edin.demo.model.NwordsCounter;

public class WordWriter {
	
	protected final double ENTROPY_THRESHOLD = 0.8;
	protected final int COUNT_THRESHOLD = 3;
	protected BufferedWriter _writer;
	protected NwordsCounter _nwordsCounter;
	
	public WordWriter(String outputFileName,NwordsCounter nwordsCounter) throws IOException{
		File f = new File(outputFileName);
		if(!f.exists()){
			f.createNewFile();
		}
		_nwordsCounter = nwordsCounter;
		_writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputFileName))));
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
