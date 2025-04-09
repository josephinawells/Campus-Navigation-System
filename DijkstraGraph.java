// === CS400 File Header Information ===
// Name: Josey Wells
// Email: jmwells5 @wisc.edu 
// Group and Team: <your group name: two letters, and team color>
// Group TA: <name of your group's ta>
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.ArrayList;
/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     */
    public DijkstraGraph() {
        super(new PlaceholderMap<>());
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
	//checks that the start and end nodes exist and if they dont exception is thrown
        if(this.containsNode(start) == false || this.containsNode(end) == false){
	    throw new NoSuchElementException("Start or End no was not found");
	}
	PriorityQueue<SearchNode> queue = new PriorityQueue<>();
	PlaceholderMap<Node, Node> seen = new PlaceholderMap<>();

	Node startingNode = this.nodes.get(start);
	Node endingNode = this.nodes.get(end);

	//searches the nodes with a cost of 0 and no predecessor
	queue.add(new SearchNode(startingNode, 0.0, null));

	while(queue.isEmpty() == false){
	    //finds the node with the lowest cost
	    SearchNode on = queue.remove();

	    if(seen.containsKey(on.node)){
		continue;
	    }
	    //gets marked as a visited node
	    seen.put(on.node, on.node);

	    if(on.node == endingNode){
		return on;
	    }
	    //searches through the edges 
	    for(Edge edge : on.node.edgesLeaving){
		Node successor = edge.successor;
		//only goes to the unvistster successors
		if(seen.containsKey(successor) == false){
		    double cost = on.cost + edge.data.doubleValue();
		    //adds to the priority queue
		    queue.add(new SearchNode(successor, cost, on));
		}
	    }   
	
	}

	//if no path was found excpetion is thrown
    throw new NoSuchElementException("No path was found from " + start + " to " + end);
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        List<NodeType> shortPath = new ArrayList<>();

	try{
	    //computes the shortestPath
	    SearchNode on = computeShortestPath(start, end);

	    //gives teh path in reverse so it has it from start to end
	    LinkedList<NodeType> list = new LinkedList<>();
	    while(on != null){
		//puts the node into the stack
		list.push(on.node.data);
		on = on.predecessor;
	    }
	    shortPath.addAll(list);
	}catch (NoSuchElementException e){
	    //if there is no path then an empty list will be returned
	    return new ArrayList<>();
	}
	
        return shortPath;
	
    }
    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
	try{
	    //computes the shortest path and gives the cost
	    SearchNode sum = computeShortestPath(start,end);
	    return sum.cost;
	}catch(NoSuchElementException e){
	    //thrown if no path exists
	    throw new NoSuchElementException("No path found");
	}

    }

    

    //Tests that the graph prints the correct things
    @Test
    public void test1(){
	DijkstraGraph<String, Integer> graph = new DijkstraGraph<String,Integer>();

	//adds the nodes
	graph.insertNode("D");
	graph.insertNode("G");
	graph.insertNode("F");
	graph.insertNode("A");
	graph.insertNode("B");
	graph.insertNode("L");
	graph.insertNode("H");
	graph.insertNode("M");
	graph.insertNode("I");
	graph.insertNode("E");

	//add edges
        graph.insertEdge("D", "G", 2);
        graph.insertEdge("D", "F", 4);
        graph.insertEdge("D", "A", 7);
        graph.insertEdge("G", "A", 4);
        graph.insertEdge("G", "L", 7);
        graph.insertEdge("G", "H", 9);
        graph.insertEdge("F", "G", 9);
        graph.insertEdge("A", "B", 1);
        graph.insertEdge("A", "H", 7);
        graph.insertEdge("A", "M", 5);
        graph.insertEdge("B", "M", 3);
        graph.insertEdge("H", "I", 2);
        graph.insertEdge("H", "L", 2);
        graph.insertEdge("M", "I", 4);
        graph.insertEdge("M", "E", 3);
        graph.insertEdge("M", "F", 4);
        graph.insertEdge("I", "H", 2);
        graph.insertEdge("I", "D", 1);


	//creates list of expected and actual values
	List<String> paths = graph.shortestPathData("D", "I");
	List<String> expected = new ArrayList<>();

	expected.add("D");
        expected.add("G");
        expected.add("H");
        expected.add("I");

	//checks if the expected equals the given output
	Assertions.assertEquals(expected,paths);
	Assertions.assertEquals(13, graph.shortestPathCost("D", "I"));
    }

    //tests the different costs and paths
    @Test
    public void test2(){
	 DijkstraGraph<String, Integer> graph = new DijkstraGraph<String,Integer>();

        //adds the nodes                                                                                     
        graph.insertNode("D");
        graph.insertNode("G");
        graph.insertNode("F");
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("L");
        graph.insertNode("H");
        graph.insertNode("M");
        graph.insertNode("I");
        graph.insertNode("E");

	 //add edges                                                                                          
        graph.insertEdge("D", "G", 2);
        graph.insertEdge("D", "F", 4);
        graph.insertEdge("D", "A", 7);
        graph.insertEdge("G", "A", 4);
        graph.insertEdge("G", "L", 7);
        graph.insertEdge("G", "H", 9);
        graph.insertEdge("F", "G", 9);
        graph.insertEdge("A", "B", 1);
        graph.insertEdge("A", "H", 7);
        graph.insertEdge("A", "M", 5);
        graph.insertEdge("B", "M", 3);
        graph.insertEdge("H", "I", 2);
        graph.insertEdge("H", "L", 2);
        graph.insertEdge("M", "I", 4);
        graph.insertEdge("M", "E", 3);
        graph.insertEdge("M", "F", 4);
        graph.insertEdge("I", "H", 2);
        graph.insertEdge("I", "D", 1);

	//creates list of expected and actual
	List<String> paths = graph.shortestPathData("F", "E");
	List<String> expected = new ArrayList<>();

	expected.add("F");
	expected.add("G");
	expected.add("A");
        expected.add("B");
        expected.add("M");
        expected.add("E");

	//checks that the expected matches the actual output
        Assertions.assertEquals(expected,paths);
        Assertions.assertEquals(20, graph.shortestPathCost("F", "E"));
    }

    //tests when there is no path
    @Test
    public void test3(){
	 DijkstraGraph<String, Integer> graph = new DijkstraGraph<String,Integer>();

        //adds the nodes                                                                                    
                                                                                                            
        graph.insertNode("D");
        graph.insertNode("G");
        graph.insertNode("F");
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("L");
        graph.insertNode("H");
        graph.insertNode("M");
        graph.insertNode("I");
        graph.insertNode("E");

	   //add edges                                                                                
                                                                                                       
        graph.insertEdge("D", "G", 2);
        graph.insertEdge("D", "F", 4);
        graph.insertEdge("D", "A", 7);
        graph.insertEdge("G", "A", 4);
        graph.insertEdge("G", "L", 7);
        graph.insertEdge("G", "H", 9);
        graph.insertEdge("F", "G", 9);
        graph.insertEdge("A", "B", 1);
        graph.insertEdge("A", "H", 7);
        graph.insertEdge("A", "M", 5);
        graph.insertEdge("B", "M", 3);
        graph.insertEdge("H", "I", 2);
        graph.insertEdge("H", "L", 2);
        graph.insertEdge("M", "I", 4);
        //dont include to edge to node e
        graph.insertEdge("M", "F", 4);
        graph.insertEdge("I", "H", 2);
        graph.insertEdge("I", "D", 1);

	//create the list of the path

	List<String> paths = graph.shortestPathData("D", "E");
	Assertions.assertTrue(paths.isEmpty());

	try{
	    graph.shortestPathCost("D","E");
	    //false if exception is not thrown
	    Assertions.assertTrue(false);
	}catch(NoSuchElementException e){
	    Assertions.assertTrue(true);
	}
    }
}
