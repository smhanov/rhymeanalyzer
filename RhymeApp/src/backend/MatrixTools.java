package backend;

//import Jama.*;

public class MatrixTools {
	
	/*public static Matrix center(Matrix m) {
		Matrix ret = new Matrix(m.getArray());
		double mean = m.normF();
		for (int i=0; i<m.getRowDimension(); i++) {
			for (int j=0; j<m.getColumnDimension(); j++) {
				ret.set(i, j, m.get(i, j)-mean);
			}
		}
		return ret;
	}*/
	
	public static int[] arrayAdd(int[] a, int[] b) {
		int[] ret = new int[a.length];
		if (a.length!=b.length) {
			return null;
		}
		for (int i=0; i<a.length; i++) {
			ret[i] = a[i]+b[i];
		}
		return ret;
	}
	
	public static int[][] arrayAdd(int[][] a, int[][] b) {
		int[][] ret = new int[a.length][a[0].length];
		if (a.length!=b.length || a[0].length!=b[0].length) {
			return null;
		}
		for (int i=0; i<a.length; i++) {
			for (int j=0; j<a[i].length; j++) {
				ret[i][j] = a[i][j]+b[i][j];
			}
		}
		return ret;
	}
	
	public static double[] arrayAdd(double[] a, double[] b) {
		double[] ret = new double[a.length];
		if (a.length!=b.length) {
			return null;
		}
		for (int i=0; i<a.length; i++) {
			ret[i] = a[i]+b[i];
		}
		return ret;
	}
	
	public static double[][] arrayAdd(double[][] a, double[][] b) {
		double[][] ret = new double[a.length][a[0].length];
		if (a.length!=b.length || a[0].length!=b[0].length) {
			return null;
		}
		for (int i=0; i<a.length; i++) {
			for (int j=0; j<a[i].length; j++) {
				ret[i][j] = a[i][j]+b[i][j];
			}
		}
		return ret;
	}

}
