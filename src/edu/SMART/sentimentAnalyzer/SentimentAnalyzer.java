package edu.SMART.sentimentAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.SMART.ReadWordList.ReadCVS;
import edu.SMART.Tokenizer.Tokenizer;
import edu.SMART.mongoDB.ReadFromMongo;
import edu.SMART.stemmer.Cleaning;

public class SentimentAnalyzer {

	/***************************
	 * wordList created from a given csv file with negative and positive sentiment
	 */
	public HashMap<String, Integer> wordList = new HashMap<>();

	/******************************
	 * initialize wordList
	 * @param csvFile
	 */
	public SentimentAnalyzer(String csvFile) {

		ReadCVS readCSVObject = new ReadCVS();
		wordList = readCSVObject.wordListGeneratorAFINN(csvFile);
	}
	
	/*public void analyzer(String tweet){
		System.out.println(tweet);
	}*/
	public int analyzer(String input){

		int output=0;
		Cleaning objCleaning = new Cleaning();
		input = objCleaning.removeRT(input);
		List<String> tokenizeInput = Tokenizer.Tokenize(input);
		for (int i = 0; i < tokenizeInput.size(); i++) {
			String log = tokenizeInput.get(i);
			log = objCleaning.removeHashTag(log);
			if(wordList.containsKey(log)){
				System.out.print(log+"="+wordList.get(log)+" ");
				output+=wordList.get(log);
				//System.out.println(log+affinWordList.get(log));
			}
//			System.out.println();

		}
		return output;
	}
	public static void main(String[] args) {
//		SentimentAnalyzer obj = new SentimentAnalyzer("wordlist/twitter_sentiment_list.csv");
		//		System.out.println(obj.wordList);
		ReadFromMongo objFromMongo = new ReadFromMongo();
		objFromMongo.readMongoSentiAnalysis("test", "ukraine_v1", 21 ,50, "wordlist/SMART.csv");
	}

}

