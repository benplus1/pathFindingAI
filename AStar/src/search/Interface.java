package search;

import java.util.*;

import java.lang.Math;
import java.lang.Float;

import java.io.*;

public class Interface {
	public static String directory = "";
	
	public static void main(String[] args) throws IOException {
		Graph g = new Graph();
		Interface i = new Interface();
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		System.out.println("enter 0: initialize 50 graphs\nenter 1: run algorithm for one file\nenter 2: run all 50\nenter 3: run sequential\nenter 4: run incremental");
		int j = scan.nextInt();
		if (j == 0) {
			g.initialize50();
		}
		else if (j == 1) {
			System.out.println("please enter the number of the case you wish to process");
			caseNo = scan.nextInt();
			String fileName = directory + "Case" + caseNo + ".txt";
			i.readFile(fileName, g);
			System.out.println("enter 0: regular A*\nenter 1: Sequential A*\nenter 2: incremental A*");
			int check = scan.nextInt();
			boolean success = false;
			double pathLength;
			double memoryUsed = -1;
			if (check == 0) {
				System.out.println("enter 0: uniform cost search\nenter 1: manhatten distance by river\nenter 2: Diagonal + Straight Line by river\nenter 3: Euclidean Distance by river\nenter 4: chebyshev distance\nenter 5: octile distance\nenter 6: min of all heuristics");
				int heuristic = scan.nextInt();
				System.out.println("enter w >= 1 for weight (enter 1 for regular heuristic cost)");
				float weight = scan.nextFloat();
				i.nodesExpanded = 1;
				long startTime = System.currentTimeMillis();
				double totalMemoryBefore = Runtime.getRuntime().freeMemory();
				success = i.algorithm(g, weight, heuristic, i);
				double totalMemoryAfter = Runtime.getRuntime().freeMemory();
				long endTime   = System.currentTimeMillis();
				memoryUsed = totalMemoryBefore - totalMemoryAfter;
				long totalTime = endTime - startTime;
				pathLength = 0;
				System.out.println("total time: " + totalTime);
			}
			else if (check == 1) {
				System.out.println("enter w >= 1 for weight1");
				float weight1 = scan.nextFloat();
				System.out.println("enter w >= 1 for weight2");
				float weight2 = scan.nextFloat();
				i.nodesExpanded = 1;
				long startTime = System.currentTimeMillis();
				double totalMemoryBefore = Runtime.getRuntime().freeMemory();
				success = i.sequentialAlgo(g, weight1, weight2, i);
				double totalMemoryAfter = Runtime.getRuntime().freeMemory();
				long endTime   = System.currentTimeMillis();
				memoryUsed = totalMemoryBefore - totalMemoryAfter;
				long totalTime = endTime - startTime;
				pathLength = 0;
				System.out.println("total time: " + totalTime);
			}
			else if (check == 2) {
				System.out.println("enter w >= 1 for weight1");
				float weight1 = scan.nextFloat();
				System.out.println("enter w >= 1 for weight2");
				float weight2 = scan.nextFloat();
				i.nodesExpanded = 1;
				long startTime = System.currentTimeMillis();
				double totalMemoryBefore = Runtime.getRuntime().freeMemory();
				success = i.incrementalAlgo(g, weight1, weight2, i);
				double totalMemoryAfter = Runtime.getRuntime().freeMemory();
				long endTime   = System.currentTimeMillis();
				memoryUsed = totalMemoryBefore - totalMemoryAfter;
				long totalTime = endTime - startTime;
				pathLength = 0;
				System.out.println("total time: " + totalTime);
			}
			if (success) {
				pathLength = calculateCostOfPathLength(i.solution);
				i.printSolution();
				System.out.println("path length: " + pathLength);
				System.out.println("nodes expanded: " + i.nodesExpanded);
				System.out.println("memory used: " + memoryUsed + " bytes");
				i.nodesExpanded = 1;
			}
			else {
				System.out.println("no solution");
				i.nodesExpanded = 1;
			}
		}
		else if (j == 2) {
			System.out.println("enter 0: uniform cost search\nenter 1: manhatten distance by river\nenter 2: Diagonal + Straight Line by river\nenter 3: Euclidean Distance by river\nenter 4: chebyshev distance\nenter 5: octile distance\nenter 6: min of all heuristics");
			int heuristic = scan.nextInt();
			System.out.println("enter w >= 1 for weight (enter 1 for regular heuristic cost)");
			float weight = scan.nextFloat();
			ArrayList<Double> pathLengthArr = new ArrayList<Double>();
			ArrayList<Long> totalTimeArr = new ArrayList<Long>();
			ArrayList<Long> nodesExpandedArr = new ArrayList<Long>();
			ArrayList<Double> memoryUsedArr = new ArrayList<Double>();
			for (int m = 0; m < 50; m++) {
				Graph excellent = new Graph();
				String fileName = directory + "Case" + m + ".txt";
				i.readFile(fileName, excellent);
				i.nodesExpanded = 1;
				long startTime = System.currentTimeMillis();
				double totalMemoryBefore = Runtime.getRuntime().freeMemory();
				boolean success = i.algorithm(excellent, weight, heuristic, i);
				double totalMemoryAfter = Runtime.getRuntime().freeMemory();
				long endTime   = System.currentTimeMillis();
				double memoryUsed = totalMemoryBefore - totalMemoryAfter;
				long totalTime = endTime - startTime;
	//			System.out.println(success);
				if (success) {
					double pathCost = calculateCostOfPathLength(i.solution);
	//				Interface opt = new Interface();
	//				Graph optGraph = new Graph();
	//				opt.readFile(fileName, optGraph);
					caseNo = m;
					i.printSolution();
					totalTimeArr.add(totalTime);
					nodesExpandedArr.add((long) i.nodesExpanded);
					memoryUsedArr.add(memoryUsed);
					for (int a = 0; a < 120; a++) {
						for (int b = 0; b < 160; b++) {
							excellent.graph[a][b].gValue = Float.MAX_VALUE;
							excellent.graph[a][b].hValue = 1000000;
							excellent.graph[a][b].parent = null;
							excellent.graph[a][b].fringe = false;
							excellent.graph[a][b].closed = false;
						}
					}
					i.solution.clear();
					i.nodesExpanded = 1;
					i.fringe.clear();
					i.closed.clear();
					i.algorithm(excellent, 0, 0, i);
					if (pathCost < 100) {
						for (int c = 0; c < i.solution.size() - 1; c++) {
							System.out.println(i.solution.get(c).coor.xPoint + "," + i.solution.get(c).coor.yPoint);
						}
					}
					double optimalPathCost = calculateCostOfPathLength(i.solution);
	//				System.out.println(pathCost);
	//				System.out.println(optimalPathCost);
	//				System.out.println(pathCost/optimalPathCost);
					pathLengthArr.add((pathCost/optimalPathCost));
					i.nodesExpanded = 1;
					i.solution.clear();
					i.closed.clear();
				}
				else {
					totalTimeArr.add(totalTime);
					memoryUsedArr.add(memoryUsed);
					i.nodesExpanded = 1;
					i.solution.clear();
				}
			}
	//		for (int k = 0; k < 50; k++) {
	//			System.out.println(pathLengthArr.get(k));
	//		}
			System.out.println("average (path length (cost) / opt path length (cost)): " + calculateDDAverageArrayList(pathLengthArr));
			System.out.println("average total time: " + calculateAverageArrayList(totalTimeArr));
			System.out.println("average memory used: " + calculateDAverageArrayList(memoryUsedArr) + " bytes");
			System.out.println("average nodes expanded: " + calculateAverageArrayList(nodesExpandedArr));
		}
		else if (j == 3) {
			System.out.println("enter w >= 1 for weight1");
			float weight1 = scan.nextFloat();
			System.out.println("enter w >= 1 for weight2");
			float weight2 = scan.nextFloat();
			ArrayList<Double> pathLengthArr = new ArrayList<Double>();
			ArrayList<Long> totalTimeArr = new ArrayList<Long>();
			ArrayList<Long> nodesExpandedArr = new ArrayList<Long>();
			ArrayList<Double> memoryUsedArr = new ArrayList<Double>();
			for (int m = 0; m < 50; m++) {
				Graph excellent = new Graph();
				String fileName = directory + "Case" + m + ".txt";
				i.readFile(fileName, excellent);
				i.nodesExpanded = 1;
				long startTime = System.currentTimeMillis();
				double totalMemoryBefore = Runtime.getRuntime().freeMemory();
				boolean success = i.sequentialAlgo(excellent, weight1, weight2, i);
	//			System.out.println(success + " " + m);
				double totalMemoryAfter = Runtime.getRuntime().freeMemory();
				long endTime   = System.currentTimeMillis();
				double memoryUsed = totalMemoryBefore - totalMemoryAfter;
				long totalTime = endTime - startTime;
	//			System.out.println(success);
				if (success) {
					double pathCost = calculateCostOfPathLength(i.solution);
					caseNo = m;
					i.printSolution();
	//				Interface opt = new Interface();
	//				Graph optGraph = new Graph();
	//				opt.readFile(fileName, optGraph);
					totalTimeArr.add(totalTime);
					nodesExpandedArr.add((long) i.nodesExpanded);
					memoryUsedArr.add(memoryUsed);
					for (int a = 0; a < 120; a++) {
						for (int b = 0; b < 160; b++) {
							excellent.graph[a][b].gValue = Float.MAX_VALUE;
							excellent.graph[a][b].hValue = 1000000;
							excellent.graph[a][b].parent = null;
							excellent.graph[a][b].fringe = false;
							excellent.graph[a][b].closed = false;
						}
					}
					i.solution.clear();
					i.nodesExpanded = 1;
					i.manFringe.clear();
					i.manClosed.clear();
					i.dslFringe.clear();
					i.dslClosed.clear();
					i.euclidFringe.clear();
					i.euclidClosed.clear();
					i.octClosed.clear();
					i.octFringe.clear();
					i.chebFringe.clear();
					i.chebClosed.clear();
					i.algorithm(excellent, 0, 0, i);
	//				if (pathCost < 100) {
	//					for (int c = 0; c < i.solution.size() - 1; c++) {
	//						System.out.println(i.solution.get(c).coor.xPoint + "," + i.solution.get(c).coor.yPoint);
	//					}
	//				}
					double optimalPathCost = calculateCostOfPathLength(i.solution);
	//				System.out.println(pathCost);
	//				System.out.println(optimalPathCost);
	//				System.out.println(pathCost/optimalPathCost);
					pathLengthArr.add((pathCost/optimalPathCost));
					i.nodesExpanded = 1;
					i.solution.clear();
					i.closed.clear();
				}
				else {
					totalTimeArr.add(totalTime);
					memoryUsedArr.add(memoryUsed);
					i.nodesExpanded = 1;
					i.solution.clear();
				}
			}
	//		for (int k = 0; k < 50; k++) {
	//			System.out.println(pathLengthArr.get(k));
	//		}
			System.out.println("average (path length (cost) / opt path length (cost)): " + calculateDDAverageArrayList(pathLengthArr));
			System.out.println("average total time: " + calculateAverageArrayList(totalTimeArr));
			System.out.println("average memory used: " + calculateDAverageArrayList(memoryUsedArr) + " bytes");
			System.out.println("average nodes expanded: " + calculateAverageArrayList(nodesExpandedArr));
		}
		else if (j == 4) {
			System.out.println("enter w >= 1 for weight1");
			float weight1 = scan.nextFloat();
			System.out.println("enter w >= 1 for weight2");
			float weight2 = scan.nextFloat();
			ArrayList<Double> pathLengthArr = new ArrayList<Double>();
			ArrayList<Long> totalTimeArr = new ArrayList<Long>();
			ArrayList<Long> nodesExpandedArr = new ArrayList<Long>();
			ArrayList<Double> memoryUsedArr = new ArrayList<Double>();
			for (int m = 0; m < 50; m++) {
				Graph excellent = new Graph();
				String fileName = directory + "Case" + m + ".txt";
				i.readFile(fileName, excellent);
				i.nodesExpanded = 1;
				long startTime = System.currentTimeMillis();
				double totalMemoryBefore = Runtime.getRuntime().freeMemory();
				boolean success = i.incrementalAlgo(excellent, weight1, weight2, i);
	//			System.out.println(success + " " + m);
				double totalMemoryAfter = Runtime.getRuntime().freeMemory();
				long endTime   = System.currentTimeMillis();
				double memoryUsed = totalMemoryBefore - totalMemoryAfter;
				long totalTime = endTime - startTime;
	//			System.out.println(success);
				if (success) {
					double pathCost = calculateCostOfPathLength(i.solution);
					caseNo = m;
					i.printSolution();
	//				Interface opt = new Interface();
	//				Graph optGraph = new Graph();
	//				opt.readFile(fileName, optGraph);
					totalTimeArr.add(totalTime);
					nodesExpandedArr.add((long) i.nodesExpanded);
					memoryUsedArr.add(memoryUsed);
					for (int a = 0; a < 120; a++) {
						for (int b = 0; b < 160; b++) {
							excellent.graph[a][b].gValue = Float.MAX_VALUE;
							excellent.graph[a][b].hValue = 1000000;
							excellent.graph[a][b].parent = null;
							excellent.graph[a][b].fringe = false;
							excellent.graph[a][b].closed = false;
						}
					}
					i.solution.clear();
					i.nodesExpanded = 1;
					i.manFringe.clear();
					i.manClosed.clear();
					i.dslFringe.clear();
					i.closed.clear();
	//				i.dslClosed.clear();
					i.euclidFringe.clear();
	//				i.euclidClosed.clear();
	//				i.octClosed.clear();
					i.octFringe.clear();
					i.chebFringe.clear();
	//				i.chebClosed.clear();
					i.algorithm(excellent, 0, 0, i);
	//				if (pathCost < 100) {
	//					for (int c = 0; c < i.solution.size() - 1; c++) {
	//						System.out.println(i.solution.get(c).coor.xPoint + "," + i.solution.get(c).coor.yPoint);
	//					}
	//				}
					double optimalPathCost = calculateCostOfPathLength(i.solution);
	//				System.out.println(pathCost);
	//				System.out.println(optimalPathCost);
	//				System.out.println(pathCost/optimalPathCost);
					pathLengthArr.add((pathCost/optimalPathCost));
					i.nodesExpanded = 1;
					i.solution.clear();
					i.closed.clear();
				}
				else {
					totalTimeArr.add(totalTime);
					memoryUsedArr.add(memoryUsed);
					i.nodesExpanded = 1;
					i.solution.clear();
				}
			}
	//		for (int k = 0; k < 50; k++) {
	//			System.out.println(pathLengthArr.get(k));
	//		}
			System.out.println("average (path length (cost) / opt path length (cost)): " + calculateDDAverageArrayList(pathLengthArr));
			System.out.println("average total time: " + calculateAverageArrayList(totalTimeArr));
			System.out.println("average memory used: " + calculateDAverageArrayList(memoryUsedArr) + " bytes");
			System.out.println("average nodes expanded: " + calculateAverageArrayList(nodesExpandedArr));
		}
	}
	
