package edu.SMART.sentimentAnalyzer;


import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jfree.ui.RefineryUtilities;

import edu.SMART.POS.SentiWordNetTags;
import edu.SMART.ReadWordList.EmotionsWordList;
import edu.SMART.basicDSFTasks.PrintAndInitialize;
import edu.SMART.showStats.PieChartDemo1;
import edu.stanford.nlp.io.EncodingPrintWriter.out;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class EmotionAnalyzer {

	public static MaxentTagger tagger = new MaxentTagger("lib\\models\\english-caseless-left3words-distsim.tagger");
	public static SentiWordNetTags sentiTagger = new SentiWordNetTags();
	public static SentiWordWordList sentiwordnet = new SentiWordWordList("wordlist\\SentiWordNet_3.0.0_20130122.txt");
	public static EmotionsWordList objEmotionsWordList = new EmotionsWordList();
	public static HashMap<String, ArrayList<String>> emotionsWordList = objEmotionsWordList.makeWordList("wordlist\\emotions.csv", "wordlist\\emotions2.csv");
	public static PrintAndInitialize objAndInitialize = new PrintAndInitialize();

	/**************************
	 * 
	 */
	public EmotionAnalyzer(){

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
			currLine = this.intensifiers(currLine);
			//			objAndInitialize.displayNestedArraylist(currLine);			
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
		else if(input>0.8) {
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
		else if(input<-0.8) {
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
			try{

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
			}catch(Exception e){
				output.add("0");
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
			//			System.out.println(word);
			for(int i=0;i<9;i++){
				//				System.out.print(word.get(i+1)+",");
				if(i==0){
					//					System.out.print("here,");
					String sentiScore = word.get(i+1);
					if(sentiScore.contains("--")){
						sentiScore = sentiScore.substring(1);
						//						System.out.println("here");
					}
					tempScore = Integer.parseInt(sentiScore)+Integer.parseInt(output.get(i));
					output.set(i,""+tempScore);
				}

				else{
					if(this.containEmotionSentiment(word)){
						String sentiScore = word.get(i+1);
						if(sentiScore.contains("--")){
							sentiScore = sentiScore.substring(2);
							//						System.out.println("here");
						}
						tempScore = Math.abs(Integer.parseInt(sentiScore))+Math.abs(Integer.parseInt(output.get(i)));
						//						System.out.print(output.get(i)+",");
						output.set(i,""+tempScore);
					}
				}

			}

		}

		//		System.out.println(">>"+output);
		return output;
	}
	/**************************
	 * returns true if an intensifier is encountered
	 * @param log
	 * @return
	 */
	public static boolean intensityCheck(String log){
		if(log.contains("so")
				||log.contains("too")
				||log.contains("very")
				||log.contains("really")
				||log.contains("bloody")
				||log.contains("dreadfully")
				||log.contains("extremely")
				||log.contains("fucking")
				||log.contains("hella")
				||log.contains("most")
				||log.contains("precious")
				||log.contains("quite")
				||log.contains("real")
				||log.contains("remarkably")
				||log.contains("terribly")
				||log.contains("moderately")
				||log.contains("wicked")
				||log.contains("bare")
				||log.contains("rather")
				||log.contains("somewhat")
				||log.contains("fully")
				||log.contains("super")
				||log.contains("veritable"))
		{

			return true;
		}
		else{
			return false;
		}
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
				||log.contains("isnt")
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

			/**********
			 * checks for valence shifter
			 */
			if(negationCheck(currWord)){

				negFlag=!negFlag;
				i++;

				if(i<input.size()){
					currWord = input.get(i).get(0);

					currWordList = input.get(i);
				}
				else{
					negFlag=false;
				}
			}

			if(currWord.equals(".")||currWord.equals(",")||currWord.equals("!")||currWord.equals("?")){

				negFlag = false;
			}

			if(negFlag && this.containEmotionSentiment(currWordList)){
				String temp = currWordList.get(1);
				currWordList = this.reverseAll(currWordList);
				currWordList.add(temp);
				input.set(i, currWordList);

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

		if(currWord.startsWith("-")){
			arrayList.set(1,currWord.substring(1));
		}
		else if(!currWord.equals("0")){

			arrayList.set(1, "-"+currWord);

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

				}
			}//if 2-4
			else{
				if(!currWord.equals("0")&&flagArrayList.equals("0")){
					arrayList.set(i, "0");
					arrayList.set(i-4, currWord);
				}
			}//else 5-9

		}//for

		return arrayList;
	}

	/******************************
	 * if an intensifier is encountered the following relevent word is then given a value of higher intensity than its own
	 * @param input
	 * @return
	 */
	public ArrayList<ArrayList<String>> intensifiers(ArrayList<ArrayList<String>> input){
		boolean intFlag = false;
		String currWord;
		ArrayList<String> currWordList;
		for(int i=0;i<input.size();i++){
			currWordList = input.get(i);
			currWord=currWordList.get(0);

			/**********
			 * checks for intensity
			 */
			if(intensityCheck(currWord)){

				intFlag=!intFlag;
				i++;
				if(i<input.size()){
					currWord = input.get(i).get(0);

					currWordList = input.get(i);
				}
				else{
					intFlag=false;
				}
			}

			if(currWord.equals(".")||currWord.equals(",")||currWord.equals("!")||currWord.equals("?")){

				intFlag = false;
			}

			if(intFlag && this.containEmotionSentiment(currWordList)){
				if(currWordList.size()>11){

					currWordList.remove(currWordList.size()-1);

					input.set(i, this.objectiveIntensifyAll(input.get(i)));

					intFlag = false;
				}
				else{
					input.set(i, this.intensifyAll(input.get(i)));
					intFlag = false;
				}

			}
		}
		return input;
	}

	/************************************
	 * deals with condition where negation is followed by an intensifier
	 * reduces the intensity of the word by one
	 * example if sentiment score was 3 it will become 2
	 * @param arrayList
	 * @return
	 */
	public ArrayList<String> objectiveIntensifyAll(ArrayList<String> arrayList) {
		String scoreString = arrayList.get(1);
		if(scoreString.startsWith("--")){
			scoreString=scoreString.substring(1);
		}
		int score = Integer.parseInt(scoreString);
		if(score<0){
			if((score+1)>=0){

				return this.setValuesToAll(arrayList, -1);
			}
			else{

				return this.setValuesToAll(arrayList, score+1);
			}
		}//negative
		else{
			if((score-1)<=0){

				return this.setValuesToAll(arrayList, 1);
			}
			else{

				return this.setValuesToAll(arrayList, score-1);
			}
		}//zero and positive

	}
	/*************************
	 * adds 1 to the given word to intensify its sentiment and emotion score
	 * @param arrayList
	 * @return
	 */
	public ArrayList<String> intensifyAll(ArrayList<String> arrayList) {
		String sentiScore = arrayList.get(1);
		if(sentiScore.contains("--")){
			sentiScore = sentiScore.substring(1);
			//			System.out.println("here");
		}
		int score = Integer.parseInt(sentiScore);

		if(score<0){
			if((score-1)<-5){

				return this.setValuesToAll(arrayList, -5);
			}
			else{

				return this.setValuesToAll(arrayList, score-1);
			}
		}//negative
		else{
			if((score+1)>5){

				return this.setValuesToAll(arrayList, 5);
			}
			else{

				return this.setValuesToAll(arrayList, score+1);
			}
		}//zero and positive

	}

	/*****************************
	 * given a integer score this function sets the value to all elements of the arrayList
	 * except first and the last element, the elements 2-9 will contain absolute value of the score
	 * @param input
	 * @param score
	 * @return
	 */
	public ArrayList<String> setValuesToAll(ArrayList<String> input, int score){
		input.set(1, ""+score);
		String scoreString = ""+Math.abs(score);
		for(int i=2;i<input.size()-1;i++){
			if(!input.get(i).equals("0"))
				input.set(i, scoreString);
		}

		return input;
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
				String input = "Tense standoff between obama and the senate isn't good";
//				String input = "Tense standoff between obama and the senate isnt very good";
//		String input = "tense standoff between obama and the senate is very good";
//				String input = "Too late now -everyone is there But not too late to boycott the closing ceremony Syria Ukraine Sochi2014";
		System.out.println(obj.analysis(input));
		PieChartDemo1 demo = new PieChartDemo1("Emotion Pie Chart",obj.analysis(input),input);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}
}
