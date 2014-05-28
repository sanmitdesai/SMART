package edu.SMART.showStats;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.data.general.DefaultPieDataset;
import java.io.File;

public class Barchart {
	public DisplayImage obj = new DisplayImage();
	public void plotPie(int[] input, String path){
		// Create a simple pie chart
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		pieDataset.setValue("Negative", new Integer(input[0]));
		pieDataset.setValue("Neutral", new Integer(input[1]));
		pieDataset.setValue("Positive", new Integer(input[2]));
		
		JFreeChart chart = ChartFactory.createPieChart
				("Sentiment Distribution", // Title
						pieDataset, // Dataset
						true, // Show legend
						true, // Use tooltips
						false // Configure chart to generate URLs?
						);
		try {
			ChartUtilities.saveChartAsJPEG(new File(path), chart, 500, 300);
			obj.showImage(path);
		} catch (Exception e) {
			System.out.println("Problem occurred creating chart.");
		}

	}
	public static void main(String[] args) 
	{
	}
}