	public static long calculateAverageArrayList(ArrayList<Long> arr) {
		long average = 0;
		for (int i = 0; i < arr.size(); i++) {
			average = average + arr.get(i);
		}
		average = (average/(arr.size()));
		return average;
	}
	
	public static double calculateDAverageArrayList(ArrayList<Double> arr) {
		double average = 0;
		for (int i = 0; i < arr.size(); i++) {
			average = average + (arr.get(i) / 50);
		}
		return average;
	}
	
	public static double calculateDDAverageArrayList(ArrayList<Double> arr) {
		double average = 0;
		for (int i = 0; i < arr.size(); i++) {
			average = average + arr.get(i);
		}
		average = (average/(arr.size()));
		return average;
	}
	
	public static double calculateCostOfPathLength(ArrayList<Vertex> arr) {
		double cost = 0;
		for (int i = 0; i < (arr.size() - 1); i++) {
			cost += calculateVertexCost(arr.get(i), arr.get(i + 1));
		}
		return cost;
	}
	
	public boolean sequentialAlgo(Graph g, float w1, float w2, Interface i) {
		Comparator<Vertex> wow1 = new VertexComparator1();
		Comparator<Vertex> wow2 = new VertexComparator2();
		Comparator<Vertex> wow3 = new VertexComparator3();
		Comparator<Vertex> wow4 = new VertexComparator4();
		Comparator<Vertex> wow5 = new VertexComparator5();
		Vertex start = g.graph[g.start.yPoint][g.start.xPoint];
		Vertex goal = g.graph[g.goal.yPoint][g.goal.xPoint];
		i.manFringe = new PriorityQueue<Vertex>(10, wow1);
		i.manClosed = new ArrayList<Vertex>();
		i.dslFringe = new PriorityQueue<Vertex>(10, wow2);
		i.dslClosed = new ArrayList<Vertex>();
		i.euclidFringe = new PriorityQueue<Vertex>(10, wow3);
		i.euclidClosed = new ArrayList<Vertex>();
		i.octFringe = new PriorityQueue<Vertex>(10, wow4);
		i.octClosed = new ArrayList<Vertex>();
		i.chebFringe = new PriorityQueue<Vertex>(10, wow5);
		i.chebClosed = new ArrayList<Vertex>();
		start.parent1 = start;
		start.gValue1 = 0;
		start.hValue1 = hValue(start, g, w1, 1);
		i.manFringe.add(start);
		start.fringe1 = true;
		start.parent2 = start;
		start.gValue2 = 0;
		start.hValue2 = hValue(start, g, w1, 2);
		i.dslFringe.add(start);
		start.fringe2 = true;
		start.parent3 = start;
		start.gValue3 = 0;
		start.hValue3 = hValue(start, g, w1, 3);
		i.euclidFringe.add(start);
		start.fringe3 = true;
		start.parent4 = start;
		start.gValue4 = 0;
		start.hValue4 = hValue(start, g, w1, 4);
		i.octFringe.add(start);
		start.fringe4 = true;
		start.parent5 = start;
		start.gValue5 = 0;
		start.hValue5 = hValue(start, g, w1, 5);
		i.chebFringe.add(start);
		start.fringe5 = true;
		boolean foundPath = false;
		int successQueue = -1;
		while (!i.manFringe.isEmpty()) {
			for (int a = 1; a < 5; a++) {
				PriorityQueue<Vertex> f = null;
				ArrayList<Vertex> c = null;
				Vertex topMan = i.manFringe.peek();
				Vertex top = null;
				float topKey = 0;
				float topManKey = topMan.gValue1 + hValue(topMan, g, w1, 1);
				if (a == 1) {
					f = i.dslFringe;
					c = i.dslClosed;
					top = f.peek();
					topKey = top.gValue2 + hValue(top, g, w1, 2);
				}
				else if (a == 2) {
					f = i.euclidFringe;
					c = i.euclidClosed;
					top = f.peek();
					topKey = top.gValue3 + hValue(top, g, w1, 3);
				}
				else if (a == 3) {
					f = i.octFringe;
					c = i.octClosed;
					top = f.peek();
					topKey = top.gValue4 + hValue(top, g, w1, 4);
				}
				else if (a == 4) {
					f = i.chebFringe;
					c = i.chebClosed;
					top = f.peek();
					topKey = top.gValue5 + hValue(top, g, w1, 5);
				}
				if (topKey <= (w2 * topManKey)) {
					Vertex current = f.remove();
					if (a == 1) {
						current.fringe2 = false;
					}
					else if (a == 2) {
						current.fringe3 = false;
					}
					else if (a == 3) {
						current.fringe4 = false;
					}
					else if (a == 4) {
						current.fringe5 = false;
					}
					if (top.coor == goal.coor) {
						foundPath = true;
						successQueue = (a + 1);
						break;
					}
					if (a == 1) {
						current.closed2 = true;
					}
					else if (a == 2) {
						current.closed3 = true;
					}
					else if (a == 3) {
						current.closed4 = true;
					}
					else if (a == 4) {
						current.closed5 = true;
					}
					c.add(current);
					expandState(f, current, g, w1, i, (a + 1));
				}
				else {
					Vertex current = i.manFringe.remove();
					current.fringe1 = false;
					if (topMan.coor == goal.coor) {
						foundPath = true;
						successQueue = 1;
						break;
					}
					current.closed1 = true;
					i.manClosed.add(current);
					expandState(i.manFringe, current, g, w1, i, 1);
				}
			}
			if (foundPath == true) {
				break;
			}
		}
		if (foundPath == false) {
			return false;
		}
		else {
			Vertex temp = goal;
			if (successQueue == 1) {
				while (temp.parent1 != temp) {
					solution.add(temp);
					temp.gValue = temp.gValue1;
					temp.hValue = hValue(temp, g, w1, 1);
					System.out.println(temp.coor.xPoint + "," + temp.coor.yPoint + " " + successQueue);
					temp = temp.parent1;
				}
			}
			else if (successQueue == 2) {
				while (temp.parent2 != temp) {
					solution.add(temp);
					temp.gValue = temp.gValue2;
					temp.hValue = hValue(temp, g, w1, 2);
					System.out.println(temp.coor.xPoint + "," + temp.coor.yPoint + " " + successQueue);
					temp = temp.parent2;
				}
			}
			else if (successQueue == 3) {
				while (temp.parent3 != temp) {
					solution.add(temp);
					temp.gValue = temp.gValue3;
					temp.hValue = hValue(temp, g, w1, 3);
					System.out.println(temp.coor.xPoint + "," + temp.coor.yPoint + " " + successQueue);
					temp = temp.parent3;
				}
			}
			else if (successQueue == 4) {
				while (temp.parent4 != temp) {
					solution.add(temp);
					temp.gValue = temp.gValue4;
					temp.hValue = hValue(temp, g, w1, 4);
					System.out.println(temp.coor.xPoint + "," + temp.coor.yPoint + " " + successQueue);
					temp = temp.parent4;
				}
			}
			else if (successQueue == 5) {
				while (temp.parent5 != temp) {
					solution.add(temp);
					temp.gValue = temp.gValue5;
					temp.hValue = hValue(temp, g, w1, 5);
					System.out.println(temp.coor.xPoint + "," + temp.coor.yPoint + " " + successQueue);
					temp = temp.parent5;
				}
			}
			solution.add(temp);
			return true;
		}
	}
	
