package planner;

public class Edge {
	Station vertexOne;
	Station vertexTwo;
	int weight;
	public Edge(Station vertexOne, Station vertexTwo) {
		super();
		this.vertexOne = vertexOne;
		this.vertexTwo = vertexTwo;
		this.weight = getWeight(vertexOne, vertexTwo);
	}
	

	public int getWeight(Station vertexOne, Station vertexTwo) {
		//todo
		return 0;
	}
}
