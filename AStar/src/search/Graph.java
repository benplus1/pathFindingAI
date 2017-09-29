package search;

import java.util.*;
import java.io.*;

public class Graph {

	Vertex[][] graph;
	
	Coordinate start = new Coordinate(0, 0);
	Coordinate goal = new Coordinate(0, 0);
	
	public ArrayList<Coordinate> usedHardCenters = new ArrayList<Coordinate>();
	
	public Graph() {
		graph = new Vertex[120][160];
		for (int i = 0; i < 120; i++) {
			for (int j = 0; j < 160; j++) {
				Vertex x = new Vertex();
				x.coor.xPoint = j;
				x.coor.yPoint = i;
				graph[i][j] = x;
			}
		}
	}
	
	public Graph hardToTraverse(Graph graph) {
		Random generator = new Random();
		for (int i = 0; i < 8; i++) {
		//	System.out.println("inCenterLoop");
			boolean previousHardCenterExists = true;
			Coordinate hard = new Coordinate(-1, -1);
			while (previousHardCenterExists == true) {
			//	System.out.println("creatingCenter");
				int hardY = generator.nextInt(120);
				int hardX = generator.nextInt(160);
				hard = new Coordinate(hardX, hardY);
				if (graph.usedHardCenters.isEmpty()) {
					previousHardCenterExists = false;
					graph.usedHardCenters.add(hard);
				}
				else {
					int j = 0;
					for (j = 0; j < graph.usedHardCenters.size(); j++) {
						if (hard == graph.usedHardCenters.get(j)) {
							break;
						}
					}
					if (j == graph.usedHardCenters.size()) {
						previousHardCenterExists = false;
						graph.usedHardCenters.add(hard);
					}
				}
			}
		//	System.out.println("centerSuccesfullyMade");
			int boxMinX = -1;
			int boxMinY = -1;
			int boxMaxX = -1;
			int boxMaxY = -1;
			if (hard.xPoint < 15) {
				boxMinX = 0;
				boxMaxX = hard.xPoint + 15;
			}
			else if (hard.xPoint > 144) {
				boxMaxX = 159;
				boxMinX = hard.xPoint - 15;
			}
			else {
				boxMaxX = hard.xPoint + 15;
				boxMinX = hard.xPoint - 15;
			}
			if (hard.yPoint < 15) {
				boxMinY = 0;
				boxMaxY = hard.yPoint + 15;
			}
			else if (hard.yPoint > 104) {
				boxMaxY = 119;
				boxMinY = hard.yPoint - 15;
			}
			else {
				boxMaxY = hard.yPoint + 15;
				boxMinY = hard.yPoint - 15;
			}
			for (int k = boxMinX; k <= boxMaxX; k++) {
				for (int l = boxMinY; l <= boxMaxY; l++) {
					if (graph.graph[l][k].hardTraverse == false) {
						graph.graph[l][k].hardTraverse = generator.nextBoolean();
					}
				}
			}
			previousHardCenterExists = true;
		}
		return graph;
	}
	
	public Graph highway(Graph g) {
		int failCounter = 0;
		int highwayNumber = 0;
		boolean generateComplete = false;
		while (generateComplete == false) {
			while (failCounter <= 5) {
				boolean success = generateHighway(g);
				if (success == true) {
					failCounter = 0;
					highwayNumber++;
					if (highwayNumber == 4) {
						generateComplete = true;
						break;
					}
				}
				else if (success == false) {
					failCounter++;
				}
			}
			if (failCounter > 5) {
				destroyHighway(g);
				highwayNumber = 0;
				failCounter = 0;
			}
		}
		return g;
	}
	
	public void destroyHighway(Graph g) {
		for (int i = 0; i < 120; i++) {
			for (int j = 0; j < 160; j++) {
				g.graph[i][j].highway = false;
			}
		}
	}
	
