import java.awt.Color;

import java.util.ArrayList;
import java.awt.BasicStroke;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;

import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;

public class GraphLibrary extends ApplicationFrame {

	public GraphLibrary(String applicationTitle, String chartTitle, XYSeriesCollection dataset) {
		super(applicationTitle);

		JFreeChart xylineChart = ChartFactory.createScatterPlot(chartTitle, "X", "Y", dataset, PlotOrientation.VERTICAL,
				true, true, false);

		ChartPanel chartPanel = new ChartPanel(xylineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
		final XYPlot plot = xylineChart.getXYPlot();

		ChartPanel panel = new ChartPanel(xylineChart);
		setContentPane(panel);

	}

	public static XYSeriesCollection addLine(XYSeriesCollection oldDataset, ArrayList<Double> xList1,
			ArrayList<Double> yList1, String lineName) {
		final XYSeries Line1 = new XYSeries(lineName);
		for (int i = 0; i < xList1.size(); i++) {
			Line1.add(xList1.get(i), yList1.get(i));
		}
		oldDataset.addSeries(Line1);
		return oldDataset;
	}
}