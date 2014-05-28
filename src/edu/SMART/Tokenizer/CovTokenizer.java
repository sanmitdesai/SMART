package edu.SMART.Tokenizer;
//import edu.SMART.Tokenizer.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import javax.print.attribute.standard.MediaSize.Other;

/**
 * @author Michael A. Covington (www.covingtoninnovations.com) 2013
 * Released into the public domain as a programming example.  No warranties.
 * 
 * Command line UI for simple tokenizer.
 * 
 * Copies stdin to stdout breaking it into words (one per line) and
 * uppercasing all letters.  Non-ASCII quotation marks and dashes are
 * changed to ASCII.
 * 
 * Input and output are UTF-8.  Output lacks byte order mark.
 * 
 * UnsupportedEncodingException will not happen unless the Java 
 * implementation is defective.
 * 
 * FEATURES THAT COULD BE ADDED:
 * Print help when --help is given as a command line argument.
 * Allow user to choose the input encoding.
 * If optional features are added to Tokenizer, make them available
 * through command line arguments.
 *
 */
public class CovTokenizer {
	
	public List<String> tokenizeInput(String fileName){
	try	{
PrintStream utf8out = new PrintStream(System.out, true, "UTF-8");
	    List<String> tokenizedWords = new ArrayList<>();
	    //List<String> list = new ArrayList<>();
	    File fileDir = new File(fileName);
	    
		BufferedReader in = new BufferedReader(
		   new InputStreamReader(
                      new FileInputStream(fileDir), "UTF8"));
 
		String str;
 
		while ((str = in.readLine()) != null) {
			
			
			List<String> list = Tokenizer.Tokenize(str);
			for (String ss: list)
			{
				//utf8out.println(ss);
				tokenizedWords.add(ss);
			}
		    
		}
 
                in.close();
		
		return tokenizedWords;
	}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	
		
	}
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		
		CovTokenizer obj = new CovTokenizer();
		Iterator<String> it = obj.tokenizeInput("C:\\Users\\Sanmit\\workspace\\CompLing\\docs\\Apple.txt").iterator();
		System.out.println("output --- ");
		while(it.hasNext()){
			System.out.println(it.next());
		}
		/*PrintStream utf8out = new PrintStream(System.out, true, "UTF-8");
	    
		Scanner sc = new Scanner(System.in, "UTF-8");
		while (sc.hasNextLine())
		{
			String s = sc.nextLine();
			
			List<String> list = Tokenizer.Tokenize(s);
			for (String ss: list)
			{
				utf8out.println(ss);
			}
			
		}
		sc.close();*/
	}
}
