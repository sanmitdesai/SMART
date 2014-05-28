package edu.SMART.sentimentAnalyzer;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import edu.SMART.Tokenizer.CovTokenizer;
import edu.SMART.Tokenizer.Tokenizer;
import edu.SMART.createWordList.CreateWordList;
import edu.SMART.showStats.Barchart;


public class CompareNormalWordList {

	public static CovTokenizer objectForCovTokenizer = new CovTokenizer();
	//public static Tokenizer objectForTokenizer= new Tokenizer();
	public static CreateWordList objectForCreateWordList = new CreateWordList();


	public static HashMap<String, Integer> positive = new HashMap<>();
	public static HashMap<String, Integer> negative = new HashMap<>();
	public static HashMap<String, Integer> tweets = new HashMap<>();
	public static Barchart objectBarChart = new Barchart();
	/********************************************************
	 * 
	 * @param input
	 * @return
	 */
	public int[] barChartData (int[] input)
	{
		int[] output = new int[3];
		output[0]=output[1]=output[2]=0;
		
		for ( int i=0;i<input.length;i++)
		{
			if (input[i] <0) 
			{
				output[0]++;
			}
			else if ( input[i] ==0)
			{
				output[1]++;
			}
			else output[2]++;
		}
		;
		return output;

	}
	/******************************************************
	 * 
	 * @param positiveFile
	 * @param negativeFile
	 */
	public CompareNormalWordList(String positiveFile, String negativeFile,String targetFile) {
		positive=objectForCreateWordList.readCSV(positiveFile);
		negative=objectForCreateWordList.readCSV(negativeFile);
		this.initiate(targetFile);
		tweets=this.readFile(targetFile);
		this.calculateSentiment();
		int[] arrayForGraph = this.convert();
		this.displayGraph(arrayForGraph,"Sentiment With Normal Word List");	
		objectBarChart.plotPie(this.barChartData(arrayForGraph),"Chart Normal.jpg");

	}
	/*********************************************************************************
	 * 
	 * @param hist
	 * @param name
	 */
	public void displayGraph(int[] hist,String name){
		XYSeries series1   = new XYSeries("Sentiment Graph");
		for(int i=0;i<hist.length;i++){
			series1.add(i, hist[i]);
		}



		XYSeriesCollection      xyDataset = new XYSeriesCollection();
		xyDataset.addSeries(series1);


		JFreeChart              chart     = ChartFactory.createXYLineChart(name,"Number of Tweets","Sentiment Score",xyDataset,PlotOrientation.VERTICAL,true,false,false);
		chart.setBackgroundPaint(Color.white); 

		XYPlot                  plot      = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint       (Color.white);
		plot.setDomainGridlinePaint   (Color.GREEN);
		plot.setRangeGridlinePaint    (Color.blue);
		plot.setAxisOffset            (new RectangleInsets(50, 0, 20, 5));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible (true);

		XYLineAndShapeRenderer  renderer  = (XYLineAndShapeRenderer) plot.getRenderer();      
		renderer.setBaseShapesVisible(true);
		renderer.setBaseShapesFilled (true);

		ChartFrame              frame     = new ChartFrame("ChartFrame", chart);
		frame.setSize   (500,500);
		frame.setVisible(true);

	}
/**********************************************************************
 * 	
 * @param hist
 * @param name
 */
	public void displayGraph1(int[] hist,String name){
		XYSeries series1   = new XYSeries("Sentiment Graph");
		for(int i=0;i<hist.length;i++){
			series1.add(i, hist[i]);
		}



		XYSeriesCollection      xyDataset = new XYSeriesCollection();
		xyDataset.addSeries(series1);


		JFreeChart              chart     = ChartFactory.createXYLineChart(name,"Frequency","Number of Words",xyDataset,PlotOrientation.VERTICAL,true,false,false);
		chart.setBackgroundPaint(Color.white); 

		XYPlot                  plot      = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint       (Color.white);
		plot.setDomainGridlinePaint   (Color.GREEN);
		plot.setRangeGridlinePaint    (Color.blue);
		plot.setAxisOffset            (new RectangleInsets(50, 0, 20, 5));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible (true);

		XYLineAndShapeRenderer  renderer  = (XYLineAndShapeRenderer) plot.getRenderer();      
		renderer.setBaseShapesVisible(true);
		renderer.setBaseShapesFilled (true);

		ChartFrame              frame     = new ChartFrame("ChartFrame", chart);
		frame.setSize   (500,500);
		frame.setVisible(true);

	}

