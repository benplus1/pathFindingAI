package prob;

import java.util.*;

public class Transition{
	
	public Matrix leftMove;
	public Matrix rightMove;
	public Matrix upMove;
	public Matrix downMove;
	public Matrix nObs;
	public Matrix tObs;
	public Matrix hObs;
	
	public ArrayList<Matrix> transitions = new ArrayList<Matrix>();
	public ArrayList<Matrix> priors = new ArrayList<Matrix>();
	
	public ArrayList<Coordinate> viterbiPath = new ArrayList<Coordinate>();
	
	public Transition(Grid g) {
		for (int i = 0; i < g.ySize; i++) {
			for (int j = 0; j < g.xSize; j++) {
				leftMove = new Matrix(g.xSize, g.ySize);
				rightMove = new Matrix(g.xSize, g.ySize);
				upMove = new Matrix(g.xSize, g.ySize);
				downMove = new Matrix(g.xSize, g.ySize);
				nObs = new Matrix(g.xSize, g.ySize);
				tObs = new Matrix(g.xSize, g.ySize);
				hObs = new Matrix(g.xSize, g.ySize);
			}
		}
		for (int i = 0; i < g.ySize; i++) {
			for (int j = 0; j < g.xSize; j++) {
				if (g.grid[i][j].type == 'B') {
					rightMove.matrix[i][j] = 0;
					continue;
				}
				if (j == (g.xSize-1)) {
					rightMove.matrix[i][j] += 1;
				}
				else if (g.grid[i][j+1].type == 'B') {
					rightMove.matrix[i][j] += 1;
				}
				else {
					rightMove.matrix[i][j+1] += .9;
					rightMove.matrix[i][j] += .1;
				}
			}
		}
		for (int i = 0; i < g.ySize; i++) {
			for (int j = 0; j < g.xSize; j++) {
				if (g.grid[i][j].type == 'B') {
					leftMove.matrix[i][j] = 0;
					continue;
				}
				if (j == 0) {
					leftMove.matrix[i][j] += 1;
				}
				else if (g.grid[i][j-1].type == 'B') {
					leftMove.matrix[i][j] += 1;
				}
				else {
					leftMove.matrix[i][j-1] += .9;
					leftMove.matrix[i][j] += .1;
				}
			}
		}
		for (int i = 0; i < g.ySize; i++) {
			for (int j = 0; j < g.xSize; j++) {
				if (g.grid[i][j].type == 'B') {
					upMove.matrix[i][j] = 0;
					continue;
				}
				if (i == 0) {
					upMove.matrix[i][j] += 1;
				}
				else if (g.grid[i-1][j].type == 'B') {
					upMove.matrix[i][j] += 1;
				}
				else {
					upMove.matrix[i-1][j] += .9;
					upMove.matrix[i][j] += .1;
				}
			}
		}
		for (int i = 0; i < g.ySize; i++) {
			for (int j = 0; j < g.xSize; j++) {
				if (g.grid[i][j].type == 'B') {
					downMove.matrix[i][j] = 0;
					continue;
				}
				if (i == (g.ySize-1)) {
					downMove.matrix[i][j] += 1;
				}
				else if (g.grid[i+1][j].type == 'B') {
					downMove.matrix[i][j] += 1;
				}
				else {
					downMove.matrix[i+1][j] += .9;
					downMove.matrix[i][j] += .1;
				}
			}
		}
		for (int i = 0; i < g.ySize; i++) {
			for (int j = 0; j < g.xSize; j++) {
				if (g.grid[i][j].type == 'B') {
					nObs.matrix[i][j] = 0;
				}
				else if (g.grid[i][j].type == 'N') {
					nObs.matrix[i][j] = (float) (0.9);
				}
				else if (g.grid[i][j].type == 'T') {
					nObs.matrix[i][j] = (float) (0.05);
				}
				else if (g.grid[i][j].type == 'H') {
					nObs.matrix[i][j] = (float) (0.05);
				}
			}
		}
		for (int i = 0; i < g.ySize; i++) {
			for (int j = 0; j < g.xSize; j++) {
				if (g.grid[i][j].type == 'B') {
					tObs.matrix[i][j] = 0;
				}
				else if (g.grid[i][j].type == 'N') {
					tObs.matrix[i][j] = (float) (0.05);
				}
				else if (g.grid[i][j].type == 'T') {
					tObs.matrix[i][j] = (float) (0.9);
				}
				else if (g.grid[i][j].type == 'H') {
					tObs.matrix[i][j] = (float) (0.05);
				}
			}
		}
		for (int i = 0; i < g.ySize; i++) {
			for (int j = 0; j < g.xSize; j++) {
				if (g.grid[i][j].type == 'B') {
					hObs.matrix[i][j] = 0;
				}
				else if (g.grid[i][j].type == 'N') {
					hObs.matrix[i][j] = (float) (0.05);
				}
				else if (g.grid[i][j].type == 'T') {
					hObs.matrix[i][j] = (float) (0.05);
				}
				else if (g.grid[i][j].type == 'H') {
					hObs.matrix[i][j] = (float) (0.9);
				}
			}
		}
	}
	
