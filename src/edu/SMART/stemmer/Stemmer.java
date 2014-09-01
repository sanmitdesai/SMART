package edu.SMART.stemmer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import edu.stanford.nlp.io.EncodingPrintWriter.out;

public class Stemmer{
	public String callStemmer(String exampleString){
		char[] w = new char[501];
	      Stemming s = new Stemming();
	      String output="";

	      
	      try
	      {
	    	  
//	         FileInputStream in = new FileInputStream("data\\movies.txt");
//	    	  String exampleString = "basic facts";
	    	  InputStream in = new ByteArrayInputStream(exampleString.getBytes(StandardCharsets.UTF_8));

	         try
	         { while(true)

	           {  int ch = in.read();

	              if (Character.isLetter((char) ch))
	              {
	                 int j = 0;
	                 while(true)
	                 {  ch = Character.toLowerCase((char) ch);
	                    w[j] = (char) ch;
	                    if (j < 500) j++;
	                    ch = in.read();
	                    if (!Character.isLetter((char) ch))
	                    {
	                       /* to test add(char ch) */
	                       for (int c = 0; c < j; c++) s.add(w[c]);

	                       /* or, to test add(char[] w, int j) */
	                       /* s.add(w, j); */

	                       s.stem();
	                       {  String u;

	                          /* and now, to test toString() : */
	                          u = s.toString();

	                          /* to test getResultBuffer(), getResultLength() : */
	                          /* u = new String(s.getResultBuffer(), 0, s.getResultLength()); */

//	                          System.out.print(u);
	                          output+=u;
	                       }
	                       break;
	                    }
	                 }
	              }
	              if (ch < 0) break;
//	              System.out.print((char)ch);
	              output+=(char)ch;
	           }
	         return output;
	         }
	         catch (IOException e)
	         {  System.out.println("error reading ");
	         return null;
	            
	         }
	      }
	      catch (Exception e)
	      {  e.printStackTrace();
	      return output;
	         
	      }
		
	}
	public static void main(String[] args) {
		Stemmer objStemmer = new Stemmer();
//		System.out.println(objStemmer.callStemmer("paniking things"));
	}
}