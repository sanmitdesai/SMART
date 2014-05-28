package edu.SMART.sentimentAnalyzer;
import edu.SMART.*;
import edu.SMART.Tokenizer.Tokenizer;
import edu.SMART.showStats.Barchart;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class CompareAffinWordList {
	public static HashMap<String, Integer> affinWordList = new HashMap<>();
	private  Scanner scanner;
	public static CompareNormalWordList objectForNormal = new CompareNormalWordList();
	public static HashMap<String, Integer> tweets = new HashMap<>();
	public static Barchart objectBarChart = new Barchart();
	/*************************************************************
	 * 
	 * @param wordList
	 * @param targetFile
	 */
	public CompareAffinWordList(String wordList,String targetFile) {
		affinWordList = this.readAfinnWordlistHashMapGeneration(wordList);
		//System.out.println(affinWordList);
		tweets = objectForNormal.readFile(targetFile);
		this.calculateSentiment();
		int[] arrayForGraph = this.convert();
		objectForNormal.displayGraph(arrayForGraph,"Sentiment With Affin Word List");
		objectBarChart.plotPie(objectForNormal.barChartData(arrayForGraph),"Chart Affin.jpg");
	}
	/***********************************************************
	 * 
	 * @return
	 */
	public int[] convert(){
		int[] intArray = new int[tweets.size()];
		int i =0;
		for (Map.Entry entry : tweets.entrySet()) { 
			//String tweet = (String)entry.getKey();
			//tweets.put(tweet, this.getSentimentScore(tweet));
			intArray[i] = (int) entry.getValue();
			//System.out.println(intArray[i]);
			i++;

		}

		return intArray;
	}
	/********************************************************
	 * 
	 */
	public CompareAffinWordList() {

	}

	/*****************************************************
	 * 
	 * @param fileLoc
	 * @return
	 * @throws FileNotFoundException
	 */
	public HashMap<String,Integer> readAfinnWordlistHashMapGeneration(String fileLoc){
		try{
			File f = new File(fileLoc);
			int value=0;
			HashMap <String,Integer> wordList = new HashMap<>();
			scanner = new Scanner(f);
			while(scanner.hasNext()){
				if(!scanner.hasNextInt()){
					String word = scanner.next().toUpperCase();
					//System.out.println("Word:"+word);
					if(!scanner.hasNextInt()){
						word = word+" "+scanner.next().toUpperCase();
						//System.out.println("Word inside loop:"+word);
						if(!scanner.hasNextInt()){
							word = word+" "+scanner.next().toUpperCase();
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
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}


	}
	/****************************************************************
	 * 
	 * @param input
	 * @return
	 */
	public int getSentimentScore(String input){

		int output=0;
		List<String> tokenizeInput = Tokenizer.Tokenize(input);
		for (int i = 0; i < tokenizeInput.size(); i++) {
			String log = tokenizeInput.get(i);
			if(affinWordList.containsKey(log)){
				output+=affinWordList.get(log);
				//System.out.println(log+affinWordList.get(log));
			}
			

		}
		return output;
	}
	
	/************************************************************
	 * 
	 */
	public void calculateSentiment(){
		for (Map.Entry entry : tweets.entrySet()) { 
			String tweet = (String)entry.getKey();
			tweets.put(tweet, this.getSentimentScore(tweet));

		}
	}
	/****************************************************************
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		//CompareAffinWordList obj = new CompareAffinWordList("wordlist\\AFINN-111.txt","tweets.txt");
		//CompareNormalWordList obj1 = new CompareNormalWordList("wordlist\\LoughranMcDonald_Positive.csv","wordlist\\LoughranMcDonald_Negative.csv","tweets.txt");
		//System.out.println(affinWordList);
	}
}
