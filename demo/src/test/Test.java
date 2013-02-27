package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

public class Test{

	public synchronized void  love() throws InterruptedException{
		for(;true;this.wait(1000))
			System.out.println("5"+(1900+new Date().getYear())/100);
	}
	
	
	public static void main(String a[]) throws InterruptedException, IOException{
/*		int inte =0;
		while(inte<257){
			System.out.print(inte+":");
			System.out.write(inte);
			inte++;
		}*/
		/*Test t = new Test();
		t.love();*/
		/*for(;true;Thread.sleep(1000))
			System.out.println((1900+new Date().getYear())/100);*/
		//System.out.println(5+new SimpleDateFormat("yyyyMd").format(new Date()));
		//System.out.println(1900+new Date().getYear());
		//while(true)
			//System.out.println(5+new SimpleDateFormat("yyyyMd").format(new Date()));
		
		
/*		String sentence = "一二三四五六七八sadf";
		int senLen=sentence.length();
		if(senLen<=0){
			return;
		}	
		for(int end = senLen;end>0;end--){
			for(int start = end-1;end-start<=5 && start>=0;start--){
				System.out.println(sentence.substring(start,end));
			}
		}*/
		
/*		BufferedReader br = new BufferedReader(new FileReader(new File("E:/test/result26170388875364.txt")));
		String s = br.readLine();
		while(s!=null){
			System.out.println(s);
			s=br.readLine();
		}*/
		
/*		String sentence = "一二三四五六七八";
		int MAXLEN=5;
		int senLen=sentence.length();
		if(senLen<=0){
			return;
		}
		for(int start=0,end;start<senLen;start++){
			end=start+MAXLEN;
			System.out.println(start+"+"+end+"+"+senLen);
			System.out.println(sentence.substring(start,end<senLen?end:senLen));
		}*/
		
		String sentence = "一二三四五六七八";
		int MAXLEN=5;
		int senLen=sentence.length();
		if(senLen<=0){
			return;
		}
		for(int end=senLen,start;end>0;end--){
			start=end-MAXLEN;
			System.out.println(sentence.substring(start<0?0:start,end));
		}
	}
}
