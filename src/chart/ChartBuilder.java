package chart;

import database.ReactionsReader;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;


/**
 * This program demonstrates how to draw line chart with CategoryDataset using
 * JFreechart library.
 *
 * @author www.codejava.net
 *
 */
public class ChartBuilder extends JFrame {

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    String postId;

    public ChartBuilder(String postId) {
        super("Chart Reactions - " + postId);

        JPanel chartPanel = createChartPanel();
        add(chartPanel, BorderLayout.CENTER);

        setSize(960, 480);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        ReactionsReader rr = new ReactionsReader(postId);

        LinkedHashMap<String, LinkedHashMap> allReactions = rr.getAllReactionsCount();

        for (String key : allReactions.keySet()) {

            LinkedHashMap<String, Float> reactions = allReactions.get(key);

            buildDataset(reactions.get("loves"), "loves", key);
            buildDataset(reactions.get("wow"), "wow", key);
            buildDataset(reactions.get("haha"), "haha", key);
            buildDataset(reactions.get("likes"), "likes", key);
            buildDataset(reactions.get("sad"), "sad", key);
            buildDataset(reactions.get("angry"), "angry", key);
            buildDataset(reactions.get("shares"), "shares", key);

        }
    }

    private JPanel createChartPanel() {
        // creates a line chart object
        // returns the chart panel
        String chartTitle = "Reactions";
        String categoryAxisLabel = "Reactions over time";
        String valueAxisLabel = "Count";

        boolean showLegend = true;
        boolean createURL = true;
        boolean createTooltip = true;

        JFreeChart chart = ChartFactory.createLineChart(chartTitle,
                categoryAxisLabel, valueAxisLabel, dataset,
                PlotOrientation.VERTICAL, showLegend, createTooltip, createURL);
//        ChartFactory.createLi

        return new ChartPanel(chart);
    }

    private void buildDataset(float count, String serie, String reactionTime) {

        dataset.addValue(count, serie, reactionTime);

    }

    public static void drawWindow(String postId) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChartBuilder(postId).setVisible(true);
            }
        });

    }

}
