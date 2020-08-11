package TesterFiles;
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

public class LineTest4 extends ApplicationFrame {

   public LineTest4( String applicationTitle, String chartTitle, XYSeriesCollection dataset ) {
      super(applicationTitle);

      JFreeChart xylineChart = ChartFactory.createXYLineChart(
    	         chartTitle ,
    	         "X" ,
    	         "Y" ,
    	         dataset,
    	         PlotOrientation.VERTICAL ,
    	         true , true , false);
      
      ChartPanel chartPanel = new ChartPanel( xylineChart );
      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
      final XYPlot plot = xylineChart.getXYPlot( );
      
      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
      renderer.setSeriesPaint( 0 , Color.RED );
      renderer.setSeriesPaint( 1 , Color.GREEN );

      renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
      renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );

      plot.setRenderer( renderer ); 
      setContentPane( chartPanel ); 
   }

   private static XYSeriesCollection addLine(XYSeriesCollection oldDataset,ArrayList<Double> xList1,ArrayList<Double> yList1, String lineName ){
	  final XYSeries Line1 = new XYSeries( lineName );          
      for (int i=0;i<xList1.size();i++){
    	  Line1.add( xList1.get(i) , yList1.get(i));   
      }      
      oldDataset.addSeries( Line1 );  
      return oldDataset;
   }

   public static void main( String[ ] args ) {
      ArrayList<Double> samplex1=new ArrayList<Double>();
      ArrayList<Double> samplex2=new ArrayList<Double>();
      ArrayList<Double> sampley1=new ArrayList<Double>();
      ArrayList<Double> sampley2=new ArrayList<Double>();
      for(int i=0; i<=20;i++){
    	  samplex1.add((double) i);
    	  sampley1.add((double) i);
    	  if (i<=10){
    		  samplex2.add((double) i);
    	  }
    	  if (i%2==0){
    		  sampley2.add((double) i);
    	  }
      }
      
      XYSeriesCollection dataset = new XYSeriesCollection( );
      dataset=addLine(dataset,samplex1,sampley1, "Line1");
      dataset=addLine(dataset,samplex2,sampley2, "Line2");
      LineTest4 chart = new LineTest4("XY",
         "XY", dataset);
      chart.pack( );                  
      chart.setVisible( true ); 
   }
}