	public boolean generateHighway(Graph g) {
		final ArrayList<Coordinate> currentHighway = new ArrayList<Coordinate>();
		String previousDirection = "";
		boolean hitAnotherHighway = false;
		boolean isStartHighway = true;
		boolean hitABoundary = false;
		Coordinate c = new Coordinate();
		while (isStartHighway == true) {
			c = pickStart();
			if (g.graph[c.yPoint][c.xPoint].highway == false) {
				isStartHighway = false;
			}
		}
		Coordinate x = new Coordinate(c.xPoint, c.yPoint);
		currentHighway.add(x);
	//	System.out.println(c.xPoint + "," + c.yPoint);
		if (c.xPoint == 0) {
			previousDirection = "west";
			for (int i = 0; i <= 19; i++) {
				c.xPoint++;
				if (g.graph[c.yPoint][c.xPoint].highway == false) {
	//				System.out.println(c.xPoint + "," + c.yPoint);
					currentHighway.add(g.graph[c.yPoint][c.xPoint].coor);
				}
				else {
					return false;
				}
			}
		}
		else if (c.xPoint == 159) {
			previousDirection = "east";
			for (int i = 0; i <= 19; i++) {
				c.xPoint--;
				if (g.graph[c.yPoint][c.xPoint].highway == false) {
	//				System.out.println(c.xPoint + "," + c.yPoint);
					currentHighway.add(g.graph[c.yPoint][c.xPoint].coor);
				}
				else {
					return false;
				}
			}
		}
		else if (c.yPoint == 0) {
			previousDirection = "north";
			for (int i = 0; i <= 19; i++) {
				c.yPoint++;
				if (g.graph[c.yPoint][c.xPoint].highway == false) {
	//				System.out.println(c.xPoint + "," + c.yPoint);
					currentHighway.add(g.graph[c.yPoint][c.xPoint].coor);
				}
				else {
					return false;
				}
			}
		}
		else if (c.yPoint == 119) {
			previousDirection = "south";
			for (int i = 0; i <= 19; i++) {
				c.yPoint--;
				if (g.graph[c.yPoint][c.xPoint].highway == false) {
	//				System.out.println(c.xPoint + "," + c.yPoint);
					currentHighway.add(g.graph[c.yPoint][c.xPoint].coor);
				}
				else {
					return false;
				}
			}
		}
		while (hitAnotherHighway == false && hitABoundary == false) {
			previousDirection = newDirection(previousDirection);
			if (previousDirection.equals("west")) {
				for (int i = 0; i <= 19; i++) {
					c.xPoint++;
					if (c.xPoint > 159) {
						hitABoundary = true;
						break;
					}
					else {
						if (g.graph[c.yPoint][c.xPoint].highway == true) {
							hitAnotherHighway = true;
							break;
						}
						else {
	//						System.out.println(c.xPoint + "," + c.yPoint);
							currentHighway.add(g.graph[c.yPoint][c.xPoint].coor);
						}
					}
				}
			}
			else if (previousDirection.equals("east")) {
				for (int i = 0; i <= 19; i++) {
					c.xPoint--;
					if (c.xPoint < 0) {
						hitABoundary = true;
						break;
					}
					else {
						if (g.graph[c.yPoint][c.xPoint].highway == true) {
							hitAnotherHighway = true;
							break;
						}
						else {
	//						System.out.println(c.xPoint + "," + c.yPoint);
							currentHighway.add(g.graph[c.yPoint][c.xPoint].coor);
						}
					}
				}
			}
			else if (previousDirection.equals("north")) {
				for (int i = 0; i <= 19; i++) {
					c.yPoint++;
					if (c.yPoint > 119) {
						hitABoundary = true;
						break;
					}
					else {
						if (g.graph[c.yPoint][c.xPoint].highway == true) {
							hitAnotherHighway = true;
							break;
						}
						else {
	//						System.out.println(c.xPoint + "," + c.yPoint);
							currentHighway.add(g.graph[c.yPoint][c.xPoint].coor);
						}
					}
				}				
			}
			else if (previousDirection.equals("south")) {
				for (int i = 0; i <= 19; i++) {
					c.yPoint--;
					if (c.yPoint < 0) {
						hitABoundary = true;
						break;
					}
					else {
						if (g.graph[c.yPoint][c.xPoint].highway == true) {
							hitAnotherHighway = true;
							break;
						}
						else {
	//						System.out.println(c.xPoint + "," + c.yPoint);
							currentHighway.add(g.graph[c.yPoint][c.xPoint].coor);
						}
					}
				}
			}
		}
		if (hitAnotherHighway == true) {
			return false;
		}
		else if (hitABoundary == true) {
			if (currentHighway.size() < 100) {
				return false;
			}
			else {
	//			for (int i = 0; i < currentHighway.size(); i++) {
	//				System.out.println(currentHighway.get(i).xPoint + "," + currentHighway.get(i).yPoint);
	//			}
				for (int i = 0; i < currentHighway.size(); i++) {
					g.graph[currentHighway.get(i).yPoint][currentHighway.get(i).xPoint].highway = true;
				}
	//			System.out.println("highwayGeneratedComplete");
				return true;
			}
		}
		return false;
	}
	
