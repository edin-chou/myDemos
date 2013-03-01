package gdufs.edin.demo.analysis.sort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
//外排序工具
public class ExtSort {
	//外排序最小文件行数
	private final int SIZE=50000;
	//排序结果存放路径以及中间处理文件存放路径
	private String _savePath="E:/排序";
	//内存词列表
	private List<String> _wordList=new ArrayList<String>();
	//外排序文件列表
	private List<String> _fileList=new ArrayList<String>();
	//比较器
	private Comparator _comparator = null;
	
	//路径初始化，初始化外排序
	public ExtSort(String savePath,Comparator comparator){
		_savePath=savePath;
		_comparator=comparator;
	}
	//路径初始化，初始化外排序
	public ExtSort(String savePath){
		_savePath=savePath;
	}
	
	//往外排序添加一条记录
	public void addWord(String wordString) throws IOException{
		_wordList.add(wordString);
		if(_wordList.size()>=SIZE){
			_fileList.add(sortAndSave());
		}
	}

/*	//往外排序添加一个句子的所有前缀词
	public void addSentenceAsPreWord(String sentence,int maxWordLen) throws IOException{
		int senLen=sentence.length();
		if(senLen<=0){
			return;
		}	
		for(int start = 0;start<senLen;start++){
			for(int end = start+1;end-start<=maxWordLen && end<=senLen;end++){
				addWord(sentence.substring(start,end));
			}
		}
	}	
	

	//往外排序添加一个句子的所有后缀词
	public void addSentenceAsPostWord(String sentence,int maxWordLen) throws IOException{
		int senLen=sentence.length();
		if(senLen<=0){
			return;
		}	
		for(int end = senLen;end>0;end--){
			for(int start = end-1;end-start<=maxWordLen && start>=0;start--){
				addWord(sentence.substring(start,end));
			}
		}
	}*/

	//往外排序添加一个句子的后缀词的前MAXLEN个
	public void addSentenceAsPreWord(String sentence,int maxWordLen) throws IOException{
		int senLen=sentence.length();
		if(senLen<=0){
			return;
		}
		for(int start=0,end;start<senLen;start++){
			end=start+maxWordLen;
			addWord(sentence.substring(start,end<senLen?end:senLen));
		}
	}	
	

	//往外排序添加一个句子的前缀词的前MAXLEN个
	public void addSentenceAsPostWord(String sentence,int maxWordLen) throws IOException{
		int senLen=sentence.length();
		if(senLen<=0){
			return;
		}
		for(int end=senLen,start;end>0;end--){
			start=end-maxWordLen;
			addWord(sentence.substring(start<0?0:start,end));
		}
	}
	
	//完成添加后进行归并运算
	public String finished() throws IOException{
		_fileList.add(sortAndSave());
		System.out.println("merge sorting...please wait！");
		String resultFile = mergeSort(_fileList);
		System.out.println("merge sort is finished, please check results at "+resultFile);
		return resultFile;
	}
	
	//对内存记录进行排序，并保存成文件
	private String sortAndSave() throws IOException{
		if(_wordList.size()>=0){
			if(_comparator==null)
				Collections.sort(_wordList);
			else
				Collections.sort(_wordList,_comparator);
			String fileName = _savePath+"/tmp";//"/split"+System.nanoTime()+".txt";
			File file = new File(fileName);
			if(!file.exists()){
				file.mkdirs();
			}
			fileName = fileName+"/split"+System.nanoTime()+".txt";
			file = new File(fileName);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(Iterator i = _wordList.iterator();i.hasNext();)
				bw.write((String)i.next()+"\n");
			bw.close();
			_wordList.clear();
			//wordList=new ArrayList<String>();
			System.out.println("file \""+fileName+"\" had been generated");
			return fileName;
		}else{
			return null;
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
				BufferedReader br1 = new BufferedReader(new FileReader(file1));
				String word1 = br1.readLine();
				
				//带合并文件2若存在，则合并文件1，2
				if(i.hasNext()){
					//待合并文件2，并读取第一行数据
					String name2 = (String)i.next();
					File file2 = new File(name2);
					BufferedReader br2 = new BufferedReader(new FileReader(file2));
					String word2 = br2.readLine();
					//文件1,2合并
					while(word1!=null&&word2!=null){
						if(_comparator.compare(word1, word2)>=0){
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
			String resultFileName = _savePath+"/result"+System.nanoTime()+".txt";
			file.renameTo(new File(resultFileName));
			return resultFileName;
		}
			
	}


	/**
	 * @param args
	 * @throws IOException 
	 */
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/*ExtSort extSort=new ExtSort("e:/test");
		extSort.addWord("第一个词");
		extSort.finished();*/
		
/*		List<String> ls = new ArrayList<String>();
		ls.add("第");
		ls.add("一");
		ls.add("个");
		ls.add("词");
		Collections.sort(ls);
		for(Iterator i = ls.iterator();i.hasNext();)
			System.out.println((String)i.next());
		System.out.println("一".compareTo("个"));
		System.out.println("个".compareTo("第"));
		System.out.println("第".compareTo("词"));*/
		
	}

}
