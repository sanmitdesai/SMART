package edu.SMART.sentimentAnalyzer;


import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.SMART.POS.SentiWordNetTags;
import edu.SMART.ReadWordList.EmotionsWordList;
import edu.SMART.basicDSFTasks.PrintAndInitialize;
import edu.stanford.nlp.ling.HasWord;
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
	/****************************
	 * compares two word combinations with the emotions wordList
	 * @param input
	 * @return
	 */
	public ArrayList<ArrayList<String>> bigram(ArrayList<ArrayList<String>> input){
		int size = input.size();
		ArrayList<String> tempWordOne=new ArrayList<String>();
		ArrayList<String> tempWordTwo=new ArrayList<String>();

		tempWordTwo = objAndInitialize.initializeArrayList(tempWordTwo, 12);
		String bigram;
		for(int i=0;i<size;i++){
			if(i+1>=size){
				break;
			}
			else{
				bigram = input.get(i).get(0)+" "+input.get(i+1).get(0);
				if(emotionsWordList.containsKey(bigram)){
					tempWordOne = emotionsWordList.get(bigram);

					tempWordOne.set(0, input.get(i).get(0));
					tempWordOne.set(10,input.get(i).get(10));
					input.set(i, tempWordOne);

					tempWordTwo.set(0, input.get(i+1).get(0));
					tempWordTwo.set(10, input.get(i+1).get(10));
					input.set(i+1, tempWordTwo);

				}

			}
		}
		return input;
	}
	/**********************
	 * tags the sentence for POS tags and uses the same for sentiment analysis
	 * @param tweet
	 */
	public ArrayList<String> analysis(String tweet){

		DocumentPreprocessor sentences = new DocumentPreprocessor(new StringReader(tweet));

		//		System.out.println(sentences);
		ArrayList<ArrayList<String>> currLine;
		ArrayList<String> output = null;
		for (List<HasWord> sentence : sentences) {
			List<TaggedWord> tSentence = tagger.tagSentence(sentence);

			currLine = this.unigram(tSentence);
			currLine = this.bigram(currLine);
			currLine = this.valenceShifter(currLine);
			objAndInitialize.displayNestedArraylist(currLine);
			output = this.combineEmotions(currLine);

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
					
					output.add("-"+temp);
				}
				else{
					temp = Integer.parseInt(element)*extrapolateToFiveScale(sentiScore);
					
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
	public ArrayList<ArrayList<String>> unigram(List<TaggedWord> tSentence){
		String currentWord,currentTag;
		ArrayList<ArrayList<String>> sentence = new ArrayList<ArrayList<String>>();
		Double tempScore;
		for(TaggedWord word : tSentence){
			currentWord = word.word().toLowerCase();
			currentTag=word.tag();

			if(emotionsWordList.containsKey(currentWord)){
				tempScore = sentiwordnet.extract(currentWord, sentiTagger.returnSentiWordNetTags(currentTag)); 

				ArrayList<String> temp = new ArrayList<String>();
				temp = emotionsWordList.get(currentWord);
				temp.set(10, currentTag);
				if(tempScore==0.0){
					sentence.add(temp);
				}
				else{
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
				if(i==0){
					tempScore = Integer.parseInt(word.get(i+1))+Integer.parseInt(output.get(i));
					output.set(i,""+tempScore);
				}
				
				else{
					tempScore = Math.abs(Integer.parseInt(word.get(i+1)))+Math.abs(Integer.parseInt(output.get(i)));
					output.set(i,""+tempScore);
				}
				
			}

		}

		return output;
	}
	
	/*********************
	 * checks for negation in a sentence
	 * @param log
	 * @return
	 */
	public static boolean negationCheck(String log){
		if(log.contains("n't")
				||log.contains("not")
				||log.contains("never")
				||log.contains("none")
				||log.contains("nowhere")
				||log.contains("nothing")
				||log.contains("neither")
				||log.contains("hardly")
				||log.contains("scarcely")
				||log.contains("barely")
				||log.contains("nor")
				||log.contains("donno")
				||log.contains("bad")
				||log.contains("hasnt")
				||log.contains("havent")
				||log.contains("havnt")
				||log.contains("however")
				||log.contains("no")
				||log.contains("nothin")
				||log.contains("neither")
				||log.contains("nor")
				||log.contains("hope")
				||log.contains("how")
				||log.contains("lack")
				||log.contains("lacking")){
			
			return true;
		}
		else{
			return false;
		}
	}
	
	/***************************
	 * checks for valence shifters like n't , not , never etc and reverses the sentiment
	 * and/or emotions of the word or words that follow
	 * @param input
	 * @return
	 */
	public ArrayList<ArrayList<String>> valenceShifter(ArrayList<ArrayList<String>> input){
		boolean negFlag = false;
		String currWord;
		ArrayList<String> currWordList;
		for(int i=0;i<input.size();i++){
			currWordList = input.get(i);
			currWord=currWordList.get(0);
//			System.out.println(currWord);
			if(negationCheck(currWord)){
//				System.out.println(" negation found "+currWord);
				negFlag=!negFlag;
				i++;
				currWord = input.get(i).get(0);

				currWordList = input.get(i);
			}
			
			if(currWord.equals(".")||currWord.equals(",")||currWord.equals("!")||currWord.equals("?")){
//				System.out.println("negation removed "+currWord);
				negFlag = false;
			}
			
			if(negFlag && this.containEmotionSentiment(currWordList)){
//				System.out.println("word found "+currWordList);
				input.set(i, this.reverseAll(input.get(i)));
				negFlag = false;
			}
		}
		return input;
	}
	/******************
	 * reverses emotions and sentiment of a given word
	 * @param arrayList
	 * @return
	 */
	public ArrayList<String> reverseAll(ArrayList<String> arrayList) {
		String currWord = arrayList.get(1);
//		System.out.println(currWord);
		if(currWord.startsWith("-")){
			arrayList.set(1,currWord.substring(1));
		}
		else if(!currWord.equals("0")){
//			System.out.println(currWord);
			arrayList.set(1, "-"+currWord);
//			System.out.println(arrayList.get(1));
		}
		 ArrayList<String> flagArrayList = new ArrayList<String>();
		 flagArrayList = objAndInitialize.initializeArrayList(flagArrayList, 11);
		for(int i =2;i<arrayList.size()-1;i++){
			currWord=arrayList.get(i);
			if(i<=5){
				if(!currWord.equals("0")){
					
					arrayList.set(i, "0");
					arrayList.set(i+4, currWord);
					flagArrayList.set(i+4, currWord);
//					System.out.println(arrayList.get(i)+arrayList.get(i+4));
				}
			}//if 2-4
			else{
				if(!currWord.equals("0")&&flagArrayList.equals("0")){
					arrayList.set(i, "0");
					arrayList.set(i-4, currWord);
				}
			}//else 5-9
			
		}//for
//		System.out.println(arrayList);
		return arrayList;
	}
	
	/*******************
	 * checks if a word is a sentiment and/or emotion word
	 * @param arrayList
	 * @return
	 */
	public boolean containEmotionSentiment(ArrayList<String> arrayList) {
		String currWord;
		
		for(int i =1;i<arrayList.size()-1;i++){
			currWord = arrayList.get(i);
			
			if(!currWord.equals("0")){
				return true;
			}
			
		}
		return false;
		
		
	}
	/****************
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		EmotionAnalyzer obj = new EmotionAnalyzer();
		System.out.println(obj.analysis("tense standoff between obama and the senate isn't good"));
	}
}
