package com.edu.gdufs.edin.demo.analysis;

import java.util.Comparator;
import java.util.Map.Entry;

public class ComparatorImp implements Comparator<Entry<String,Word>> {

	@Override
	public int compare(Entry<String,Word> o1, Entry<String,Word> o2) {
		return o2.getValue()._count-o1.getValue()._count;
	}
	
	

}
