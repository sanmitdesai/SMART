package edu.SMART.ReadWriteFile;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SMARTFileWriter {


	public void printMap(Map mp) {
	    Iterator it = mp.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	public void writeHashMapToCSV(HashMap<String, Integer>mp, String fileName){
		BufferedWriter out;
		CSVWriter writer;
		try {
			out = new BufferedWriter(new java.io.FileWriter(fileName));

			writer = new CSVWriter(out);
			
			
			Iterator it = mp.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
//		        System.out.println(pairs.getKey() + " = " + pairs.getValue());
		        if(Integer.parseInt(""+pairs.getValue())>=10){
		        String[] values = {""+pairs.getKey(),""+pairs.getValue()};
		        writer.writeNext(values);
		        }
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		    System.out.println("Bigrams successfully Written to file "+fileName);
			
			
			
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public static void main(String[] main) throws Exception{
		SMARTFileWriter obj = new SMARTFileWriter();
		
		
	}
}