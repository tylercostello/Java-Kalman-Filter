import java.util.ArrayList;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;
import org.jfree.data.xy.XYSeriesCollection;

public class EKF {
	public static SimpleMatrix x;
	public static SimpleMatrix P;
	public static SimpleMatrix Q;
	public static SimpleMatrix H;
	public static SimpleMatrix R;
	public static SimpleMatrix I;
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
		P = new SimpleMatrix(5, 5, true, new double[] { 0.01, 0, 0, 0, 0, 0, 0.01, 0, 0, 0, 0, 0, 0.01, 0, 0, 0, 0, 0,
				0.01, 0, 0, 0, 0, 0, 0.01 });
		H = new SimpleMatrix(3, 5, true, new double[] { 0, 0, 1.0, 0, 0, 0, 0, 0, 1.0, 0, 0, 0, 0, 0, 1.0 });
		I = SimpleMatrix.identity(5);

		SimpleMatrix z_sensors = new SimpleMatrix(3, 1);

		Q = new SimpleMatrix(5, 5);

		R = SimpleMatrix.diag(0.25, 0.25, 0.25);

		ArrayList<Double> xList = new ArrayList<Double>();
		xList.add(xTruth.get(0));
		ArrayList<Double> yList = new ArrayList<Double>();
		yList.add(yTruth.get(0));
		ArrayList<Double> thetaList = new ArrayList<Double>();
		thetaList.add(thetaTruth.get(0));
		ArrayList<Double> vlList = new ArrayList<Double>();
		vlList.add(vlTruth.get(0));
		ArrayList<Double> vrList = new ArrayList<Double>();
		vrList.add(vrTruth.get(0));

		for (int counter = 1; counter < 1400; counter++) {
			xList.add(x.get(0,0));
			yList.add(x.get(1,0));
			thetaList.add(x.get(2,0));
			vlList.add(x.get(2,0));
			vrList.add(x.get(2,0));
			z_sensors.set(0,0,thetaNoisy.get(counter));
			z_sensors.set(1,0, vlNoisy.get(counter));
			z_sensors.set(2,0, vrNoisy.get(counter));
			predict();
			update(z_sensors);
		}
		
