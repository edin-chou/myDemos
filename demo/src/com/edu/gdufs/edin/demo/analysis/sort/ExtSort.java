package com.edu.gdufs.edin.demo.analysis.sort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.gdufs.edin.demo.crawler.Storage;
//外排序工具
public class ExtSort {
	

	final  Logger logger  =  LoggerFactory.getLogger(ExtSort.class );
	//外排序最小文件行数
	private final int SIZE=500000;
	//
	private final int WORD_MAX_LEN=6;
	//排序结果存放路径以及中间处理文件存放路径
	private String _savePath="E:/排序";
	//前缀内存词列表
	private List<String> _preWordList=new ArrayList<String>();
	//后缀内存词列表
	private List<String> _postWordList=new ArrayList<String>();
	//前缀外排序文件列表
	private List<String> _preFileList=new ArrayList<String>();
	//后缀外排序文件列表
	private List<String> _postFileList=new ArrayList<String>();

	//路径初始化，初始化外排序
	public ExtSort(String savePath){
		_savePath=savePath;
	}
	//初始化外排序
	public ExtSort(){
	}

	//往外排序添加一个句子的后缀词的前MAXLEN个
	private void addSentenceAsPostWord(String sentence,int maxWordLen) throws IOException{
		int senLen=sentence.length();
		if(senLen<=0){
			return;
		}
		for(int start=0,end;start<senLen;start++){
			end=start+maxWordLen;
			
			_postWordList.add(sentence.substring(start,end<senLen?end:senLen));
			if(_postWordList.size()>=SIZE){
				_postFileList.add(sortAndSave(_postWordList));
			}
		}
	}	
	

	//往外排序添加一个句子的前缀词的前MAXLEN个
	private void addSentenceAsPreWord(String sentence,int maxWordLen) throws IOException{
		int senLen=sentence.length();
		if(senLen<=0){
			return;
		}
		for(int end=senLen,start;end>0;end--){
			start=end-maxWordLen;
			String tmp = sentence.substring(start<0?0:start,end);
			StringBuffer sb = new StringBuffer();
			for(int i = tmp.length()-1;i>=0;i--){
				sb.append(tmp.charAt(i));
			}

			_preWordList.add(sb.toString());
			if(_preWordList.size()>=SIZE){
				_preFileList.add(sortAndSave(_preWordList));
			}
		}
	}
	
	//往外排序添加一条记录
	public void addWord(String wordString) throws IOException{
		addSentenceAsPostWord(wordString,WORD_MAX_LEN);
		addSentenceAsPreWord(wordString,WORD_MAX_LEN);
	}

	//完成添加后进行归并运算,并返回排序结果存放路径
	public String[] finished() throws IOException{
		String[] tmpStrings = {"",""};
		tmpStrings[0]=postFinished();
		tmpStrings[1]=preFinished();
		return tmpStrings;
	}
	//完成后缀词添加后进行归并运算
	private String postFinished() throws IOException{
		_postFileList.add(sortAndSave(_postWordList));
		System.out.println("merge sorting...please wait！");
		String resultFile = mergeSort(_postFileList);
		System.out.println("merge sort is finished, please check results at "+resultFile);
		return resultFile;
	}
	//完成前缀次添加后进行归并运算
	private String preFinished() throws IOException{
		_preFileList.add(sortAndSave(_preWordList));
		System.out.println("merge sorting...please wait！");
		String resultFile = mergeSort(_preFileList);
		System.out.println("merge sort is finished, please check results at "+resultFile);
		return resultFile;
	}
	
	//对内存记录进行排序，并保存成文件
	private String sortAndSave(List<String> wordList) throws IOException{
		if(wordList.size()>=0){
			Collections.sort(wordList);
			String fileName = _savePath+"/tmp";//"/split"+System.nanoTime()+".txt";
			File file = new File(fileName);
			if(!file.exists()){
				file.mkdirs();
			}
			fileName = fileName+"/split"+System.nanoTime()+".txt";
			file = new File(fileName);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(Iterator i = wordList.iterator();i.hasNext();)
				bw.write((String)i.next()+"\n");
			bw.close();
			wordList.clear();
			//wordList=new ArrayList<String>();
			logger.warn("file \""+fileName+"\" had been generated");
			return fileName;
		}else{
			return "null";
		}
	}
	
	//归并文件
	private String mergeSort(List<String> fileList) throws IOException{
		List<String> tempFileList = new ArrayList<String>();
		if(fileList.size()>1){
			//将文件两两合并
			for(Iterator i = fileList.iterator();i.hasNext();){
				//合并结果输出流
				String resultFileName = _savePath+"/tmp/split"+System.nanoTime()+".txt";
				BufferedWriter bw = new BufferedWriter(new FileWriter(resultFileName));
				tempFileList.add(resultFileName);
				//待合并文件1，并读取第一行的数据
				String name1 = (String)i.next();
				File file1 = new File(name1);
				BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(file1)));
				String word1 = br1.readLine();
				
				//带合并文件2若存在，则合并文件1，2
				if(i.hasNext()){
					//待合并文件2，并读取第一行数据
					String name2 = (String)i.next();
					File file2 = new File(name2);
					BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(file2)));
					String word2 = br2.readLine();
					//文件1,2合并
					while(word1!=null&&word2!=null){
						if(word1.compareTo(word2)<=0){
							bw.write(word1+"\n");
							word1=br1.readLine();
						}else{
							bw.write(word2+"\n");
							word2=br2.readLine();
						}
					}
					while(word2!=null){
						bw.write(word2+"\n");
						word2=br2.readLine();
					}
					br2.close();
					file2.delete();
				}
				
				while(word1!=null){
					bw.write(word1+"\n");
					word1=br1.readLine();
				}
				br1.close();
				file1.delete();
				bw.close();
			}
			return mergeSort(tempFileList);
		}else{
			File file = new File(fileList.get(0));
			String resultFileName = _savePath+"/sortResult"+System.nanoTime()+".txt";
			file.renameTo(new File(resultFileName));
			return resultFileName;
		}
			
	}

}
