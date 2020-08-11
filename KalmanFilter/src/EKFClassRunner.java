import java.util.ArrayList;
import java.util.Random;

import org.jfree.data.xy.XYSeriesCollection;

public class EKFClassRunner {

	private static ArrayList<Double> normalNoise(double mean, double stdev, ArrayList<Double> oldList) {
		ArrayList<Double> newList = new ArrayList<Double>();
		Random r = new Random();
		r.setSeed(1);
		for (int i = 0; i < oldList.size(); i++) {
			newList.add(oldList.get(i) + (r.nextGaussian() * stdev + mean));
		}
		return newList;
	}

	private static boolean isClose(double in1, double in2) {
		if (Math.abs(in1 - in2) <= 0.001) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Double> t = new ArrayList<Double>();
		for (double i = 0; i <= 14.00; i = i + 0.01) {
			t.add(i);
			// System.out.println(i);
		}

		double theta = Math.PI / 2;

		ArrayList<Double> vrTruth = new ArrayList<Double>();
		for (double i : t) {
			vrTruth.add(20 * (Math.sin(i) + 10) - 100);
		}

		// ArrayList<Double> vrTruth = new ArrayList<Double>();
		// for (int i = 0; i <= 1400; i++) {
		// vrTruth.add((double) 75);
		// }

		ArrayList<Double> vlTruth = new ArrayList<Double>();
		for (int i = 0; i <= 1400; i++) {
			vlTruth.add((double) 110);
		}

		double vl = vlTruth.get(0);
		double vr = vrTruth.get(0);

		ArrayList<Double> thetaTruth = new ArrayList<Double>();
		thetaTruth.add(theta);
		for (int counter = 1; counter <= 1400; counter++) {
			double w = (vrTruth.get(counter) - vlTruth.get(counter)) / 10.0;
			theta = theta + w * 0.01;
			thetaTruth.add(theta);
		}

		ArrayList<Double> vlNoisy = normalNoise(0, 0.5, vlTruth);
		ArrayList<Double> vrNoisy = normalNoise(0, 0.5, vrTruth);
		ArrayList<Double> thetaNoisy = normalNoise(0, 0.5, thetaTruth);

		double xStart = 0;
		double yStart = 0;

		ArrayList<Double> xList = new ArrayList<Double>();
		xList.add(xStart);
		ArrayList<Double> yList = new ArrayList<Double>();
		yList.add(yStart);
		ArrayList<Double> thetaList = new ArrayList<Double>();
		thetaList.add(thetaTruth.get(0));
		ArrayList<Double> vlList = new ArrayList<Double>();
		vlList.add(vlTruth.get(0));
		ArrayList<Double> vrList = new ArrayList<Double>();
		vrList.add(vrTruth.get(0));
		EKFClass myEKF = new EKFClass(xStart, yStart, thetaTruth.get(0), vlTruth.get(0), vrTruth.get(0), 10, 10, 10,
				0.01);

		for (int counter = 1; counter <= 1400; counter++) {

			double[] inputArray = new double[3];
			inputArray[0] = thetaTruth.get(counter);
			inputArray[1] = vlNoisy.get(counter);
			inputArray[2] = vrNoisy.get(counter);
			double[] outputArray = myEKF.runFilter(inputArray);
			xList.add(outputArray[0]);
			yList.add(outputArray[1]);
			thetaList.add(outputArray[2]);
			vlList.add(outputArray[3]);
			vrList.add(outputArray[4]);

		}

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset = GraphLibrary.addLine(dataset, xList, yList, "Estimate");
		GraphLibrary chart = new GraphLibrary("XY", "XY", dataset);
		chart.pack();
		chart.setVisible(true);
	}

}