		 XYSeriesCollection dataset = new XYSeriesCollection( );
		 dataset=GraphLibrary.addLine(dataset,xList,yList, "Line1");
		 dataset=GraphLibrary.addLine(dataset,xTruth,yTruth, "Line2");
		 GraphLibrary chart = new GraphLibrary("XY", "XY", dataset);
		 chart.pack( ); chart.setVisible( true );
		 		

	}

	private static void predict() {
		Q.set(2, 0, 0);
		Q.set(2, 1, 0);
		Q.set(2, 2, (dt_4 * alvariance + dt_4 * arvariance) / (4 * b * b));
		Q.set(2, 3, (-dt_3 * alvariance) / (2 * b));
		Q.set(2, 4, (dt_3 * arvariance) / (2 * b));

		Q.set(3, 0, 0);
		Q.set(3, 1, 0);
		Q.set(3, 2, (-dt_3 * alvariance) / (2 * b));
		Q.set(3, 3, dt_2 * alvariance);
		Q.set(3, 4, 0);

		Q.set(4, 0, 0);
		Q.set(4, 1, 0);
		Q.set(4, 2, (dt_3 * arvariance) / (2 * b));
		Q.set(4, 3, 0);
		Q.set(4, 4, dt_2 * arvariance);

		SimpleMatrix A = new SimpleMatrix(5, 5);

		if (x.get(3, 0) == x.get(4, 0)) {
			A.set(0, 0, 1);
			A.set(0, 1, 0);
			A.set(0, 2, -x.get(3, 0) * dt * Math.sin(x.get(2, 0)));
			A.set(0, 3, dt * Math.cos(x.get(2, 0)));
			A.set(0, 4, 0);

			A.set(1, 0, 0);
			A.set(1, 1, 1);
			A.set(1, 2, dt * x.get(3, 0) * Math.cos(x.get(2, 0)));
			A.set(1, 3, dt * Math.sin(x.get(2, 0)));
			A.set(1, 4, 0);

			A.set(2, 0, 0);
			A.set(2, 0, 0);
			A.set(2, 2, 1);
			A.set(2, 3, 0);
			A.set(2, 4, 0);

			A.set(3, 0, 0);
			A.set(3, 1, 0);
			A.set(3, 2, 0);
			A.set(3, 3, 1);
			A.set(3, 4, 0);

			A.set(4, 0, 0);
			A.set(4, 1, 0);
			A.set(4, 2, 0);
			A.set(4, 3, 0);
			A.set(4, 4, 1);
		} else {
			A.set(0, 0, 1);
			A.set(0, 1, 0);
			A.set(0, 2,
					(b * (x.get(4, 0) + x.get(3, 0))
							* (Math.cos(x.get(2, 0) + dt * (x.get(4, 0) - x.get(3, 0)) / b) - Math.cos(x.get(2, 0))))
							/ (2 * (x.get(4, 0) - x.get(3, 0))));
			A.set(0, 3,
					(-x.get(4, 0) * x.get(4, 0) * dt * Math.cos(x.get(2, 0) + dt * (x.get(4, 0) - x.get(3, 0)) / b)
							+ 2 * b * x.get(4, 0) * Math.sin(x.get(2, 0) + dt * (x.get(4, 0) - x.get(3, 0)) / b)
							- 2 * b * x.get(4, 0) * Math.sin(x.get(2, 0))
							+ x.get(3, 0) * x.get(3, 0) * dt
									* Math.cos(x.get(2, 0) + dt * (x.get(4, 0) - x.get(3, 0)) / b))
							/ (2 * (x.get(4, 0) - x.get(3, 0)) * (x.get(4, 0) - x.get(3, 0))));
			A.set(0, 4, (2 * x.get(3, 0) * b * Math.sin(x.get(2, 0))
					- 2 * x.get(3, 0) * b * Math.sin(x.get(2, 0) + dt * (x.get(4, 0) - x.get(3, 0)) / b)
					+ x.get(4, 0) * x.get(4, 0) * dt * Math.cos(x.get(2, 0) + dt * (x.get(4, 0) - x.get(3, 0)) / b)
					- x.get(3, 0) * x.get(3, 0) * dt * Math.cos(x.get(2, 0) + dt * (x.get(4, 0) - x.get(3, 0)) / b))
					/ (2 * (x.get(4, 0) - x.get(3, 0)) * (x.get(4, 0) - x.get(3, 0))));

			A.set(1, 0, 0);
			A.set(1, 1, 1);
			A.set(1, 2,
					(b * (x.get(4, 0) + x.get(3, 0))
							* (Math.sin(x.get(2, 0) + dt * (x.get(4, 0) - x.get(3, 0)) / b) - Math.sin(x.get(2, 0))))
							/ (2 * (x.get(4, 0) - x.get(3, 0))));
			A.set(1, 3,
					(x.get(3, 0) * x.get(3, 0) * dt * Math.cos(dt * (x.get(4, 0) - x.get(3, 0)) / b)
							* Math.sin(x.get(2, 0))
							+ x.get(3, 0) * x.get(3, 0) * dt * Math.sin(dt * (x.get(4, 0) - x.get(3, 0)) / b)
									* Math.cos(x.get(2, 0))
							+ 2 * b * x.get(4, 0) * Math.sin(dt * (x.get(4, 0) - x.get(3, 0)) / b)
									* Math.sin(x.get(2, 0))
							+ 2 * b * x.get(4, 0) * Math.cos(x.get(2, 0))
							- x.get(4, 0) * x.get(4, 0) * dt * Math.cos(dt * (x.get(4, 0) - x.get(3, 0)) / b)
									* Math.sin(x.get(2, 0))
							- x.get(4, 0) * x.get(4, 0) * dt * Math.sin(dt * (x.get(4, 0) - x.get(3, 0)) / b)
									* Math.cos(x.get(2, 0))
							- 2 * b * x.get(4, 0) * Math.cos(dt * (x.get(4, 0) - x.get(3, 0)) / b)
									* Math.cos(x.get(2, 0)))
							/ (2 * (x.get(4, 0) - x.get(3, 0)) * (x.get(4, 0) - x.get(3, 0))));
			A.set(1, 4,
					(x.get(4, 0) * x.get(4, 0) * dt * Math.cos(dt * (x.get(4, 0) - x.get(3, 0)) / b)
							* Math.sin(x.get(2, 0))
							+ x.get(4, 0) * x.get(4, 0) * dt * Math.sin(dt * (x.get(4, 0) - x.get(3, 0)) / b)
									* Math.cos(x.get(2, 0))
							+ 2 * b * x.get(3, 0) * Math.cos(dt * (x.get(4, 0) - x.get(3, 0)) / b)
									* Math.cos(x.get(2, 0))
							- x.get(3, 0) * x.get(3, 0) * dt * Math.cos(dt * (x.get(4, 0) - x.get(3, 0)) / b)
									* Math.sin(x.get(2, 0))
							- 2 * b * x.get(3, 0) * Math.sin(dt * (x.get(4, 0) - x.get(3, 0)) / b)
									* Math.sin(x.get(2, 0))
							- x.get(3, 0) * x.get(3, 0) * dt * Math.sin(dt * (x.get(4, 0) - x.get(3, 0)) / b)
									* Math.cos(x.get(2, 0))
							- 2 * b * x.get(3, 0) * Math.cos(x.get(2, 0)))
							/ (2 * (x.get(4, 0) - x.get(3, 0)) * (x.get(4, 0) - x.get(3, 0))));

			A.set(2, 0, 0);
			A.set(2, 1, 0);
			A.set(2, 2, 1);
			A.set(2, 3, -dt / b);
			A.set(2, 4, dt / b);

			A.set(3, 0, 0);
			A.set(3, 1, 0);
			A.set(3, 2, 0);
			A.set(3, 3, 1);
			A.set(3, 4, 0);

			A.set(4, 0, 0);
			A.set(4, 1, 0);
			A.set(4, 2, 0);
			A.set(4, 3, 0);
			A.set(4, 4, 1);

		}
		x = aFunction(x, b, dt);
		SimpleMatrix At = A.transpose();
		//P.print();
		//At.print();
		//P = P.mult(At);
		P = (A.mult(P.mult(At))).plus(Q);

	}

	private static void update(SimpleMatrix z) {
		//H.print();
		//x.print();
		SimpleMatrix Y = z.minus(H.mult(x));
		SimpleMatrix Ht = H.transpose();
		SimpleMatrix S = (H.mult(P.mult(Ht))).plus(R);
		SimpleMatrix K = P.mult(Ht);
		SimpleMatrix Si = S.invert();
		K = K.mult(Si);
		x = x.plus(K.mult(Y));
		P = (I.minus(K.mult(H))).mult(P);
	}

}