	public void expandState(PriorityQueue<Vertex> f, Vertex c, Graph g, float w, Interface j, int h) {
		Vertex[] s = new Vertex[8];
		int xPoint = c.coor.xPoint;
		int yPoint = c.coor.yPoint;
		int numOfS = 0;
		if (c.coor.xPoint == 0) {
			if (c.coor.yPoint == 0) {
				numOfS = 3;
				s[0] = g.graph[yPoint + 1][xPoint];
				s[1] = g.graph[yPoint][xPoint + 1];
				s[2] = g.graph[yPoint + 1][xPoint + 1];
			}
			else if (c.coor.yPoint == 119) {
				numOfS = 3;
				s[0] = g.graph[yPoint - 1][xPoint];
				s[1] = g.graph[yPoint][xPoint + 1];
				s[2] = g.graph[yPoint - 1][xPoint + 1];
			}
			else {
				numOfS = 5;
				s[0] = g.graph[yPoint + 1][xPoint];
				s[1] = g.graph[yPoint][xPoint + 1];
				s[2] = g.graph[yPoint + 1][xPoint + 1];
				s[3] = g.graph[yPoint - 1][xPoint];
				s[4] = g.graph[yPoint - 1][xPoint + 1];
			}
		}
		else if (c.coor.xPoint == 159) {
			if (c.coor.yPoint == 0) {
				numOfS = 3;
				s[0] = g.graph[yPoint + 1][xPoint];
				s[1] = g.graph[yPoint][xPoint - 1];
				s[2] = g.graph[yPoint + 1][xPoint - 1];
			}
			else if (c.coor.yPoint == 119) {
				numOfS = 3;
				s[0] = g.graph[yPoint - 1][xPoint];
				s[1] = g.graph[yPoint][xPoint - 1];
				s[2] = g.graph[yPoint - 1][xPoint - 1];
			}
			else {
				numOfS = 5;
				s[0] = g.graph[yPoint + 1][xPoint];
				s[1] = g.graph[yPoint][xPoint - 1];
				s[2] = g.graph[yPoint + 1][xPoint - 1];
				s[3] = g.graph[yPoint - 1][xPoint - 1];
				s[4] = g.graph[yPoint - 1][xPoint];
			}
		}
		else if (c.coor.yPoint == 0) {
			numOfS = 5;
			s[0] = g.graph[yPoint + 1][xPoint];
			s[1] = g.graph[yPoint + 1][xPoint - 1];
			s[2] = g.graph[yPoint + 1][xPoint + 1];
			s[3] = g.graph[yPoint][xPoint + 1];
			s[4] = g.graph[yPoint][xPoint - 1];
		}
		else if (c.coor.yPoint == 119) {
			numOfS = 5;
			s[0] = g.graph[yPoint - 1][xPoint];
			s[1] = g.graph[yPoint - 1][xPoint - 1];
			s[2] = g.graph[yPoint - 1][xPoint + 1];
			s[3] = g.graph[yPoint][xPoint + 1];
			s[4] = g.graph[yPoint][xPoint - 1];
		}
		else {
			numOfS = 8;
			s[0] = g.graph[yPoint - 1][xPoint];
			s[1] = g.graph[yPoint - 1][xPoint - 1];
			s[2] = g.graph[yPoint - 1][xPoint + 1];
			s[3] = g.graph[yPoint][xPoint + 1];
			s[4] = g.graph[yPoint][xPoint - 1];
			s[5] = g.graph[yPoint + 1][xPoint];
			s[6] = g.graph[yPoint + 1][xPoint - 1];
			s[7] = g.graph[yPoint + 1][xPoint + 1];
		}
		for (int i = 0; i < numOfS; i++) {
			if (h == 1) {
				if (s[i].closed1 == false) {
					if (s[i].fringe1 == false) {
						s[i].gValue1 = Float.MAX_VALUE;
						s[i].parent1 = null;
					}
					updateVertexUniformS(f, c, s[i], w, h, g, j);
				}
			}
			else if (h == 2) {
				if (s[i].closed2 == false) {
					if (s[i].fringe2 == false) {
						s[i].gValue2 = Float.MAX_VALUE;
						s[i].parent2 = null;
					}
					updateVertexUniformS(f, c, s[i], w, h, g, j);
				}
			}
			else if (h == 3) {
				if (s[i].closed3 == false) {
					if (s[i].fringe3 == false) {
						s[i].gValue3 = Float.MAX_VALUE;
						s[i].parent3 = null;
					}
					updateVertexUniformS(f, c, s[i], w, h, g, j);
				}
			}
			else if (h == 4) {
				if (s[i].closed4 == false) {
					if (s[i].fringe4 == false) {
						s[i].gValue4 = Float.MAX_VALUE;
						s[i].parent4 = null;
					}
					updateVertexUniformS(f, c, s[i], w, h, g, j);
				}
			}
			else if (h == 5) {
				if (s[i].closed5 == false) {
					if (s[i].fringe5 == false) {
						s[i].gValue5 = Float.MAX_VALUE;
						s[i].parent5 = null;
					}
					updateVertexUniformS(f, c, s[i], w, h, g, j);
				}
			}
		}
	}
	
