package edu.SMART.ReadWordList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;


public class ReadAfinnWordlist {
	
	private  HashMap <String,Integer> wordList;
	private  Scanner scanner;
	
	public HashMap<?,?> readAfinnWordlistHashMapGeneration(File f) throws FileNotFoundException{
		int value=0;
		scanner = new Scanner(f);
		while(scanner.hasNext()){
			if(!scanner.hasNextInt()){
				String word = scanner.next().toLowerCase();
				//System.out.println("Word:"+word);
				if(!scanner.hasNextInt()){
					word = word+" "+scanner.next().toLowerCase();
					//System.out.println("Word inside loop:"+word);
					if(!scanner.hasNextInt()){
						word = word+" "+scanner.next().toLowerCase();
						//System.out.println("Word inside 2nd loop:"+word);
						value = Integer.parseInt(scanner.next());
						//System.out.println("Value inside 2nd:"+value);
					}
					else{
					value = Integer.parseInt(scanner.next());
					//System.out.println("Value:"+value);
					}
				}
				else{
					value = Integer.parseInt(scanner.next());
					//System.out.println("Value:"+value);
				}
					if(wordList==null){
					wordList  = new HashMap<String,Integer>();
					wordList.put(word, value);
				
				}
				else
					wordList.put(word, value);
			}
		}
		
		
		return wordList;
				
	}
	
	public void iteratorMethodforSentiment(HashMap<String,Integer[]> inputHashMap){
		Iterator iterator = inputHashMap.keySet().iterator();
		int frequency =0;
		float tfidf = 0;
		int counter =0;
		while (iterator.hasNext()){
				String key = iterator.next().toString();
				System.out.print(key+":");
				Integer[] sentimentAndFrequency = new Integer[2];
				sentimentAndFrequency = inputHashMap.get(key);
				System.out.println(sentimentAndFrequency[0]+":"+sentimentAndFrequency[1]);
				counter = counter+sentimentAndFrequency[0]*sentimentAndFrequency[1];
				frequency=frequency+sentimentAndFrequency[1];
		}
		System.out.println("Weighted Value = "+counter);
		System.out.println("Frequency: "+frequency);
		tfidf = (float)counter/(float)frequency; 
		System.out.println("Weighted average="+tfidf);
	}
	
	public void iteratorMethodforFrequency(HashMap<String,Integer> inputHashMap){
		Iterator iterator = inputHashMap.keySet().iterator();
		while (iterator.hasNext()){
				String key = iterator.next().toString();
				System.out.print(key+":");
				System.out.println(inputHashMap.get(key));
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		File file = new File("C:/Users/Nilayan/Downloads/AFINN/AFINN-111.txt");
		ReadAfinnWordlist readAfinnWordListObject = new ReadAfinnWordlist();
		readAfinnWordListObject.readAfinnWordlistHashMapGeneration(file);
		readAfinnWordListObject.iteratorMethodforFrequency(readAfinnWordListObject.wordList);
	}

}
