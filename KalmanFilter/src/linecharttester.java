import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.category.DefaultCategoryDataset;

public class linecharttester extends ApplicationFrame {

   public linecharttester( String applicationTitle , String chartTitle ) {
      super(applicationTitle);
      JFreeChart lineChart = ChartFactory.createLineChart(
         chartTitle,
         "X","Y",
         createDataset(),
         PlotOrientation.VERTICAL,
         true,true,false);
         
      ChartPanel chartPanel = new ChartPanel( lineChart );
      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
      setContentPane( chartPanel );
   }

   private DefaultCategoryDataset createDataset( ) {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
      dataset.addValue( 1 , "y" , "1" );
      dataset.addValue( 2.25 , "y" , "2.35" );
      dataset.addValue( 3 , "y" ,  "3" );
      dataset.addValue( 4 , "y" , "4" );
      dataset.addValue( 5 , "y" , "5" );
      dataset.addValue( 6 , "y" , "6" );
      return dataset;
   }
   
   public static void main( String[ ] args ) {
      linecharttester chart = new linecharttester(
         "Y Vs X" ,
         "Y Vs X");

      chart.pack( );
     
      chart.setVisible( true );
   }
}