package TesterFiles;
import org.ejml.simple.SimpleMatrix;

public class MatrixTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleMatrix A = new SimpleMatrix(2,3,true,new double[]{1,2,3,
																4,5,6});
		SimpleMatrix B = new SimpleMatrix(3,2,true,new double[]{1,2,
																3,4,
																5,6});
		
		A.print();
		B.print();
		SimpleMatrix C = B.mult(A);
		C.print();
		
		A=sin(A);
		A.print();
		B=cos(B);
		B.print();
		
		
	}
	private static SimpleMatrix sin(SimpleMatrix inputMatrix){
		for (int i=0;i<inputMatrix.numRows();i++){
			for (int j=0;j<inputMatrix.numCols();j++){
				double newValue=inputMatrix.get(i, j);
				newValue=Math.sin(newValue);
				inputMatrix.set(i,j,newValue);
			}
		}
		return inputMatrix;
	}
	private static SimpleMatrix cos(SimpleMatrix inputMatrix){
		for (int i=0;i<inputMatrix.numRows();i++){
			for (int j=0;j<inputMatrix.numCols();j++){
				double newValue=inputMatrix.get(i, j);
				newValue=Math.cos(newValue);
				inputMatrix.set(i,j,newValue);
			}
		}
		return inputMatrix;
	}

}
