import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;




import java.util.Arrays;
import java.util.HashSet;

import org.jfree.chart.urls.URLUtilities;
import org.w3c.dom.Text;


public class Test {
	public static boolean isBetween(double x, double lower, double upper) {
		  return lower < x && x <= upper;
		}
	
	public static int extrapolateToFiveScale(double input){
		
		if(isBetween(input, 0.0, 0.2)){
			return 1;
		}
		else if(isBetween(input, 0.2, 0.4)) {
			return 2;
		}
		else if(isBetween(input, 0.4, 0.6)) {
			return 3;
		}
		else if(isBetween(input, 0.6, 0.8)) {
			return 4;
		}
		else if(isBetween(input, 0.8, 1.0)) {
			return 5;
		}
		else if(isBetween(input, -0.2, 0.0)) {
			return -1;
		}
		else if(isBetween(input, -0.4, -0.2)) {
			return -2;
		}
		else if(isBetween(input, -0.6, -0.4)) {
			return -3;
		}
		else if(isBetween(input, -0.8, -0.6)) {
			return -4;
		}
		else if(isBetween(input, -1.1, -0.8)) {
			return -5;
		}
		else {
			return 0;
		}
		
	}
	
	
	public static int fibonacci(int n)  {
	    if(n == 0)
	        {
//	    	System.out.println(0);
	    	return 0;}
	    else if(n == 1)
	      {
//	    	System.out.println(1);
	    	return 1;}
	   else
	   { int temp =fibonacci(n - 2)+ fibonacci(n - 1)  ;
		   System.out.println(temp);
		   return temp;}
	}
	
	
	public static int factorial(int limit){
		int mul=1;
		for(int i=1;i<=limit;i++){
			mul*=i;
		}
		return mul;
	}
	
public static void main(String[] args) throws UnsupportedEncodingException {
//	String example = "RT @richardbranson: So sad to see violence ruling over democracy in #Ukraine &amp; #Venezuela. We all need to stand up for true democracy http:…'";
//	System.out.println(example.replaceAll("RT @.*: ", ""));
	
	
//	fibonacci(10);
//	System.out.println(factorial(4));
	
	char[] charArray = {'a', 'b', 'c'};
	String str = String.valueOf(charArray);
	System.out.println(str);
	str=String.valueOf(Arrays.copyOfRange(charArray, 0, 1));
	
	
	String input = "OMG this cuiteeeee is so hooootttt funny !!!!!!!!! :-)))";
//	System.out.println(input.replaceAll("(.)\\1{2,}", "$1"));
	
	String data = "President Obama called Wednesday on Congress to extend a tax break for students included in last year's economic stimulus package, arguing that the policy provides more generous assistance.";
	String temp = URLUtilities.encode(data, "UTF-8");
//	System.out.println(temp.replace("+", "%20"));
	
//	System.out.println(extrapolateToFiveScale(-0.766666219));
	
	byte a = 7;
	int position = 3;
//	System.out.println(Integer.toBinaryString(a));
	String abc = "llll";
	char an = 'a';
abc+=an;

//int number = 10;
//long a = 0;
//long b = 1;
//for(int i = 1; i<number;i++){
//    long c = a +b;
//    a=b;
//    b=c;
//    System.out.println(c);
//}
//System.out.println(a);


	System.out.println((a>>position));
	
	
	
}
}