	public void updateVertexUniformS(PriorityQueue<Vertex> f, Vertex c, Vertex s, float w, int h, Graph g, Interface i) {
		float cost = calculateVertexCost(c, s);
		if (s.blocked != true) {
			if (h == 1) {
				if ((c.gValue1 + cost) < s.gValue1) {
					s.gValue1 = c.gValue1 + cost;
					s.hValue1 = hValue(s, g, w, h);
					s.parent1 = c;
					if (s.fringe1 == true) {
						f.remove(s);
						s.fringe1 = false;
					}
					i.manFringe.add(s);
					i.nodesExpanded++;
					s.fringe1 = true;
				}
			}
			else if (h == 2) {
				if ((c.gValue2 + cost) < s.gValue2) {
					s.gValue2 = c.gValue2 + cost;
					s.hValue2 = hValue(s, g, w, h);
					s.parent2 = c;
					if (s.fringe2 == true) {
						f.remove(s);
						s.fringe2 = false;
					}
					i.dslFringe.add(s);
					i.nodesExpanded++;
					s.fringe2 = true;
				}
			}
			else if (h == 3) {
				if ((c.gValue3 + cost) < s.gValue3) {
					s.gValue3 = c.gValue3 + cost;
					s.hValue3 = hValue(s, g, w, h);
					s.parent3 = c;
					if (s.fringe3 == true) {
						f.remove(s);
						s.fringe3 = false;
					}
					i.euclidFringe.add(s);
					i.nodesExpanded++;
					s.fringe3 = true;
				}
			}
			else if (h == 4) {
				if ((c.gValue4 + cost) < s.gValue4) {
					s.gValue4 = c.gValue4 + cost;
					s.hValue4 = hValue(s, g, w, h);
					s.parent4 = c;
					if (s.fringe4 == true) {
						f.remove(s);
						s.fringe4 = false;
					}
					i.octFringe.add(s);
					i.nodesExpanded++;
					s.fringe4 = true;
				}
			}
			else if (h == 5) {
				if ((c.gValue5 + cost) < s.gValue5) {
					s.gValue5 = c.gValue5 + cost;
					s.hValue5 = hValue(s, g, w, h);
					s.parent5 = c;
					if (s.fringe5 == true) {
						f.remove(s);
						s.fringe5 = false;
					}
					i.chebFringe.add(s);
					i.nodesExpanded++;
					s.fringe5 = true;
				}
			}
		}
	}
	
//	public void initializeSequentialHeuristic(PriorityQueue<Vertex> q, ArrayList<Vertex> c, Comparator<Vertex> wow) {
//		q = new PriorityQueue<Vertex>(10, wow);
//		c = new ArrayList<Vertex>();
//	}
	
