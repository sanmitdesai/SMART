package edu.SMART.Main;

import org.jfree.ui.RefineryUtilities;

import edu.SMART.sentimentAnalyzer.CompareAffinWordList;
import edu.SMART.sentimentAnalyzer.EmotionAnalyzer;
import edu.SMART.showStats.PieChartDemo1;

public class Main {

	public static void main(String[] args) {
//		CompareAffinWordList obj = new CompareAffinWordList("wordlist\\AFINN-111.txt","data\\tweets.txt");
		EmotionAnalyzer objEmotionAnalyzer = new EmotionAnalyzer();
		String input = "Tense standoff between obama and the senate isn't good";
		System.out.println(objEmotionAnalyzer.analysis(input));
		PieChartDemo1 objPieChartDemo1 = new PieChartDemo1("Emotion Pie Chart",objEmotionAnalyzer.analysis(input),input);
		objPieChartDemo1.pack();
		RefineryUtilities.centerFrameOnScreen(objPieChartDemo1);
		objPieChartDemo1.setVisible(true);
	}

}
