import org.ejml.simple.SimpleMatrix;

public class MatrixTrig {

	public static SimpleMatrix sin(SimpleMatrix inputMatrix){
		for (int i=0;i<inputMatrix.numRows();i++){
			for (int j=0;j<inputMatrix.numCols();j++){
				double newValue=inputMatrix.get(i, j);
				newValue=Math.sin(newValue);
				inputMatrix.set(i,j,newValue);
			}
		}
		return inputMatrix;
	}
	public static SimpleMatrix cos(SimpleMatrix inputMatrix){
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
