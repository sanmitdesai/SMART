package edu.SMART.sentimentAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.SMART.ReadWordList.ReadCVS;
import edu.SMART.Tokenizer.Tokenizer;

public class SentimentAnalyzer {

	/***************************
	 * wordList created from a given csv file with negative and positive sentiment
	 */
	public HashMap<String, ArrayList<Double>> wordList = new HashMap<>();

	/******************************
	 * initialize wordList
	 * @param csvFile
	 */
	public SentimentAnalyzer(String csvFile) {

		ReadCVS readCSVObject = new ReadCVS();
		wordList = readCSVObject.wordListGenerator(csvFile);
	}
	
	/*********************************
	 * Given a list return total of all elements inside the list
	 * @param list
	 * @return
	 */
	
	public double sum(List<Double> list){
	      if(list==null || list.size()<1)
	        return 0.0;

	      Double sum = 0.0;
	      for(Double i: list)
	        sum = sum+i;

	      return sum;
	    }

	/****************************
	 * get array from a given string with separated words
	 * @param line
	 * @return
	 */
	public ArrayList<String> analyzeTweet(String line){

		return Tokenizer.Tokenize(line);
	}

	public void calculateSentiment(ArrayList<String> line){
		ArrayList<Double> positiveList = new ArrayList<>();
		ArrayList<Double> negativeList = new ArrayList<>();
		
		for(String word : line){
			if(wordList.containsKey(word)){
				ArrayList<Double> temp = wordList.get(word);
				positiveList.add(temp.get(0));
				negativeList.add(temp.get(1));
			}//if
			else{
				positiveList.add(0.0);
				negativeList.add(0.0);
			}//else
		}//for
		
		double positiveTotal = this.sum(positiveList);
		double negativeTotal = this.sum(negativeList);
		
		
		
	}

	public static void main(String[] args) {
		SentimentAnalyzer obj = new SentimentAnalyzer("wordlist/twitter_sentiment_list.csv");
		//		System.out.println(obj.wordList);
	}

}

