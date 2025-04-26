import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Backend implementation that uses a GraphADT to manage campus locations
 * and find paths between them.
 */
public class Backend implements BackendInterface {

    private GraphADT<String, Double> graph;

    /**
     * Constructor for the Backend class 
     * @param graph object to store the backend's graph data
     */
    public Backend(GraphADT<String, Double> graph) {
	this.graph = graph;
    }
    /**
     * Loads graph data from a dot file. If a graph was previously loaded, this
     * method will first delete the contents (nodes and edges) of the existing
     * graph before loading a new one.
     * @param filename the path to a dot file to read graph data from
     * @throws IOException if there was any problem reading from this file
     */
    @Override
    public void loadGraphData(String filename) throws IOException {
	// remove all nodes first to clear graph
	List<String> originalNodes = new ArrayList<>(graph.getAllNodes());
	for (String node : originalNodes) {
	    graph.removeNode(node);
	}
	
	BufferedReader reader = null;
	
	try {
	    reader = new BufferedReader(new FileReader(filename));
	    String line;
	    while ((line = reader.readLine()) != null) {
		line = line.trim();

		// skip lines with no edge
		if (!line.contains("->") || !line.contains("[seconds=")) {
		    continue;
		}

		// start (pointA)
		int q1Index = line.indexOf('"');
		int q2Index = line.indexOf('"', q1Index + 1);
		if (q1Index == -1 || q2Index == -1) {
		    continue;  // pass over lines that do not have quotes (error in format)
		}
		String pointA = line.substring(q1Index + 1, q2Index);

		// end (pointB)
		int q3Index = line.indexOf('"', q2Index + 1);
		int q4Index = line.indexOf('"', q3Index + 1);
		if (q3Index == -1 || q4Index == -1) {
		    continue;
		}
		String pointB = line.substring(q3Index + 1, q4Index);

		// find weights
		int secondsStartIndex = line.indexOf("[seconds=") + 9;
		int secondsEndIndex = line.indexOf("]", secondsStartIndex);
		if (secondsStartIndex == -1 || secondsEndIndex == -1) {
		    continue;  // skip erors
		}

		try {
		    Double seconds = Double.parseDouble(line.substring(secondsStartIndex, secondsEndIndex));

		    // insert nodes that do not exist
		    if (!graph.containsNode(pointA)) {
			graph.insertNode(pointA);
		    }
		    if (!graph.containsNode(pointB)) {
			graph.insertNode(pointB);
		    }

		    // insert edge and edge weight
		    graph.insertEdge(pointA, pointB, seconds);
		} catch (NumberFormatException e) {
		    // skip invalid 
		    continue;
		}
	    }
	} finally {
	    // close read regardless of exceptions
	    if (reader != null) {
		try {
		    reader.close();
		} catch (IOException e) {
		    // handle exception in closing
		    System.err.println("Reader Close Error: " + e.getMessage());
		}
	    }
	}
    }

    /**
     * Returns a list of all locations (node data) available in the graph.
     * @return list of all location names
     */
    @Override
    public List<String> getListOfAllLocations() {
	return graph.getAllNodes();
    }

    /**
     * Return the sequence of locations along the shortest path from
     * startLocation to endLocation, or an empty list if no such path exists. 
     * @param startLocation the start location of the path
     * @param endLocation the end location of the path
     * @return a list with the nodes along the shortest path from startLocation to endLocation, or an empty list if no such path exists
     */
    @Override
    public List<String> findLocationsOnShortestPath(String startLocation, String endLocation) {
	try {
	    return graph.shortestPathData(startLocation, endLocation);
	} catch (NoSuchElementException e) {
	    // return an empty list if no path exists
	    return new ArrayList<>();
	}
    }

    /**
     * Return the walking times in seconds between each two nodes on the
     * shortest path from startLocation to endLocation, or an empty list of no
     * such path exists.
     * @param startLocation the start location of the path
     * @param endLocation the end location of the path
     * @return a list with the walking times in seconds between two nodes along
     *         the shortest path from startLocation to endLocation, or an empty
     *         list if no such path exists
     */
    @Override
    public List<Double> findTimesOnShortestPath(String startLocation, String endLocation) {
	List<Double> times = new ArrayList<>();
	try {
	    List<String> locationsPath = graph.shortestPathData(startLocation, endLocation);

	    // calculate the time by adding between following locations
	    for (int i = 0; i < locationsPath.size() - 1; i++) {
		String current= locationsPath.get(i);
		String next = locationsPath.get(i+1);

		times.add(graph.getEdge(current, next));
	    }

	    return times;
	} catch (NoSuchElementException e) {
	    // if no path exists return an empty list
	    return times;
	}
    }

    /**
     * Returns the most distant location (the one that takes the longest time to
     * reach) when comparing all shortest paths that begin from the provided
     * startLocation.
     * @param startLocation the location to find the most distant location from
     * @return the most distant location (the one that takes the longest time to
     *         reach which following the shortest path)
     * @throws NoSuchElementException if startLocation does not exist, or if
     *         there are no other locations that can be reached from there
     */
    @Override
    public String getFurthestDestinationFrom(String startLocation) throws NoSuchElementException {
	if (!graph.containsNode(startLocation)) {
	    throw new NoSuchElementException("Start location does not exist in the graph");
	}

	List<String> allNodes = graph.getAllNodes();
	String furthest = null; // stores intialy furthest node
	double maxDistance = Double.MIN_VALUE; //any distance will be larger

	// iterate through all nodes to find furthest one
	for (int i = 0; i < allNodes.size(); i++) {
	    String destination = allNodes.get(i);
	    // skip starting location
	    if (destination.equals(startLocation)) {
		continue; 
	    }
	    try {
		double distance = graph.shortestPathCost(startLocation, destination);
		if (distance > maxDistance) {
		    maxDistance = distance;
		    furthest = destination;
		}
	    } catch (NoSuchElementException e) {
		// if a destination cannot be reached, then skip it an carry on
		// this happens when nodes are disconnected from the start node
	    }
	}

	if (furthest == null) {
	    throw new NoSuchElementException("The start location reaches no other locations");
	}

	return furthest;
    }
}
