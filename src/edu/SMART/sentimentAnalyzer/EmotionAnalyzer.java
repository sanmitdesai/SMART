package edu.SMART.sentimentAnalyzer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.SMART.POS.SentiWordNetTags;
import edu.SMART.ReadWordList.EmotionsWordList;
import edu.SMART.basicDSFTasks.PrintAndInitialize;
import edu.stanford.nlp.io.EncodingPrintWriter.out;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class EmotionAnalyzer {

	public static MaxentTagger tagger = new MaxentTagger("lib\\models\\english-caseless-left3words-distsim.tagger");
	public static SentiWordNetTags sentiTagger = new SentiWordNetTags();
	public static SentiWordWordList sentiwordnet = new SentiWordWordList("wordlist\\SentiWordNet_3.0.0_20130122.txt");
	public static EmotionsWordList objEmotionsWordList = new EmotionsWordList();
	public static HashMap<String, ArrayList<String>> emotionsWordList = objEmotionsWordList.makeWordList("wordlist/emotions.csv", "wordlist\\emotions2.csv");
	public static PrintAndInitialize objAndInitialize = new PrintAndInitialize();

/**************************
 * 
 */
	private EmotionAnalyzer(){
		
	}

	/**********************
	 * tags the sentence for POS tags and uses the same for sentiment analysis
	 * @param tweet
	 */
	public ArrayList<String> analysis(String tweet){
		//		List<List<HasWord>> sentences;

		//		InputStream is = new ByteArrayInputStream(tweet.getBytes());
		//		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		//		sentences = MaxentTagger.tokenizeText(br);
		DocumentPreprocessor sentences = new DocumentPreprocessor(new StringReader(tweet));

		//					System.out.println(sentences);
		ArrayList<ArrayList<String>> temp;
		ArrayList<String> output = null;
		for (List<HasWord> sentence : sentences) {
			List<TaggedWord> tSentence = tagger.tagSentence(sentence);

			temp = this.wordMatch(tSentence);
			objAndInitialize.displayNestedArraylist(temp);
			output = this.combineEmotions(temp);

		}//for
		
		return output;
	}
/************************
 * returns true if x is greater than the lower bound and less than or equal to the upper bound
 * else returns false	
 * @param x
 * @param lower
 * @param upper
 * @return
 */
	public static boolean isBetween(double x, double lower, double upper) {
		  return lower < x && x <= upper;
		}
/******************
 * extrapolates sentiment score on the scale of 1 to -1
 * to the scale of 5 to -5	
 * @param input
 * @return
 */
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
/****************
 * checks for a given word in sentiwordnet and returns the sentiment score 	
 * @param input
 * @param sentiScore
 * @return
 */
	public static ArrayList<String> setWithSentiScore(ArrayList<String> input, Double sentiScore){
		int temp=0;
		ArrayList<String> output = new ArrayList<String>();
		int size = input.size();
		output.add(input.get(0));
		String element;
		for(int i=1;i<size-1;i++){
			element = input.get(i);
			if(!element.equals(""+0)){
				if(element.contains("-")){
					temp =Integer.parseInt(element)*extrapolateToFiveScale(sentiScore);
//					System.out.print(input.get(i)+" ");
					output.add("-"+temp);
				}
				else{
					temp = Integer.parseInt(element)*extrapolateToFiveScale(sentiScore);
//					System.out.print(input.get(i)+" ");
					output.add(""+Math.abs(temp));
				}
			}
			else{
				output.add(element);
			}

		}

		output.add(input.get(size-1));
		return output;
	}
/***************************
 * matches each word of a sentence to express emotion for each word
 * @param tSentence
 * @return
 */
	public ArrayList<ArrayList<String>> wordMatch(List<TaggedWord> tSentence){
		String currentWord,currentTag;
		ArrayList<ArrayList<String>> sentence = new ArrayList<ArrayList<String>>();
		Double tempScore;
		for(TaggedWord word : tSentence){
			currentWord = word.word().toLowerCase();
			currentTag=word.tag();

			if(emotionsWordList.containsKey(currentWord)){
				//				System.out.println(word+"="+sentiwordnet.extract(currentWord, sentiTagger.returnSentiWordNetTags(word.tag()))
				//						+" == "+emotionsWordList.get(currentWord));

				tempScore = sentiwordnet.extract(currentWord, sentiTagger.returnSentiWordNetTags(currentTag)); 

				ArrayList<String> temp = new ArrayList<String>();
				temp = emotionsWordList.get(currentWord);
				temp.set(10, currentTag);
				if(tempScore==0.0){
					sentence.add(temp);
				}
				else{
//					System.out.println("found in senti "+currentWord);
					sentence.add(setWithSentiScore(temp,tempScore));
				}

			}
			else
			{
				ArrayList<String> temp = new ArrayList<String>();
				objAndInitialize.initializeArrayList(temp, 12);
				temp.set(0, currentWord);
				temp.set(10, currentTag);
				sentence.add(temp);
			}

		}
		//		System.out.println(sentence);
		return sentence;
	}
/*************
 * Combines emotions in a given sentence to express total emotions
 * @param sentence
 * @return
 */
	public ArrayList<String> combineEmotions(ArrayList<ArrayList<String>> sentence){
		ArrayList<String> output = new ArrayList<String>();
		output = objAndInitialize.initializeArrayList(output, 10);
		int tempScore;
		for(ArrayList<String> word : sentence){
			for(int i=0;i<9;i++){
				tempScore = Integer.parseInt(word.get(i+1))+Integer.parseInt(output.get(i));
				output.set(i,""+tempScore);
			}

		}


		return output;
	}
/****************
 * 
 * @param args
 * @throws IOException
 */
	public static void main(String[] args) throws IOException {

		EmotionAnalyzer obj = new EmotionAnalyzer();


		System.out.println(obj.analysis("Obama appalling the senate decision"));
		//		System.out.println(emotionsWordList);

	}


}
