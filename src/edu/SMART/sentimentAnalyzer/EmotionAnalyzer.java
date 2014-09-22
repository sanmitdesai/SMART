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


	private EmotionAnalyzer(){
		//		MaxentTagger tagger = new MaxentTagger("lib\\models\\english-caseless-left3words-distsim.tagger");
	}

	public void analysis(String tweet){
//		List<List<HasWord>> sentences;

//		InputStream is = new ByteArrayInputStream(tweet.getBytes());
//		BufferedReader br = new BufferedReader(new InputStreamReader(is));
//		sentences = MaxentTagger.tokenizeText(br);
		DocumentPreprocessor sentences = new DocumentPreprocessor(new StringReader(tweet));

		//					System.out.println(sentences);

		for (List<HasWord> sentence : sentences) {
			List<TaggedWord> tSentence = tagger.tagSentence(sentence);

			ArrayList<ArrayList<String>> temp = this.wordMatch(tSentence);
			objAndInitialize.displayNestedArraylist(temp);
			System.out.println(this.combineEmotions(temp));

		}//for
	}



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
					System.out.println("found in senti "+currentWord);
					sentence.add(temp);
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

	public static void main(String[] args) throws IOException {

		EmotionAnalyzer obj = new EmotionAnalyzer();


		obj.analysis("Obama appalling the senate decision");
		//		System.out.println(emotionsWordList);

	}


}
