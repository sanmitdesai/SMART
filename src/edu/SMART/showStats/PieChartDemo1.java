package edu.SMART.showStats;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration application showing how to create a pie chart using 
 * data from a {@link DefaultPieDataset}.
 */
public class PieChartDemo1 extends ApplicationFrame {

    /**
     * Default constructor.
     *
     * @param title  the frame title.
     */
    public PieChartDemo1(String title,ArrayList<String> input,String tweet) {
        super(title);
        setContentPane(createDemoPanel(input,tweet));
    }

    /**
     * Creates a sample dataset.
     * 
     * @return A sample dataset.
     */
    private static PieDataset createDataset(ArrayList<String> input) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Joy", Integer.parseInt(input.get(1)));
        dataset.setValue("Trust", Integer.parseInt(input.get(2)));
        dataset.setValue("Fear", Integer.parseInt(input.get(3)));
        dataset.setValue("Surprise", Integer.parseInt(input.get(4)));
        dataset.setValue("Sadness", Integer.parseInt(input.get(5)));
        dataset.setValue("Disgust", Integer.parseInt(input.get(6)));
        dataset.setValue("Anger", Integer.parseInt(input.get(7)));
        dataset.setValue("Anticipation", Integer.parseInt(input.get(8)));
//        System.out.println("here");
        return dataset;        
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    private static JFreeChart createChart(PieDataset dataset, String tweet) {
        
        JFreeChart chart = ChartFactory.createPieChart(
            tweet,  // chart title
            dataset,             // data
            true,               // include legend
            true,
            false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;
        
    }
    
    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
    public static JPanel createDemoPanel(ArrayList<String> input, String tweet) {
        JFreeChart chart = createChart(createDataset(input),tweet);
        return new ChartPanel(chart);
    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {
    	/*ArrayList<String> input;
//        PieChartDemo1 demo = new PieChartDemo1("Pie Chart Demo 1",input);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
*/
    }

}