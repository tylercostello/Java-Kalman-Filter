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
		}

		double theta = Math.PI / 2;

		ArrayList<Double> vrTruth = new ArrayList<Double>();
		for (double i : t) {
			vrTruth.add(20 * (Math.sin(i) + 10) - 100);
		}

//		 ArrayList<Double> vrTruth = new ArrayList<Double>();
//		 for (int i = 0; i <= 1400; i++) {
//		 vrTruth.add((double) 75);
//		 }

		ArrayList<Double> vlTruth = new ArrayList<Double>();
		for (int i = 0; i <= 1400; i++) {
			vlTruth.add((double) 110);
		}

		double vl = vlTruth.get(0);
		double vr = vrTruth.get(0);

		double xStart = 0;
		double yStart = 0;
		
		ArrayList<Double> xTruth = new ArrayList<Double>();
		ArrayList<Double> yTruth = new ArrayList<Double>();
		ArrayList<Double> thetaTruth = new ArrayList<Double>();
		xTruth.add(xStart);
		yTruth.add(yStart);
		thetaTruth.add(theta);
		double xNum=xStart;
		double yNum=yStart;
		double vlNum;
		double vrNum;
		for (int counter = 1; counter <= 1400; counter++) {
			vlNum=vlTruth.get(counter);
			vrNum=vrTruth.get(counter);
			if (isClose(vlNum, vrNum)) {
				xNum = xNum + vlNum * Math.cos(theta) * 0.01;
				yNum = yNum + vlNum * Math.sin(theta) * 0.01;
				theta = theta;
				xTruth.add(xNum);
				yTruth.add(yNum);
				thetaTruth.add(theta);

			} else {
				double w = (vrNum - vlNum) / 10;
				double r = (10 / 2) * (vlNum + vrNum) / (vrNum - vlNum);
				xNum = r * Math.sin(theta) * Math.cos(w * 0.01) + r * Math.cos(theta) * Math.sin(w * 0.01) + xNum
						- r * Math.sin(theta);
				yNum = r * Math.sin(theta) * Math.sin(w * 0.01) - r * Math.cos(theta) * Math.cos(w * 0.01) + yNum
						+ r * Math.cos(theta);
				theta = theta + w * 0.01;
				xTruth.add(xNum);
				yTruth.add(yNum);
				thetaTruth.add(theta);
			}
		}

		ArrayList<Double> vlNoisy = normalNoise(0, 0.5, vlTruth);
		ArrayList<Double> vrNoisy = normalNoise(0, 0.5, vrTruth);
		ArrayList<Double> thetaNoisy = normalNoise(0, 0.5, thetaTruth);

//                   This is the code we would have running where sensor inputs are collected on the actual robot
//-----------------------------------------------------------
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
			inputArray[0] = thetaNoisy.get(counter);
			inputArray[1] = vlNoisy.get(counter);
			inputArray[2] = vrNoisy.get(counter);
			double[] outputArray = myEKF.runFilter(inputArray);
			xList.add(outputArray[0]);
			yList.add(outputArray[1]);
			thetaList.add(outputArray[2]);
			vlList.add(outputArray[3]);
			vrList.add(outputArray[4]);

		}

		
//-------------------------------------------------------------------
		
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset = GraphLibrary.addLine(dataset, xList, yList, "Estimate");
		dataset = GraphLibrary.addLine(dataset, xTruth, yTruth, "Truth");

		GraphLibrary chart = new GraphLibrary("XY", "XY", dataset);
		chart.pack();
		chart.setVisible(true);
	}

}