	public String newDirection(String previous) {
		Random generator = new Random();
		String turn = "";
		int decider = generator.nextInt(10);
		if (decider == 0 || decider == 1) {
			turn = "left";
		}
		else if (decider == 2 || decider == 3) {
			turn = "right";
		}
		else {
			turn = "no";
		}
		if (previous.equals("west")) {
			if (turn.equals("left")) {
				previous = "south";
			}
			else if (turn.equals("right")) {
				previous = "north";
			}
			else if (turn.equals("no")) {
				previous = "west";
			}
		}
		else if (previous.equals("east")) {
			if (turn.equals("left")) {
				previous = "north";
			}
			else if (turn.equals("right")) {
				previous = "south";
			}
			else if (turn.equals("no")) {
				previous = "east";
			}
		}
		else if (previous.equals("north")) {
			if (turn.equals("left")) {
				previous = "west";
			}
			else if (turn.equals("right")) {
				previous = "east";
			}
			else if (turn.equals("no")) {
				previous = "north";
			}
		}
		else if (previous.equals("south")) {
			if (turn.equals("left")) {
				previous = "east";
			}
			else if (turn.equals("right")) {
				previous = "west";
			}
			else if (turn.equals("no")) {
				previous = "south";
			}
		}
		return previous;
	}
	
	public Coordinate pickStart() {
		Random generator = new Random();
		Coordinate c = new Coordinate();
		boolean x = generator.nextBoolean();
		if (x == true) {
			int xPoint = generator.nextInt(158) + 1;
			int yPoint = generator.nextInt(2);
			if (yPoint == 0) {
				yPoint = 0;
			}
			else if (yPoint != 0) {
				yPoint = 119;
			}
			c.xPoint = xPoint;
			c.yPoint = yPoint;
		}
		else {
			int xPoint = generator.nextInt(2);
			int yPoint = generator.nextInt(118) + 1;
			if (xPoint == 0) {
				xPoint = 0;
			}
			else if (xPoint != 0) {
				xPoint = 159;
			}
			c.xPoint = xPoint;
			c.yPoint = yPoint;
		}
		return c;
	}
	
	public Graph blocked(Graph g) {
		int counter = 0;
		int xPoint = 0;
		int yPoint = 0;
	//	Random generator = new Random();
		while (counter < 3840) {
	//		System.out.println(counter);
			Vertex current = g.graph[yPoint][xPoint];
	//		System.out.println(current.coor.xPoint + "," + current.coor.yPoint);
			int random = (int) (10 * Math.random());
			if (current.highway == false && current.blocked == false && (random == 0 || random == 1)) {
				g.graph[yPoint][xPoint].blocked = true;
				counter++;
				if (xPoint < 159) {
					xPoint++;
				}
				else if (xPoint == 159) {
					if (yPoint < 119) {
						yPoint++;
						xPoint = 0;
					}
					else if (yPoint == 119) {
						xPoint = 0;
						yPoint = 0;
					}
				}
			}
			else {
	//			System.out.println(current.highway + " " + current.blocked);
				if (xPoint < 159) {
					xPoint++;
				}
				else if (xPoint == 159) {
					if (yPoint < 119) {
						yPoint++;
						xPoint = 0;
					}
					else if (yPoint == 119) {
						xPoint = 0;
						yPoint = 0;
					}
				}
			}
		}
		return g;
	}
	