	public boolean incrementalAlgo(Graph g, float w1, float w2, Interface i) {
		Comparator<Vertex> wow1 = new VertexComparator1I();
		Comparator<Vertex> wow2 = new VertexComparator2I();
		Comparator<Vertex> wow3 = new VertexComparator3I();
		Comparator<Vertex> wow4 = new VertexComparator4I();
		Comparator<Vertex> wow5 = new VertexComparator5I();
		i.manClosed = new ArrayList<Vertex>();
		Vertex start = g.graph[g.start.yPoint][g.start.xPoint];
		Vertex goal = g.graph[g.goal.yPoint][g.goal.xPoint];
		i.manFringe = new PriorityQueue<Vertex>(10, wow1);
		i.dslFringe = new PriorityQueue<Vertex>(10, wow2);
		i.euclidFringe = new PriorityQueue<Vertex>(10, wow3);
		i.octFringe = new PriorityQueue<Vertex>(10, wow4);
		i.chebFringe = new PriorityQueue<Vertex>(10, wow5);
		start.parent = start;
		start.gValue = 0;
		start.hValue1 = hValue(start, g, w1, 1);
		i.manFringe.add(start);
		start.fringe1 = true;
		start.hValue2 = hValue(start, g, w1, 2);
		i.dslFringe.add(start);
		start.fringe2 = true;
		start.hValue3 = hValue(start, g, w1, 3);
		i.euclidFringe.add(start);
		start.fringe3 = true;
		start.hValue4 = hValue(start, g, w1, 4);
		i.octFringe.add(start);
		start.fringe4 = true;
		start.hValue5 = hValue(start, g, w1, 5);
		i.chebFringe.add(start);
		start.fringe5 = true;
		boolean foundPath = false;
		int selector = -1;
		while (!i.manFringe.isEmpty()) {
			for (int a = 1; a < 5; a++) {
				PriorityQueue<Vertex> f = null;
				ArrayList<Vertex> c = null;
				Vertex topMan = i.manFringe.peek();
				Vertex top = null;
				boolean next = false;
				float topKey = -10;
				float topManKey = topMan.gValue + hValue(topMan, g, w1, 1);
				if (a == 1) {
					f = i.dslFringe;
					c = i.closed;
					if (f.isEmpty()) {
						next = true;
					}
					else {
						top = f.peek();
						System.out.println(top);
						topKey = top.gValue + hValue(top, g, w1, 2);
					}
				}
				else if (a == 2) {
					f = i.euclidFringe;
					c = i.closed;
					if (f.isEmpty()) {
						next = true;
					}
					else {
						top = f.peek();
						System.out.println(top);
						topKey = top.gValue + hValue(top, g, w1, 3);
					}
				}
				else if (a == 3) {
					f = i.octFringe;
					c = i.closed;
					if (f.isEmpty()) {
						next = true;
					}
					else {
						top = f.peek();
						System.out.println(top);
						topKey = top.gValue + hValue(top, g, w1, 4);
					}
				}
				else if (a == 4) {
					f = i.chebFringe;
					c = i.closed;
					if (f.isEmpty()) {
						next = true;
					}
					else {
						top = f.peek();
						System.out.println(top);
						topKey = top.gValue + hValue(top, g, w1, 5);
					}
				}
				if ((topKey != -10) && (topKey <= (w2 * topManKey))) {
					Vertex current = f.remove();
					if (a == 1) {
						current.fringe2 = false;
					}
					else if (a == 2) {
						current.fringe3 = false;
					}
					else if (a == 3) {
						current.fringe4 = false;
					}
					else if (a == 4) {
						current.fringe5 = false;
					}
					if (top.coor == goal.coor) {
						foundPath = true;
						selector = a;
						break;
					}
					current.closed = false;
					c.add(current);
					expandStateI(current, g, w1, w2, i, (a + 1));
				}
				else if (next == true) {
					Vertex current = i.manFringe.remove();
					current.fringe1 = false;
					if (topMan.coor == goal.coor) {
						foundPath = true;
						selector = 0;
						break;
					}
					current.closed1 = true;
					i.manClosed.add(current);
					expandStateI(current, g, w1, w2, i, 1);
				}
			}
			if (foundPath == true) {
				break;
			}
		}
		if (foundPath == false) {
			return false;
		}
		else {
			Vertex temp = goal;
			while (temp.parent != temp) {
				if (selector == 0) {
					temp.hValue = hValue(temp, g, w1, 1);
				}
				else if (selector == 1) {
					temp.hValue = hValue(temp, g, w1, 2);
				}
				else if (selector == 2) {
					temp.hValue = hValue(temp, g, w1, 3);
				}
				else if (selector == 3) {
					temp.hValue = hValue(temp, g, w1, 4);
				}
				else if (selector == 4) {
					temp.hValue = hValue(temp, g, w1, 5);
				}
				solution.add(temp);
				temp = temp.parent;
			}
			solution.add(temp);
			return true;
		}
	}
	
