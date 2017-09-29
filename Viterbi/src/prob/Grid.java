package prob;

import java.util.*;

public class Grid {
	
	Vertex[][] grid;
	int ySize;
	int xSize;
	int numBlocked = 0;
	public String actions = "";
	public String observations = "";
	ArrayList<Coordinate> path = new ArrayList<Coordinate>();
	
	public Grid(int x, int y) {
		grid = new Vertex[y][x];
		ySize = y;
		xSize = x;
		Random generator = new Random();
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				int decider = generator.nextInt(10);
				grid[i][j] = new Vertex();
				if (decider <= 4) {
					grid[i][j].type = 'N';
				}
				else if (decider <= 6) {
					grid[i][j].type = 'H';
				}
				else if (decider <= 8) {
					grid[i][j].type = 'T';
				}
				else {
					grid[i][j].type = 'B';
					numBlocked++;
				}
				grid[i][j].prob = 0;
			}
		}
	}
	
	public Grid() {
		grid = new Vertex[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				grid[i][j] = new Vertex();
			}
		}
		ySize = 3;
		xSize = 3;
		grid[0][0].type = 'H';
		grid[0][1].type = 'H';
		grid[0][2].type = 'T';
		grid[1][0].type = 'N';
		grid[1][1].type = 'N';
		grid[1][2].type = 'N';
		grid[2][0].type = 'N';
		grid[2][1].type = 'B';
		grid[2][2].type = 'H';
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				grid[i][j].prob = 0;
			}
		}
		actions = "RRDD";
		observations = "NNHH";
		numBlocked = 1;
	}
	
	public void initializeGroundTruths() {
		
	}
}