	public Transition computeFiltering(Grid g, int iteration) {
		Transition t = new Transition(g);
		Matrix init = new Matrix(g.xSize, g.ySize, g);
		Matrix transInit = new Matrix(g.xSize, g.ySize, g, 1);
		t.priors.add(init);
		t.transitions.add(transInit);
		for (int i = 0; i < iteration; i++) {
			/*
			if (i == 0) {
				char c = g.actions.charAt(0);
				if (c == 'R') {
					t.transitions.add(t.rightMove);
				}
				else if (c == 'L') {
					t.transitions.add(t.leftMove);
				}
				else if (c == 'U') {
					t.transitions.add(t.upMove);
				}
				else {
					t.transitions.add(t.downMove);
				}
			}
			*/
			char c = g.actions.charAt(i);
			Matrix temp1 = computeSumInFiltering(g, t, c);
			Matrix temp2 = computeCurrentObservation(g, g.observations.charAt(i));
			Matrix nextStateTemp = multiply(temp1, temp2, g.xSize, g.ySize);
			Matrix nextState = normalize(g, nextStateTemp);
			t.priors.add(nextState);
			Matrix nextTrans = computeCurrentTransition(g, g.actions.charAt(i), t);
			t.transitions.add(nextTrans);
		}
		return t;
	}
	
	public Transition computeViterbi(Grid g, int iteration) {
		Transition t = new Transition(g);
		Matrix init = new Matrix(g.xSize, g.ySize, g);
		t.priors.add(init);
		for (int i = 0; i < iteration; i++) {
			
		}
		return t;
	}
	
	public Matrix computeCurrentTransition(Grid g, char currAct, Transition t) {
		Matrix m = new Matrix(g.xSize, g.ySize);
		Matrix temp = new Matrix(g.xSize, g.ySize);
		copy(t.transitions.get(t.transitions.size() - 1), temp, g.xSize, g.ySize);
		if (currAct == 'R') {
			for (int i = 0; i < g.ySize; i++) {
				for (int j = 0; j < g.xSize; j++) {
					if (g.grid[i][j].type == 'B') {
						m.matrix[i][j] = 0;
						continue;
					}
					if (j == (g.xSize-1)) {
						m.matrix[i][j] += (1.0*temp.matrix[i][j]);
					}
					else if (g.grid[i][j+1].type == 'B') {
						m.matrix[i][j] += (1.0*temp.matrix[i][j]);
					}
					else {
						m.matrix[i][j+1] += (.9*temp.matrix[i][j]);
						m.matrix[i][j] += (.1*temp.matrix[i][j]);
					}
				}
			}
		}
		else if (currAct == 'L') {
			for (int i = 0; i < g.ySize; i++) {
				for (int j = 0; j < g.xSize; j++) {
					if (g.grid[i][j].type == 'B') {
						m.matrix[i][j] = 0;
						continue;
					}
					if (j == 0) {
						m.matrix[i][j] += (1.0*temp.matrix[i][j]);
					}
					else if (g.grid[i][j-1].type == 'B') {
						m.matrix[i][j] += (1.0*temp.matrix[i][j]);
					}
					else {
						m.matrix[i][j-1] += (.9*temp.matrix[i][j]);
						m.matrix[i][j] += (.1*temp.matrix[i][j]);
					}
				}
			}
		}
		else if (currAct == 'U') {
			for (int i = 0; i < g.ySize; i++) {
				for (int j = 0; j < g.xSize; j++) {
					if (g.grid[i][j].type == 'B') {
						m.matrix[i][j] = 0;
						continue;
					}
					if (i == 0) {
						m.matrix[i][j] += (1.0*temp.matrix[i][j]);
					}
					else if (g.grid[i-1][j].type == 'B') {
						m.matrix[i][j] += (1.0*temp.matrix[i][j]);
					}
					else {
						m.matrix[i-1][j] += (.9*temp.matrix[i][j]);
						m.matrix[i][j] += (.1*temp.matrix[i][j]);
					}
				}
			}
		}
		else if (currAct == 'D') {
			for (int i = 0; i < g.ySize; i++) {
				for (int j = 0; j < g.xSize; j++) {
					if (g.grid[i][j].type == 'B') {
						m.matrix[i][j] = 0;
						continue;
					}
					if (i == (g.ySize-1)) {
						m.matrix[i][j] += (1.0*temp.matrix[i][j]);
					}
					else if (g.grid[i+1][j].type == 'B') {
						m.matrix[i][j] += (1.0*temp.matrix[i][j]);
					}
					else {
						m.matrix[i+1][j] += (.9*temp.matrix[i][j]);
						m.matrix[i][j] += (.1*temp.matrix[i][j]);
					}
				}
			}
		}
		return m;
	}
	
	public Matrix computeCurrentObservation(Grid g, char currObs) {
		Matrix m = new Matrix(g.xSize, g.ySize);
		if (currObs == 'T') {
			copy(tObs, m, g.xSize, g.ySize);
		}
		else if (currObs == 'N') {
			copy(nObs, m, g.xSize, g.ySize);
		}
		else if (currObs == 'H') {
			copy(hObs, m, g.xSize, g.ySize);
		}
		return m;
	}
	
