package search;

public class Vertex {
	boolean blocked;
	boolean hardTraverse;
	boolean highway;
	boolean fringe;
	boolean closed;
	boolean fringe1;
	boolean closed1;
	boolean fringe2;
	boolean closed2;
	boolean fringe3;
	boolean closed3;
	boolean fringe4;
	boolean closed4;
	boolean fringe5;
	boolean closed5;
	float gValue;
	float hValue;
	float gValue1;
	float gValue2;
	float gValue3;
	float gValue4;
	float gValue5;
	float hValue1;
	float hValue2;
	float hValue3;
	float hValue4;
	float hValue5;
	Coordinate coor;
	Vertex parent;
	Vertex parent1;
	Vertex parent2;
	Vertex parent3;
	Vertex parent4;
	Vertex parent5;
	
	public Vertex() {
		blocked = false;
		hardTraverse = false;
		highway = false;
		fringe = false;
		closed = false;
		fringe1 = false;
		closed1 = false;
		fringe2 = false;
		closed2 = false;
		fringe3 = false;
		closed3 = false;
		fringe4 = false;
		closed4 = false;
		fringe5 = false;
		closed5 = false;
		gValue = Float.MAX_VALUE;
		hValue = 1000000;
		gValue1 = Float.MAX_VALUE;
		hValue1 = 1000000;
		gValue2 = Float.MAX_VALUE;
		hValue2 = 1000000;
		gValue3 = Float.MAX_VALUE;
		hValue3 = 1000000;
		gValue4 = Float.MAX_VALUE;
		hValue4 = 1000000;
		gValue5 = Float.MAX_VALUE;
		hValue5 = 1000000;
		coor = new Coordinate();
		coor.xPoint = 0;
		coor.yPoint = 0;
		parent = null;
		parent1 = null;
		parent2 = null;
		parent3 = null;
		parent4 = null;
		parent5 = null;
	}
	
	public Vertex(boolean blocked, boolean hardTraverse, boolean highway, boolean fringe, boolean closed, float gValue, float hValue, int xPoint, int yPoint, Vertex parent) {
		this.blocked = blocked;
		this.hardTraverse = hardTraverse;
		this.highway = highway;
		this.fringe = fringe;
		this.closed = closed;
		this.gValue = gValue;
		this.hValue = hValue;
		this.coor.xPoint = xPoint;
		this.coor.yPoint = yPoint;
		this.parent = parent;
	}
}