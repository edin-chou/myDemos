package util;

import java.util.Comparator;

public class ComparatorFactory {

	public static PreComparator getPreComparator(){
		return new PreComparator();
	}
	
	public static PostComparator getPostComparator(){
		return new PostComparator();
	}
	
}

class PreComparator implements Comparator<String>{
	@Override
	public int compare(String o1, String o2) {
		// TODO Auto-generated method stub
		return o1.compareTo(o2); 
	}
}	

class PostComparator implements Comparator<String>{
	@Override
	public int compare(String o1, String o2) {
		// TODO Auto-generated method stub
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		for(int i=o1.length();i>0;i--){
			sb1.append(o1.charAt(i-1));
		}
		for(int i=o2.length();i>0;i--){
			sb2.append(o2.charAt(i-1));
		}
		
		return sb1.toString().compareTo(sb2.toString());
	}
}