	public void expandStateI(Vertex c, Graph g, float w, float w2, Interface i, int h) {
		Vertex[] s = new Vertex[8];
		int xPoint = c.coor.xPoint;
		int yPoint = c.coor.yPoint;
		int numOfS = 0;
		if (c.coor.xPoint == 0) {
			if (c.coor.yPoint == 0) {
				numOfS = 3;
				s[0] = g.graph[yPoint + 1][xPoint];
				s[1] = g.graph[yPoint][xPoint + 1];
				s[2] = g.graph[yPoint + 1][xPoint + 1];
			}
			else if (c.coor.yPoint == 119) {
				numOfS = 3;
				s[0] = g.graph[yPoint - 1][xPoint];
				s[1] = g.graph[yPoint][xPoint + 1];
				s[2] = g.graph[yPoint - 1][xPoint + 1];
			}
			else {
				numOfS = 5;
				s[0] = g.graph[yPoint + 1][xPoint];
				s[1] = g.graph[yPoint][xPoint + 1];
				s[2] = g.graph[yPoint + 1][xPoint + 1];
				s[3] = g.graph[yPoint - 1][xPoint];
				s[4] = g.graph[yPoint - 1][xPoint + 1];
			}
		}
		else if (c.coor.xPoint == 159) {
			if (c.coor.yPoint == 0) {
				numOfS = 3;
				s[0] = g.graph[yPoint + 1][xPoint];
				s[1] = g.graph[yPoint][xPoint - 1];
				s[2] = g.graph[yPoint + 1][xPoint - 1];
			}
			else if (c.coor.yPoint == 119) {
				numOfS = 3;
				s[0] = g.graph[yPoint - 1][xPoint];
				s[1] = g.graph[yPoint][xPoint - 1];
				s[2] = g.graph[yPoint - 1][xPoint - 1];
			}
			else {
				numOfS = 5;
				s[0] = g.graph[yPoint + 1][xPoint];
				s[1] = g.graph[yPoint][xPoint - 1];
				s[2] = g.graph[yPoint + 1][xPoint - 1];
				s[3] = g.graph[yPoint - 1][xPoint - 1];
				s[4] = g.graph[yPoint - 1][xPoint];
			}
		}
		else if (c.coor.yPoint == 0) {
			numOfS = 5;
			s[0] = g.graph[yPoint + 1][xPoint];
			s[1] = g.graph[yPoint + 1][xPoint - 1];
			s[2] = g.graph[yPoint + 1][xPoint + 1];
			s[3] = g.graph[yPoint][xPoint + 1];
			s[4] = g.graph[yPoint][xPoint - 1];
		}
		else if (c.coor.yPoint == 119) {
			numOfS = 5;
			s[0] = g.graph[yPoint - 1][xPoint];
			s[1] = g.graph[yPoint - 1][xPoint - 1];
			s[2] = g.graph[yPoint - 1][xPoint + 1];
			s[3] = g.graph[yPoint][xPoint + 1];
			s[4] = g.graph[yPoint][xPoint - 1];
		}
		else {
			numOfS = 8;
			s[0] = g.graph[yPoint - 1][xPoint];
			s[1] = g.graph[yPoint - 1][xPoint - 1];
			s[2] = g.graph[yPoint - 1][xPoint + 1];
			s[3] = g.graph[yPoint][xPoint + 1];
			s[4] = g.graph[yPoint][xPoint - 1];
			s[5] = g.graph[yPoint + 1][xPoint];
			s[6] = g.graph[yPoint + 1][xPoint - 1];
			s[7] = g.graph[yPoint + 1][xPoint + 1];
		}
		if (h == 1) {
	//		i.manFringe.remove(c);
			i.dslFringe.remove(c);
			i.euclidFringe.remove(c);
			i.octFringe.remove(c);
			i.chebFringe.remove(c);
		}
		else if (h == 2) {
			i.manFringe.remove(c);
	//		i.dslFringe.remove(c);
			i.euclidFringe.remove(c);
			i.octFringe.remove(c);
			i.chebFringe.remove(c);
		}
		else if (h == 3) {
			i.manFringe.remove(c);
			i.dslFringe.remove(c);
	//		i.euclidFringe.remove(c);
			i.octFringe.remove(c);
			i.chebFringe.remove(c);
		}
		else if (h == 4) {
			i.manFringe.remove(c);
			i.dslFringe.remove(c);
			i.euclidFringe.remove(c);
	//		i.octFringe.remove(c);
			i.chebFringe.remove(c);
		}
		else if (h == 5) {
			i.manFringe.remove(c);
			i.dslFringe.remove(c);
			i.euclidFringe.remove(c);
			i.octFringe.remove(c);
	//		i.chebFringe.remove(c);
		}
		for (int j = 0; j < numOfS; j++) {
			float cost = calculateVertexCost(c, s[j]);
			if (s[j].blocked != true) {
				if (s[j].closed1 == false) {
					if (s[j].fringe1 == false) {
						s[j].gValue = Float.MAX_VALUE;
						s[j].parent = null;
					}
				}
				if ((c.gValue + cost) < s[j].gValue) {
					s[j].gValue = c.gValue + cost;
					s[j].parent = c;
					if (s[j].closed1 == false) {
						s[j].hValue1 = hValue(s[j], g, w, 1);
						if (s[j].fringe1 == true) {
							i.manFringe.remove(s[j]);
							s[j].fringe1 = false;
						}
						i.manFringe.add(s[j]);
						s[j].fringe1 = true;
						i.nodesExpanded++;
						if (s[j].closed == false) {
							for (int a = 1; a < 5; a++) {
								float manKey = s[j].gValue + s[j].hValue1;
								if (a == 1) {
									float key = s[j].gValue + hValue(s[j], g, w, 2);
									if (key <= (w2 * manKey)) {
										s[j].hValue2 = hValue(s[j], g, w, 2);
										if (s[j].fringe2 == true) {
											i.dslFringe.remove(s[j]);
											s[j].fringe2 = false;
										}
										i.dslFringe.add(s[j]);
										s[j].fringe2 = true;
									}
								}
								else if (a == 2) {
									float key = s[j].gValue + hValue(s[j], g, w, 3);
									if (key <= (w2 * manKey)) {
										s[j].hValue3 = hValue(s[j], g, w, 3);
										if (s[j].fringe3 == true) {
											i.euclidFringe.remove(s[j]);
											s[j].fringe3 = false;
										}
										i.euclidFringe.add(s[j]);
										s[j].fringe3 = true;
									}
								}
								else if (a == 3) {
									float key = s[j].gValue + hValue(s[j], g, w, 4);
									if (key <= (w2 * manKey)) {
										s[j].hValue4 = hValue(s[j], g, w, 4);
										if (s[j].fringe4 == true) {
											i.octFringe.remove(s[j]);
											s[j].fringe4 = false;
										}
										i.octFringe.add(s[j]);
										s[j].fringe4 = true;
									}
								}
								else if (a == 4) {
									float key = s[j].gValue + hValue(s[j], g, w, 5);
									if (key <= (w2 * manKey)) {
										s[j].hValue5 = hValue(s[j], g, w, 5);
										if (s[j].fringe5 == true) {
											i.chebFringe.remove(s[j]);
											s[j].fringe5 = false;
										}
										i.chebFringe.add(s[j]);
										s[j].fringe5 = true;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	int nodesExpanded;
	static int caseNo;
	PriorityQueue<Vertex> fringe;
	ArrayList<Vertex> closed;
	ArrayList<Vertex> solution;
	
	PriorityQueue<Vertex> manFringe;
	ArrayList<Vertex> manClosed;
//	ArrayList<Vertex> manSol;
	PriorityQueue<Vertex> dslFringe;
	ArrayList<Vertex> dslClosed;
//	ArrayList<Vertex> dslSol;
	PriorityQueue<Vertex> euclidFringe;
	ArrayList<Vertex> euclidClosed;
//	ArrayList<Vertex> euclidSol;
	PriorityQueue<Vertex> octFringe;
	ArrayList<Vertex> octClosed;
//	ArrayList<Vertex> octSol;
	PriorityQueue<Vertex> chebFringe;
	ArrayList<Vertex> chebClosed;
//	ArrayList<Vertex> chebSol;
	
	public Interface() {
		Comparator<Vertex> wow = new VertexComparator();
		fringe = new PriorityQueue<Vertex>(10, wow);
		closed = new ArrayList<Vertex>();
		solution = new ArrayList<Vertex>();
	}
	
	public class VertexComparator implements Comparator<Vertex> {

		public int compare(Vertex o1, Vertex o2) {
			int retVal;
			if ((o1.gValue + o1.hValue) < (o2.gValue + o2.hValue)) {
				retVal = -1;
			}
			else if ((o1.gValue + o1.hValue) > (o2.gValue + o2.hValue)) {
				retVal = 1;
			}
			else {
				retVal = 0;
			}
			return retVal;
		}
		
	}
	
	public class VertexComparator1 implements Comparator<Vertex> {

		public int compare(Vertex o1, Vertex o2) {
			int retVal;
			if ((o1.gValue1 + o1.hValue1) < (o2.gValue1 + o2.hValue1)) {
				retVal = -1;
			}
			else if ((o1.gValue1 + o1.hValue1) > (o2.gValue1 + o2.hValue1)) {
				retVal = 1;
			}
			else {
				retVal = 0;
			}
			return retVal;
		}
		
	}
	
	public class VertexComparator2 implements Comparator<Vertex> {

		public int compare(Vertex o1, Vertex o2) {
			int retVal;
			if ((o1.gValue2 + o1.hValue2) < (o2.gValue2 + o2.hValue2)) {
				retVal = -1;
			}
			else if ((o1.gValue2 + o1.hValue2) > (o2.gValue2 + o2.hValue2)) {
				retVal = 1;
			}
			else {
				retVal = 0;
			}
			return retVal;
		}
		
	}
	
	public class VertexComparator3 implements Comparator<Vertex> {

		public int compare(Vertex o1, Vertex o2) {
			int retVal;
			if ((o1.gValue3 + o1.hValue3) < (o2.gValue3 + o2.hValue3)) {
				retVal = -1;
			}
			else if ((o1.gValue3 + o1.hValue3) > (o2.gValue3 + o2.hValue3)) {
				retVal = 1;
			}
			else {
				retVal = 0;
			}
			return retVal;
		}
		
	}
	
	public class VertexComparator4 implements Comparator<Vertex> {

		public int compare(Vertex o1, Vertex o2) {
			int retVal;
			if ((o1.gValue4 + o1.hValue4) < (o2.gValue4 + o2.hValue4)) {
				retVal = -1;
			}
			else if ((o1.gValue4 + o1.hValue4) > (o2.gValue4 + o2.hValue4)) {
				retVal = 1;
			}
			else {
				retVal = 0;
			}
			return retVal;
		}
		
	}
	
	public class VertexComparator5 implements Comparator<Vertex> {

		public int compare(Vertex o1, Vertex o2) {
			int retVal;
			if ((o1.gValue5 + o1.hValue5) < (o2.gValue5 + o2.hValue5)) {
				retVal = -1;
			}
			else if ((o1.gValue5 + o1.hValue5) > (o2.gValue5 + o2.hValue5)) {
				retVal = 1;
			}
			else {
				retVal = 0;
			}
			return retVal;
		}
		
	}
	
	public class VertexComparator1I implements Comparator<Vertex> {

		public int compare(Vertex o1, Vertex o2) {
			int retVal;
			if ((o1.gValue + o1.hValue1) < (o2.gValue + o2.hValue1)) {
				retVal = -1;
			}
			else if ((o1.gValue + o1.hValue1) > (o2.gValue + o2.hValue1)) {
				retVal = 1;
			}
			else {
				retVal = 0;
			}
			return retVal;
		}
		
	}
	
	public class VertexComparator2I implements Comparator<Vertex> {

		public int compare(Vertex o1, Vertex o2) {
			int retVal;
			if ((o1.gValue + o1.hValue2) < (o2.gValue + o2.hValue2)) {
				retVal = -1;
			}
			else if ((o1.gValue + o1.hValue2) > (o2.gValue + o2.hValue2)) {
				retVal = 1;
			}
			else {
				retVal = 0;
			}
			return retVal;
		}
		
	}
	
	public class VertexComparator3I implements Comparator<Vertex> {

		public int compare(Vertex o1, Vertex o2) {
			int retVal;
			if ((o1.gValue + o1.hValue3) < (o2.gValue + o2.hValue3)) {
				retVal = -1;
			}
			else if ((o1.gValue + o1.hValue3) > (o2.gValue + o2.hValue3)) {
				retVal = 1;
			}
			else {
				retVal = 0;
			}
			return retVal;
		}
		
	}
	
	public class VertexComparator4I implements Comparator<Vertex> {

		public int compare(Vertex o1, Vertex o2) {
			int retVal;
			if ((o1.gValue + o1.hValue4) < (o2.gValue + o2.hValue4)) {
				retVal = -1;
			}
			else if ((o1.gValue + o1.hValue4) > (o2.gValue + o2.hValue4)) {
				retVal = 1;
			}
			else {
				retVal = 0;
			}
			return retVal;
		}
		
	}
	
	public class VertexComparator5I implements Comparator<Vertex> {

		public int compare(Vertex o1, Vertex o2) {
			int retVal;
			if ((o1.gValue + o1.hValue5) < (o2.gValue + o2.hValue5)) {
				retVal = -1;
			}
			else if ((o1.gValue + o1.hValue5) > (o2.gValue + o2.hValue5)) {
				retVal = 1;
			}
			else {
				retVal = 0;
			}
			return retVal;
		}
		
	}
	
	public void readFile(String fileName, Graph g) throws IOException {
		FileReader fr =  new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		line = br.readLine();
		StringTokenizer start = new StringTokenizer(line, ",");
		g.start.xPoint = Integer.parseInt(start.nextToken());
		g.start.yPoint = Integer.parseInt(start.nextToken());
		line = br.readLine();
		StringTokenizer goal = new StringTokenizer(line, ",");
		g.goal.xPoint = Integer.parseInt(goal.nextToken());
		g.goal.yPoint = Integer.parseInt(goal.nextToken());
		line = br.readLine();
		for (int i = 0; i < 8; i++) {
			line = br.readLine();
			StringTokenizer hardCenter = new StringTokenizer(line, ",");
			Coordinate c = new Coordinate();
			c.xPoint = Integer.parseInt(hardCenter.nextToken());
			c.yPoint = Integer.parseInt(hardCenter.nextToken());
			g.usedHardCenters.add(c);
		}
		line = br.readLine();
		for (int i = 0; i < 120; i++) {
			line = br.readLine();
			for (int j = 0; j < 160; j++) {
				char c = line.charAt(j);
				if (c == '0') {
					g.graph[i][j].blocked = true;
				}
				else if (c == '2') {
					g.graph[i][j].hardTraverse = true;
				}
				else if (c == 'a') {
					g.graph[i][j].highway = true;
				}
				else if (c == 'b') {
					g.graph[i][j].highway = true;
					g.graph[i][j].hardTraverse = true;
				}
			}
		}
		br.close();
		fr.close();
	}
	
	public float hValue(Vertex c, Graph g, float w, int h) {
		Vertex goal = g.graph[g.goal.yPoint][g.goal.xPoint];
		if (h == 0) {
			return w * 0;
		}
		else if (h == 1) {
			float manDist = Math.abs(c.coor.xPoint - goal.coor.xPoint) + Math.abs(c.coor.yPoint - goal.coor.yPoint);
			return (float) (w * 0.25 * manDist);
		}
		else if (h == 2) {
			float dist = (float) ((Math.sqrt(2) * (Math.min(Math.abs(c.coor.xPoint - goal.coor.xPoint), Math.abs(c.coor.yPoint - goal.coor.yPoint)))) + (Math.max(Math.abs(c.coor.xPoint - goal.coor.xPoint), Math.abs(c.coor.yPoint - goal.coor.yPoint))) - (Math.min(Math.abs(c.coor.xPoint - goal.coor.xPoint), Math.abs(c.coor.yPoint - goal.coor.yPoint))));
			return (float) (w * 0.25 * dist);
		}
		else if (h == 3) {
			float eucDist = (float) Math.sqrt(Math.pow((c.coor.xPoint - goal.coor.xPoint), 2) + Math.pow((c.coor.yPoint - goal.coor.yPoint), 2));
			return (float) (w * 0.25 * eucDist);
		}
		else if (h == 4) {
			float dx = Math.abs(c.coor.xPoint - goal.coor.xPoint);
			float dy = Math.abs(c.coor.yPoint - goal.coor.yPoint);
			float dist = (float) dx + dy - Math.min(dx, dy);
			return (float) (w * dist);
		}
		else if (h == 5) {
			float dx = Math.abs(c.coor.xPoint - goal.coor.xPoint);
			float dy = Math.abs(c.coor.yPoint - goal.coor.yPoint);
			float dist = (float) (dx + dy - (Math.sqrt(2) - 2) * (Math.min(dx, dy)));
			return (float) (w * dist);
		}
		else if (h == 6) {
			float dx = Math.abs(c.coor.xPoint - goal.coor.xPoint);
			float dy = Math.abs(c.coor.yPoint - goal.coor.yPoint);
			float dist = (float) Math.min((Math.min(Math.min((dx+dy) * 0.25 * w, Math.sqrt(2) * Math.min(dx, dy) + Math.max(dx, dy) - Math.min(dx, dy)), Math.min(Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)), dx + dy - Math.min(dx, dy)))), (dx + dy - (Math.sqrt(2) - 2) * (Math.min(dx, dy))));
			return dist;
		}
		return 0;
	}
	
	public void addVertices(Vertex c, Graph g, float w, int h, Interface j) {
		Vertex[] s = new Vertex[8];
		int xPoint = c.coor.xPoint;
		int yPoint = c.coor.yPoint;
		int numOfS = 0;
		if (c.coor.xPoint == 0) {
			if (c.coor.yPoint == 0) {
				numOfS = 3;
				s[0] = g.graph[yPoint + 1][xPoint];
				s[1] = g.graph[yPoint][xPoint + 1];
				s[2] = g.graph[yPoint + 1][xPoint + 1];
			}
			else if (c.coor.yPoint == 119) {
				numOfS = 3;
				s[0] = g.graph[yPoint - 1][xPoint];
				s[1] = g.graph[yPoint][xPoint + 1];
				s[2] = g.graph[yPoint - 1][xPoint + 1];
			}
			else {
				numOfS = 5;
				s[0] = g.graph[yPoint + 1][xPoint];
				s[1] = g.graph[yPoint][xPoint + 1];
				s[2] = g.graph[yPoint + 1][xPoint + 1];
				s[3] = g.graph[yPoint - 1][xPoint];
				s[4] = g.graph[yPoint - 1][xPoint + 1];
			}
		}
		else if (c.coor.xPoint == 159) {
			if (c.coor.yPoint == 0) {
				numOfS = 3;
				s[0] = g.graph[yPoint + 1][xPoint];
				s[1] = g.graph[yPoint][xPoint - 1];
				s[2] = g.graph[yPoint + 1][xPoint - 1];
			}
			else if (c.coor.yPoint == 119) {
				numOfS = 3;
				s[0] = g.graph[yPoint - 1][xPoint];
				s[1] = g.graph[yPoint][xPoint - 1];
				s[2] = g.graph[yPoint - 1][xPoint - 1];
			}
			else {
				numOfS = 5;
				s[0] = g.graph[yPoint + 1][xPoint];
				s[1] = g.graph[yPoint][xPoint - 1];
				s[2] = g.graph[yPoint + 1][xPoint - 1];
				s[3] = g.graph[yPoint - 1][xPoint - 1];
				s[4] = g.graph[yPoint - 1][xPoint];
			}
		}
		else if (c.coor.yPoint == 0) {
			numOfS = 5;
			s[0] = g.graph[yPoint + 1][xPoint];
			s[1] = g.graph[yPoint + 1][xPoint - 1];
			s[2] = g.graph[yPoint + 1][xPoint + 1];
			s[3] = g.graph[yPoint][xPoint + 1];
			s[4] = g.graph[yPoint][xPoint - 1];
		}
		else if (c.coor.yPoint == 119) {
			numOfS = 5;
			s[0] = g.graph[yPoint - 1][xPoint];
			s[1] = g.graph[yPoint - 1][xPoint - 1];
			s[2] = g.graph[yPoint - 1][xPoint + 1];
			s[3] = g.graph[yPoint][xPoint + 1];
			s[4] = g.graph[yPoint][xPoint - 1];
		}
		else {
			numOfS = 8;
			s[0] = g.graph[yPoint - 1][xPoint];
			s[1] = g.graph[yPoint - 1][xPoint - 1];
			s[2] = g.graph[yPoint - 1][xPoint + 1];
			s[3] = g.graph[yPoint][xPoint + 1];
			s[4] = g.graph[yPoint][xPoint - 1];
			s[5] = g.graph[yPoint + 1][xPoint];
			s[6] = g.graph[yPoint + 1][xPoint - 1];
			s[7] = g.graph[yPoint + 1][xPoint + 1];
		}
		for (int i = 0; i < numOfS; i++) {
			if (s[i].closed == false) {
				if (s[i].fringe == false) {
					s[i].gValue = Float.MAX_VALUE;
					s[i].parent = null;
				}
				updateVertexUniform(c, s[i], w, h, g, j);
			}
		}
	}
	
	public static float calculateVertexCost(Vertex c, Vertex s) {
		float cost = 0;
		int xDifference = Math.abs(c.coor.xPoint - s.coor.xPoint);
		int yDifference = Math.abs(c.coor.yPoint - s.coor.yPoint);
		if (s.blocked == true) {
			cost = Float.MAX_VALUE;
		}
		else if (yDifference == 1 && xDifference == 1) {
			if (c.hardTraverse == true && s.hardTraverse == true) {
				cost = (float) Math.sqrt(8);
			}
			else if (c.hardTraverse == false && s.hardTraverse == false) {
				cost = (float) Math.sqrt(2);
			}
			else {
				cost = (float) ((1.5) * Math.sqrt(2));
			}
		}
		else if ((yDifference == 1 && xDifference == 0) || (yDifference == 0 && xDifference == 1)){
			if (c.highway == true && s.highway == true) {
				if (c.hardTraverse == true && s.hardTraverse == true) {
					cost = (float) 0.5;
				}
				else if (c.hardTraverse == false && s.hardTraverse == false) {
					cost = (float) .25;
				}
				else {
					cost = (float) .375;
				}
			}
			else {
				if (c.hardTraverse == true && s.hardTraverse == true) {
					cost = (float) 2;
				}
				else if (c.hardTraverse == false && s.hardTraverse == false) {
					cost = (float) 1;
				}
				else {
					cost = (float) 1.5;
				}
			}
		}
		return cost;
	}
	
	public void updateVertexUniform(Vertex c, Vertex s, float w, int h, Graph g, Interface i) {
		float cost = calculateVertexCost(c, s);
		if (s.blocked != true) {
			if ((c.gValue + cost) < s.gValue) {
				s.gValue = c.gValue + cost;
				s.hValue = hValue(s, g, w, h);
				s.parent = c;
				if (s.fringe == true) {
					fringe.remove(s);
					s.fringe = false;
				}
				fringe.add(s);
				i.nodesExpanded++;
				s.fringe = true;
			}
		}
	}
	
	public boolean algorithm(Graph g, float w, int h, Interface i) {
		Vertex start = g.graph[g.start.yPoint][g.start.xPoint];
		Vertex goal = g.graph[g.goal.yPoint][g.goal.xPoint];
		start.parent = start;
		start.gValue = 0;
		start.hValue = hValue(start, g, w, h);
		fringe.add(start);
		start.fringe = true;
		boolean foundPath = false;
		while (!fringe.isEmpty()) {
			Vertex current = fringe.remove();
			current.fringe = false;
			if (current.coor == goal.coor) {
				foundPath = true;
				break;
			}
			closed.add(current);
			current.closed = true;
			addVertices(current, g, w, h, i);
		}
		if (foundPath == false) {
			return false;
		}
		else {
			Vertex temp = goal;
			while (temp.parent != temp) {
				solution.add(temp);
				temp = temp.parent;
			}
			solution.add(temp);
			return true;
		}
	}
	
	public int printSolution() throws IOException {
		FileWriter fw = new FileWriter(directory + "Paths/Case" + caseNo + "_out.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		for (int i = solution.size() - 1; i > -1; i--) {
			bw.write(solution.get(i).coor.xPoint + "," + solution.get(i).coor.yPoint + "," + solution.get(i).gValue + "," + solution.get(i).hValue);
			bw.newLine();
	//		System.out.println(solution.get(i).xPoint + "," + solution.get(i).yPoint);
		}
		bw.close();
		fw.close();
		return solution.size();
	}
	
	public int printESolution() throws IOException {
		FileWriter fw = new FileWriter(directory + "Paths/Case" + caseNo + "_out.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		for (int i = solution.size() - 1; i > -1; i--) {
			bw.write(solution.get(i).coor.xPoint + "," + solution.get(i).coor.yPoint + "," + solution.get(i).gValue + "," + solution.get(i).hValue);
			bw.newLine();
	//		System.out.println(solution.get(i).xPoint + "," + solution.get(i).yPoint);
		}
		bw.close();
		fw.close();
		return solution.size();
	}
	
}
