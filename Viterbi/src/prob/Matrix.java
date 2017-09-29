package prob;

public class Matrix{
	
	float[][] matrix;
	int xSize;
	int ySize;
	
	public Matrix(int x, int y) {
		matrix = new float[y][x];
		ySize = y;
		xSize = x;
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				matrix[i][j] = 0;
			}
		}
	}
	
	public Matrix(int x, int y, Grid g) {
		matrix = new float[y][x];
		ySize = y;
		xSize = x;
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				if (g.grid[i][j].type != 'B') {
					matrix[i][j] = (float) ((1.0) / ((x * y) - g.numBlocked));
				}
				else {
					matrix[i][j] = 0;
				}
			}
		}
	}
	
	public Matrix(int x, int y, Grid g, int z) {
		matrix = new float[y][x];
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				if (g.grid[i][j].type != 'B') {
					matrix[i][j] = (float) 1.0;
				}
				else {
					matrix[i][j] = 0;
				}
			}
		}
	}
	
	public void printMatrix(Matrix m) {
		for (int i = 0; i < m.ySize; i++) {
			for (int j = 0; j < m.xSize; j++) {
				System.out.print(m.matrix[i][j] + ", ");
			}
			System.out.println();
		}
	}
}