	public Matrix computeSumInFiltering(Grid g, Transition t, char currAct) {
		Matrix m = new Matrix(g.xSize, g.ySize);
		Matrix currTrans = new Matrix(g.xSize, g.ySize);
		copy(t.transitions.get(t.transitions.size() - 1), currTrans, g.xSize, g.ySize);
		Matrix currPrior = new Matrix(g.xSize, g.ySize);
		copy(t.priors.get(t.priors.size() - 1), currPrior, g.xSize, g.ySize);
		if (currAct == 'R') {
			for (int i = 0; i < g.ySize; i++) {
				for (int j = 0; j < g.xSize; j++) {
					if (g.grid[i][j].type == 'B') {
						m.matrix[i][j] = 0;
						continue;
					}
					if (j == (g.xSize-1)) {
						m.matrix[i][j] += (1.0*currTrans.matrix[i][j]*currPrior.matrix[i][j]);
					}
					else if (g.grid[i][j+1].type == 'B') {
						m.matrix[i][j] += (1.0*currTrans.matrix[i][j]*currPrior.matrix[i][j]);
					}
					else {
						m.matrix[i][j+1] += (.9*currTrans.matrix[i][j]*currPrior.matrix[i][j]);
						m.matrix[i][j] += (.1*currTrans.matrix[i][j]*currPrior.matrix[i][j]);
					}
				}
			}
		}
		else if (currAct == 'L') {
			for (int i = 0; i < g.ySize; i++) {
				for (int j = 0; j < g.xSize; j++) {
					if (g.grid[i][j].type == 'B') {
						m.matrix[i][j] = 0;
						continue;
					}
					if (j == 0) {
						m.matrix[i][j] += (1.0*currTrans.matrix[i][j]*currPrior.matrix[i][j]);
					}
					else if (g.grid[i][j-1].type == 'B') {
						m.matrix[i][j] += (1.0*currTrans.matrix[i][j]*currPrior.matrix[i][j]);
					}
					else {
						m.matrix[i][j-1] += (.9*currTrans.matrix[i][j]*currPrior.matrix[i][j]);
						m.matrix[i][j] += (.1*currTrans.matrix[i][j]*currPrior.matrix[i][j]);
					}
				}
			}
		}
		else if (currAct == 'U') {
			for (int i = 0; i < g.ySize; i++) {
				for (int j = 0; j < g.xSize; j++) {
					if (g.grid[i][j].type == 'B') {
						m.matrix[i][j] = 0;
						continue;
					}
					if (i == 0) {
						m.matrix[i][j] += (1.0*currTrans.matrix[i][j]*currPrior.matrix[i][j]);
					}
					else if (g.grid[i-1][j].type == 'B') {
						m.matrix[i][j] += (1.0*currTrans.matrix[i][j]*currPrior.matrix[i][j]);
					}
					else {
						m.matrix[i-1][j] += (.9*currTrans.matrix[i][j]*currPrior.matrix[i][j]);
						m.matrix[i][j] += (.1*currTrans.matrix[i][j]*currPrior.matrix[i][j]);
					}
				}
			}
		}
		else if (currAct == 'D') {
			for (int i = 0; i < g.ySize; i++) {
				for (int j = 0; j < g.xSize; j++) {
					if (g.grid[i][j].type == 'B') {
						m.matrix[i][j] = 0;
						continue;
					}
					if (i == (g.ySize-1)) {
						m.matrix[i][j] += (1.0*currTrans.matrix[i][j]*currPrior.matrix[i][j]);
					}
					else if (g.grid[i+1][j].type == 'B') {
						m.matrix[i][j] += (1.0*currTrans.matrix[i][j]*currPrior.matrix[i][j]);
					}
					else {
						m.matrix[i+1][j] += (.9*currTrans.matrix[i][j]*currPrior.matrix[i][j]);
						m.matrix[i][j] += (.1*currTrans.matrix[i][j]*currPrior.matrix[i][j]);
					}
				}
			}
		}
		return m;
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
	
	public void copy(Matrix m1, Matrix m2, int x, int y) {
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				m2.matrix[i][j] = m1.matrix[i][j];
			}
		}
	}
	
	public Matrix add(Matrix m1, Matrix m2, int x, int y) {
		Matrix m = new Matrix(x, y);
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				m.matrix[i][j] = (m1.matrix[i][j] + m2.matrix[i][j]);
			}
		}
		return m;
	}
	
	public void addInto(Matrix m1, Matrix m2, int x, int y) {
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				m2.matrix[i][j] += m1.matrix[i][j];
			}
		}
	}
	
	public Matrix multiply(Matrix m1, Matrix m2, int x, int y) {
		Matrix m = new Matrix(x, y);
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				m.matrix[i][j] = (m1.matrix[i][j] * m2.matrix[i][j]);
			}
		}
		return m;
	}
}