	public Graph startEnd(Graph g) {
		int manhattenDistance = 0;
		while (manhattenDistance < 100) {
			manhattenDistance = 0;
			Coordinate start = new Coordinate();
			Coordinate goal = new Coordinate();
			start = startEndPicker();
			goal = startEndPicker();
			while (g.graph[start.yPoint][start.xPoint].blocked == true) {
				start = startEndPicker();
			}
			while (g.graph[goal.yPoint][goal.xPoint].blocked == true) {
				goal = startEndPicker();
			}
			g.start = start;
			g.goal = goal;
			manhattenDistance = Math.abs(g.start.xPoint - g.goal.xPoint) + Math.abs(g.start.yPoint - g.goal.yPoint);
		}
		return g;
	}
	
	public Coordinate startEndPicker() {
		Random generator = new Random();
		int selectCorner = generator.nextInt(4);
		int xCoordinate = generator.nextInt(20);
		int yCoordinate = generator.nextInt(20);
		Coordinate c = new Coordinate();
		if (selectCorner == 0) {
			c.xPoint = xCoordinate;
			c.yPoint = yCoordinate;
		}
		else if (selectCorner == 1) {
			c.xPoint = xCoordinate;
			c.yPoint = 119 - yCoordinate;
		}
		else if (selectCorner == 2) {
			c.xPoint = 159 - xCoordinate;
			c.yPoint = yCoordinate;
		}
		else if (selectCorner == 3) {
			c.xPoint = 159 - xCoordinate;
			c.yPoint = 119 - yCoordinate;
		}
		return c;
	}
	
	public void removeStartEnd(Graph g) {
		g.start = null;
		g.goal = null;
	}
	
	public void initialize50() throws IOException{
//		System.out.println("graph created");
		String FILENAME = Interface.directory;
		for (int i = 0; i < 5; i++) {
			Graph graph = new Graph();
			graph = hardToTraverse(graph);
//			System.out.println("graphTraverseComplete");
			graph = highway(graph);
//			System.out.println("graphHighwayComplete");
			graph = blocked(graph);
//			System.out.println("graphBlockedComplete");
			for (int j = 0; j < 10; j++) {
				int caseNumber = (i * 10) + j;
				FileWriter fw = new FileWriter(FILENAME + "Case" + caseNumber + ".txt");
				BufferedWriter bw = new BufferedWriter(fw);
				removeStartEnd(graph);
//				System.out.println("removeComplete");
				graph = startEnd(graph);
//				System.out.println("startEndComplete");
				bw.write(graph.start.xPoint + "," + graph.start.yPoint);
				bw.newLine();
				bw.write(graph.goal.xPoint + "," + graph.goal.yPoint);
				bw.newLine();
				bw.newLine();
				for (int k = 0; k < 8; k++) {
					bw.write(graph.usedHardCenters.get(k).xPoint + "," + graph.usedHardCenters.get(k).yPoint);
					bw.newLine();
				}
				bw.newLine();
				for (int l = 0; l < 120; l++) {
					for (int m = 0; m < 160; m++) {
						Vertex current = graph.graph[l][m];
						if (current.blocked == true) {
							bw.write("0");
						}
						else if (current.hardTraverse == false && current.highway == false) {
							bw.write("1");
						}
						else if (current.hardTraverse == false && current.highway == true) {
							bw.write("a");
						}
						else if (current.hardTraverse == true && current.highway == false) {
							bw.write("2");
						}
						else if (current.hardTraverse == true && current.highway == true) {
							bw.write("b");
						}
					}
					bw.newLine();
				}
	//			System.out.println("writeFileComplete" + (10 * i + j));
				bw.close();
				fw.close();
			}
		}
	}
}