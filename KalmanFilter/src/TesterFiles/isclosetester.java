package TesterFiles;

public class isclosetester {
	private static boolean isClose(double in1,double in2){
		if (Math.abs(in1-in2)<=0.001){
			return true;
		}
		return false;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(isClose(1.001,1.000));
	}

}
