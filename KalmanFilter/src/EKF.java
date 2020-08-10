import java.util.ArrayList;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;
import org.jfree.data.xy.XYSeriesCollection;

public class EKF {
	public static SimpleMatrix x;
	public static SimpleMatrix P;
	public static SimpleMatrix Q;
	public final static int alvariance = 100;
	public final static int arvariance = 100;

	
	public final static int b = 10;
	public final static double dt = 0.01;
	public final static double dt_2 = dt * dt;
	public final static double dt_3 = dt_2 * dt;
	public final static double dt_4 = dt_3 * dt;
	
	
	private static ArrayList<Double> normalNoise(double mean, double stdev, ArrayList<Double> oldList) {
		ArrayList<Double> newList = new ArrayList<Double>();

		for (int i = 0; i < oldList.size(); i++) {
			Random r = new Random();
			newList.add(oldList.get(i) + (r.nextGaussian() * stdev + mean));
		}
		return newList;
	}

	private static SimpleMatrix aFunction(SimpleMatrix xInput, int b, double dt) {
		SimpleMatrix xOutput = new SimpleMatrix(5, 1);
		double xNum = xInput.get(0, 0);
		double y = xInput.get(1, 0);
		double theta = xInput.get(2, 0);
		double vl = xInput.get(3, 0);
		double vr = xInput.get(4, 0);

		if (vr == vl) {
			xNum = xNum + vl * Math.cos(theta) * dt;
			y = y + vl * Math.sin(theta) * dt;
			theta = theta;
		} else {
			double w = (vr - vl) / b;
			double r = (b / 2) * (vl + vr) / (vr - vl);
			xNum = r * Math.sin(theta) * Math.cos(w * dt) + r * Math.cos(theta) * Math.sin(w * dt) + xNum
					- r * Math.sin(theta);
			y = r * Math.sin(theta) * Math.sin(w * dt) - r * Math.cos(theta) * Math.cos(w * dt) + y
					+ r * Math.cos(theta);
			theta = theta + w * dt;
		}
		xOutput.set(0, 0, xNum);
		xOutput.set(1, 0, y);
		xOutput.set(2, 0, theta);
		xOutput.set(3, 0, vl);
		xOutput.set(4, 0, vr);

		return xOutput;

	}

	public static void main(String[] args) {
		ArrayList<Double> t = new ArrayList<Double>();
		for (double i = 0; i <= 14.0; i = i + 0.01) {
			t.add(i);
		}
		double xNum = 0;
		double y = 0;
		double theta = Math.PI / 2;

		ArrayList<Double> xTruth = new ArrayList<Double>();
		xTruth.add(xNum);
		ArrayList<Double> yTruth = new ArrayList<Double>();
		yTruth.add(y);
		ArrayList<Double> thetaTruth = new ArrayList<Double>();
		thetaTruth.add(theta);
		ArrayList<Double> vrTruth = new ArrayList<Double>();
		for (double i : t) {
			vrTruth.add(20 * (Math.sin(i) + 10) - 100);
		}
		ArrayList<Double> vlTruth = new ArrayList<Double>();
		for (int i = 0; i <= 1400; i++) {
			vlTruth.add((double) 110);
		}
		double vl = vlTruth.get(0);
		double vr = vrTruth.get(0);

		for (int counter = 1; counter < 1400; counter++) {

			if (vl == vr) {
				xNum = xNum + vl * Math.cos(theta) * dt;
				y = y + vl * Math.sin(theta) * dt;
				theta = theta;
				xTruth.add(xNum);
				yTruth.add(y);
				thetaTruth.add(theta);
				vl = vlTruth.get(counter);
				vr = vrTruth.get(counter);

			} else {
				double w = (vr - vl) / b;
				double r = (b / 2) * (vl + vr) / (vr - vl);
				xNum = r * Math.sin(theta) * Math.cos(w * dt) + r * Math.cos(theta) * Math.sin(w * dt) + xNum
						- r * Math.sin(theta);
				y = r * Math.sin(theta) * Math.sin(w * dt) - r * Math.cos(theta) * Math.cos(w * dt) + y
						+ r * Math.cos(theta);
				theta = theta + w * dt;
				xTruth.add(xNum);
				yTruth.add(y);
				thetaTruth.add(theta);
				vl = vlTruth.get(counter);
				vr = vrTruth.get(counter);
			}

		}

		ArrayList<Double> vlNoisy = normalNoise(0, 0.5, vlTruth);
		ArrayList<Double> vrNoisy = normalNoise(0, 0.5, vrTruth);
		ArrayList<Double> thetaNoisy = normalNoise(0, 0.5, thetaTruth);

		/*
		 * XYSeriesCollection dataset = new XYSeriesCollection( );
		 * dataset=GraphLibrary.addLine(dataset,t,vlTruth, "Line1");
		 * dataset=GraphLibrary.addLine(dataset,t,vlNoisy, "Line2");
		 * GraphLibrary chart = new GraphLibrary("XY", "XY", dataset);
		 * chart.pack( ); chart.setVisible( true );
		 */

		double prev_time = 0;

		x = new SimpleMatrix(5, 1, true,
				new double[] { xTruth.get(0), yTruth.get(0), thetaTruth.get(0), vlTruth.get(0), vrTruth.get(0) });
		P = new SimpleMatrix(2, 3, true, new double[] { 0.01, 0, 0, 0, 0, 0, 0.01, 0, 0, 0, 0, 0, 0.01, 0,
				0, 0, 0, 0, 0.01, 0, 0, 0, 0, 0, 0.01 });
		SimpleMatrix H = new SimpleMatrix(2, 3, true,
				new double[] { 0, 0, 1.0, 0, 0, 0, 0, 0, 1.0, 0, 0, 0, 0, 0, 1.0 });
		SimpleMatrix I = SimpleMatrix.identity(5);

		SimpleMatrix z_sensors = new SimpleMatrix(3, 1);

		Q = new SimpleMatrix(5, 5);

		SimpleMatrix R = SimpleMatrix.diag(0.25, 0.25, 0.25);

		



	}
	private static void predict(){
		Q.set(2,0,0);
		Q.set(2,1,0);
		Q.set(2,2,(dt_4 * alvariance + dt_4 * arvariance) / (4 * b * b));
		Q.set(2,3,(-dt_3 * alvariance) / (2 * b));
		Q.set(2,4,(dt_3 * arvariance) / (2 * b));
		
		Q.set(3,0,0);
		Q.set(3,1,0);
		Q.set(3,2,(-dt_3 * alvariance) / (2 * b));
		Q.set(3,3,dt_2 * alvariance);
		Q.set(3,4,0);
		
		Q.set(4,0,0);
		Q.set(4,1,0);
		Q.set(4,2,(dt_3 * arvariance) / (2 * b));
		Q.set(4,3,0);
		Q.set(4,4, dt_2 * arvariance);
		
		SimpleMatrix A = new SimpleMatrix(5, 5);
		
		if(x.get(3,0)==x.get(4,0)){
			A.set(0,0,1);
			A.set(0,0,0);
			A.set(0,0,-x[3] * dt * np.sin(x[2]));
			A.set(0,0, dt * np.cos(x[2]));
			A.set(0,0,0);
			
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
		}
		else{
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
			A.set(0,0,0);
		}
		
		
		
		
	}

}
