package prob;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		System.out.println("enter 0: solve A, enter 1: solve B");
		int j = scan.nextInt();
		if (j == 0) {
			Grid g = new Grid();
			Transition init = new Transition(g);
			Transition soln = init.computeFiltering(g, 4);
			Matrix solution = soln.priors.get(soln.priors.size()-1);
			solution.printMatrix(solution);
		}
		else if (j == 1) {
			Grid g = new Grid();
			Viterbi v = new Viterbi(g);
			int iter = 4;
			Matrix solution = v.computeViterbi(iter);
			System.out.println("Final matrix: ");
			solution.printMatrix(solution);
			System.out.println("Most likely path: ");
			v.printPath();
		}
	}
	
	public void readFile(String fileName, Grid g) throws IOException {
		FileReader fr =  new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		for (int i = 0; i < 101; i++) {
			StringTokenizer point = new StringTokenizer(line, ",");
			int xPoint = Integer.parseInt(point.nextToken());
			int yPoint = Integer.parseInt(point.nextToken());
			Coordinate c = new Coordinate(xPoint, yPoint);
			g.path.add(c);
		}
		line = br.readLine();
		g.actions = line;
		line = br.readLine();
		g.observations = line;
		br.close();
		fr.close();
	}
}