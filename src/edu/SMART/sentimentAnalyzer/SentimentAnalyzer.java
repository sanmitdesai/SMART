package edu.SMART.sentimentAnalyzer;

import java.util.HashMap;
import java.util.List;
import edu.SMART.ReadWordList.ReadCVS;
import edu.SMART.Tokenizer.Tokenizer;
import edu.SMART.mongoDB.ReadFromMongo;
import edu.SMART.stemmer.Cleaning;

public class SentimentAnalyzer {

	private static List<String> tokenizeInput;

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

	/*********************
	 * checks for negation in a sentence
	 * @param log
	 * @return
	 */
	public boolean negationCheck(String log){
		if(log.contains("N'T")
				||log.contains("NOT")
				||log.contains("NEVER")
				||log.contains("NONE")
				||log.contains("NOWHERE")
				||log.contains("NOTHING")
				||log.contains("NEITHER")
				||log.contains("HARDLY")
				||log.contains("SCARCELY")
				||log.contains("BARELY")
				||log.contains("NOR")){
			
			return true;
		}
		else{
			return false;
		}
	}

	/**************************
	 * checks for single word phrases in the wordlist
	 * @return
	 */
	public int unigram(){
//		System.out.println(tokenizeInput);
		int output=0;
		boolean notFlag=false;
		for (int i = 0; i < tokenizeInput.size(); i++) {
			String log = tokenizeInput.get(i);
			
			if(notFlag){
				System.out.print("neg "+log+" ");
				if(log.contains(".")){
					
					notFlag=false;
				}
				else{
					
					if(wordList.containsKey(log)){
						
						System.out.print(log+"="+(wordList.get(log)*-1)+" ");
						output+=(wordList.get(log)*-1);
						notFlag=false;
					}
				}
			}
			else{
				notFlag=this.negationCheck(log);

				if(wordList.containsKey(log)){
					
					System.out.print(log+"="+wordList.get(log)+" ");

					output+=wordList.get(log);

				}
			}
		}
		return output;
	}

	/*******************************
	 * looks for two word phrases in the wordlist
	 * @return
	 */
	public int bigram(){
		int output=0;
		for(int i=0;i<tokenizeInput.size();i++){
			if(i+2>=tokenizeInput.size()){
				break;
			}
			else{
				String temp = tokenizeInput.get(i)+" "+tokenizeInput.get(i+1);
				//				System.out.println(temp);
				if(wordList.containsKey(temp)){
					System.out.print(temp+"="+wordList.get(temp)+" ");

					tokenizeInput.remove(i);
					tokenizeInput.remove(i);

					output+=wordList.get(temp);
				}

			}

		}
		return output;
	}
/****************************
 * cleans a given tweet
 * removes all hashtags and user names
 * @param input
 * @return
 */
	public int analyzer(String input){

		int output=0;
		Cleaning objCleaning = new Cleaning();
		
		//remove username
		input = objCleaning.removeUserNameFromTweet(input);
		
		// remove RT symbol
		input = objCleaning.removeRTFromTweet(input);
		
		//removes # sign from hashtags
		input = objCleaning.removeHashTagFromTweet(input);
		
		//make tokens of a sentence make a string arrayList of the same
		tokenizeInput = Tokenizer.Tokenize(input);

		//check for bigrams
		output+=this.bigram();

		//check for unigrams
		output+= this.unigram();

		//clear arraylist
		tokenizeInput.clear();
		
		
		return output;
	}


	public static void main(String[] args) {

		ReadFromMongo objFromMongo = new ReadFromMongo();
		objFromMongo.readMongoSentiAnalysis("test", "ukraine_v1", 0 ,5, "wordlist/SMART.csv");
//				SentimentAnalyzer obj = new SentimentAnalyzer("wordlist/SMART.csv");
//				System.out.println(obj.analyzer("@VladimirPutin hey dude... release pussy riot... and i don't like the shit in the ukraine... and some of my best friends are gay, i'm not"));
				

	}

}

