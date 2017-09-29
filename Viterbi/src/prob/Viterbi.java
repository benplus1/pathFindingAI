package prob;

import java.util.ArrayList;

public class Viterbi {
	private ArrayList<Matrix> states;
	public ArrayList<Matrix> priors;
	private ArrayList<Integer> path;
	private Grid grid;
	
	public Viterbi(Grid g){
		grid = g;
		Matrix m = new Matrix(g.xSize, g.ySize, g);
		states = new ArrayList<Matrix>();
		priors = new ArrayList<Matrix>();
		states.add(m);
	}
	
	public Matrix computeViterbi(int iterations){
		for(int i = 0; i < iterations; i++){
			Matrix m = new Matrix(grid.xSize, grid.ySize);
			Matrix p = new Matrix(grid.xSize, grid.ySize);
			char action = grid.actions.charAt(i);
			char observation = grid.observations.charAt(i);
			for(int j = 0; j < grid.xSize; j++){
				for(int k = 0; k < grid.ySize; k++){
					char terrain = grid.grid[j][k].type;
					if(terrain == 'B') continue;
					float remainValue = (float) (states.get(i).matrix[j][k]*0.1*(terrain==observation ? 0.9 : 0.05));
					float newValue = 0;
					float newIndex = j*grid.xSize + k;
					switch(action){
					case 'D':
						if(j > 0 && grid.grid[j-1][k].type != 'B'){
							newValue = (float) (states.get(i).matrix[j-1][k]*0.9*(terrain==observation ? 0.9 : 0.05));
							newIndex -= grid.xSize; //subtract one row
						}
						if(j == grid.xSize-1 || grid.grid[j+1][k].type == 'B') remainValue *= 10;
						break;
					case 'R':
						if(k > 0 && grid.grid[j][k-1].type != 'B'){
							newValue = (float) (states.get(i).matrix[j][k-1]*0.9*(terrain==observation ? 0.9 : 0.05));
							newIndex -= 1; //subtract one column
						}
						if(k == grid.ySize-1 || grid.grid[j][k+1].type == 'B') remainValue *= 10;
						break;
					case 'U':
						if(j < grid.xSize-1 && grid.grid[j+1][k].type != 'B'){
							newValue = (float) (states.get(i).matrix[j+1][k]*0.9*(terrain==observation ? 0.9 : 0.05));
							newIndex += grid.xSize; //add one row
						}
						if(j == 0 || grid.grid[j-1][k].type == 'B') remainValue *= 10;
						break;
					case 'L':
						if(k < grid.ySize-1 && grid.grid[j][k+1].type != 'B'){
							newValue = (float) (states.get(i).matrix[j][k+1]*0.9*(terrain==observation ? 0.9 : 0.05));
							newIndex += 1; //add one column
						}
						if(k == 0 || grid.grid[j][k-1].type == 'B') remainValue *= 10;
						break;
					}		
					if(newValue > remainValue){
						m.matrix[j][k] = newValue;
						p.matrix[j][k] = newIndex; //points to where the current state most likely transitioned from
					}
					else{
						m.matrix[j][k] = remainValue;
						p.matrix[j][k] = j*grid.xSize + k; //points to the index of the current state (did not move)
					}
				}
			}
			states.add(normalize(grid, m));
			priors.add(p);
		}
		
		return states.get(iterations);
	}
	
	public Matrix normalize(Grid g, Matrix m) {
		float totalSum = 0;
		for (int i = 0; i < g.ySize; i++) {
			for (int j = 0; j < g.xSize; j++) {
				totalSum += m.matrix[i][j];
			}
		}
		for (int i = 0; i < g.ySize; i++) {
			for (int j = 0; j < g.xSize; j++) {
				m.matrix[i][j] = (m.matrix[i][j] / totalSum);
			}
		}
		return m;
	}
	
	public void printPath(){
		path = new ArrayList<Integer>();
		int index = maxEntry(states.get(priors.size()));
		path.add(index);
		for(int i = priors.size()-1; i >= 0; i--){
			index = (int)priors.get(i).matrix[index/grid.xSize][index%grid.xSize];
			path.add(index);
		}
		for(int i = path.size()-1; i >= 0; i--){
			System.out.println("(" + (path.get(i)/grid.xSize+1) + ", " + (path.get(i)%grid.xSize+1) + ")");
		}
	}
	
	public int maxEntry(Matrix m){
		int maxR = 0, maxC = 0;
		for(int i = 0; i < m.xSize; i++){
			for(int j = 0; j < m.ySize; j++){
				if(m.matrix[i][j] > m.matrix[maxR][maxC]){
					maxR = i;
					maxC = j;
				}
			}
		}
		return maxR*m.xSize + maxC;
	}
}
