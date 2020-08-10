import java.util.ArrayList;
import java.util.Random;

import org.jfree.data.xy.XYSeriesCollection;

public class GraphLibraryTest {
	private static ArrayList<Double> normalNoise(double mean, double stdev, ArrayList<Double> oldList){
		for (int i=0; i<oldList.size();i++){
			Random r = new Random();
			oldList.set(i, oldList.get(i)+(r.nextGaussian()*stdev+mean));
		}
		return oldList;
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
		    //dataset=GraphLibrary.addLine(dataset,samplex2,normalNoise(0,2.0,sampley2), "Line2");
		      XYSeriesCollection dataset = new XYSeriesCollection( );
		      dataset=GraphLibrary.addLine(dataset,samplex1,sampley1, "Line1");
		      dataset=GraphLibrary.addLine(dataset,samplex2,sampley2, "Line2");
		      GraphLibrary chart = new GraphLibrary("XY",
		         "XY", dataset);
		      chart.pack( );                  
		      chart.setVisible( true ); 
		   }
}