	/************************************************************
	 * 
	 */
	public CompareNormalWordList(){

	}
	/********************************************************************************
	 * Compares a given word with all the word list available and adds to the count of
	 * the respective word-list depending on the words matched
	 * @param fileName
	 */
	public void compare(String fileName){


		List<String> tokenizeInput = objectForCovTokenizer.tokenizeInput(fileName);
		for (int i = 0; i < tokenizeInput.size(); i++) {
			String log = tokenizeInput.get(i);
			if(positive.containsKey(log)){
				positive.put(log,positive.get(log)+1);
			}
			if(negative.containsKey(log)){
				negative.put(log,negative.get(log)+1);
			}

		}

	}
	/*****************************************************************
	 * 
	 * @param fileName
	 */
	public void initiate(String fileName){
		 int posThre=0; int negThre=0;
		this.compare(fileName);
		int maxValueInMappositive=(Collections.max(positive.values()));
		//System.out.println(maxValueInMappositive);
		
		int maxValueInMapNegative=(Collections.max(negative.values()));
		//System.out.println(maxValueInMapNegative);
		
		int[] tempPos = this.convertPosNeg(positive);
		Arrays.sort(tempPos);
		this.displayGraph1(tempPos, "Postive Word Frequency");
		
		int[] tempNeg = this.convertPosNeg(negative);
		Arrays.sort(tempNeg);
		this.displayGraph1(tempNeg, "Negative Word Frequency");
		
		
		Scanner kbd = new Scanner(System.in);
		System.out.println("Please enter threshold fo postive words");
		posThre = kbd.nextInt();
		System.out.println("Please enter threshold fo negative words");
		negThre = kbd.nextInt();
		
		//		System.out.println(positive);
		maxValueInMappositive = Math.abs(posThre);
		for (Map.Entry entry : positive.entrySet()) { 
			int temp = (int) entry.getValue();
			if(temp<maxValueInMappositive){
				positive.put((String) entry.getKey(), 2);
			}
			else{
				positive.put((String) entry.getKey(), 1);
			}
		}

		
		maxValueInMapNegative = Math.abs(negThre);
		for (Map.Entry entry : negative.entrySet()) { 
			int temp = (int) entry.getValue();
			if(temp<maxValueInMapNegative){
				negative.put((String) entry.getKey(), -2);
			}
			else{
				negative.put((String) entry.getKey(), -1);
			}
		}
	}

	/********************************************************
	 * 
	 * @param srcFile
	 * @return
	 */
	public HashMap<String, Integer> readFile(String srcFile){
		BufferedReader br = null;
		HashMap<String, Integer> output = new HashMap<>();
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(srcFile));

			while ((sCurrentLine = br.readLine()) != null) {
				output.put(sCurrentLine, 0);
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();

			}
		}
		return output;
	}

	/*******************************************************************
	 * 
	 * @param input
	 */
	public int getSentimentScore(String input){

		int output=0;
		List<String> tokenizeInput = Tokenizer.Tokenize(input);
		for (int i = 0; i < tokenizeInput.size(); i++) {
			String log = tokenizeInput.get(i);
			if(positive.containsKey(log)){
				output+=positive.get(log);
			}
			if(negative.containsKey(log)){
				output+=negative.get(log);
			}

		}
		return output;
	}

	/******************************************************************
	 * 
	 */
	public void calculateSentiment(){
		for (Map.Entry entry : tweets.entrySet()) { 
			String tweet = (String)entry.getKey();
			tweets.put(tweet, this.getSentimentScore(tweet));

		}
	}


	/*******************************************************************
	 * 
	 * @param objectArray
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
	
	/*************************************************
	 * 
	 * @return
	 */
	public int[] convertPosNeg(HashMap<String, Integer> input){
		int[] intArray = new int[input.size()];
		int i =0;
		for (Map.Entry entry : input.entrySet()) { 
			//String tweet = (String)entry.getKey();
			//tweets.put(tweet, this.getSentimentScore(tweet));
			intArray[i] = (int) entry.getValue();
			//System.out.println(intArray[i]);
			i++;

		}

		return intArray;
	}
	/***************************************************************************
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		CompareNormalWordList obj1 = new CompareNormalWordList("wordlist\\LoughranMcDonald_Positive.csv","wordlist\\LoughranMcDonald_Negative.csv","tweets.txt");
		


		//System.out.println(tweets);

		//		int maxValueInMappositive=(Collections.max(positive.values()));
		//		int maxValueInMapNegative=(Collections.max(negative.values()));
		//		System.out.println(positive+"\n"+maxValueInMappositive);
		//		System.out.println(negative+"\n"+maxValueInMapNegative);
		//		System.out.println(positive.get("aaaa"));

	}
}
