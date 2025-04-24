import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;
public class Frontend implements FrontendInterface{
    /**
       This class is the Frontend of the project and loads the user information on the screen on how to load 
       information on the screen with instructions
     **/
    private BackendInterface backend;
    
    /**
     * Constructor that loads the backend interface to the frontend and creates a frontend 
     * @param backend is used for shortest path computations
     */
    public Frontend(BackendInterface backend){
	this.backend = backend;
	try{
	    backend.loadGraphData("campus.dot");
	}catch (IOException e){
	    System.err.println("Error loading graph data: " + e.getMessage());
	}
    }
    
    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a text input field with the id="start", for the start location
     * - a text input field with the id="end", for the destination
     * - a button labelled "Find Shortest Path" to request this computation
     * Ensure that these text fields are clearly labelled, so that the user
     * can understand how to use them.
     * @return an HTML string that contains input controls that the user can
     *         make use of to request a shortest path computation
     */
    @Override
    public String generateShortestPathPromptHTML(){
	//returns a prompt for the user to get the shortest path
	return "<form>"+
	   "<label for ='start'>Start Location:</label>"+
	    "<input type = 'text' id ='start' name = 'start'>"+
	    "<label for ='end'> End location:</label>"+
	    "<input type = 'text' id ='end' name = 'end'>"+
	    "<button type = 'submit'>Find shortest path</button>"+
	   " </form>";			  
    }

    /**
       Private helper method that helps the organization and efficiany of the code
     **/
    private String generateString(List<String> items){
	StringBuilder stringHtml = new StringBuilder("<ol>");

	for(String string : items){
	    stringHtml.append("<li>").append(items).append("</li>");
	}

	stringHtml.append("</ol>");

	return stringHtml.toString();
    }
    
    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a paragraph (p) that describes the path's start and end locations
     * - an ordered list (ol) of locations along that shortest path
     * - a paragraph (p) that includes the total travel time along this path
     * Or if there is no such path, the HTML returned should instead indicate 
     * the kind of problem encountered.
     * @param start is the starting location to find a shortest path from
     * @param end is the destination that this shortest path should end at
     * @return an HTML string that describes the shortest path between these
     *         two locations
     */
    @Override
    public String generateShortestPathResponseHTML(String start, String end){
	//Lists that hold the location and time from the shortest path called from the backend
	List<String> location = backend.findLocationsOnShortestPath(start,end);
	List<Double> time = backend.findTimesOnShortestPath(start,end);

	if(location.isEmpty()){
	    return "<p>No path found between " + start + " and " + end + ". Please check the locations and try again.</p>";
	}

	//creates an ordered list to store the locations in the shortest path
	String path = generateString(location);

     	double totalTime = 0;
	for(double times: time){
	    totalTime += times;
	}

	//gives the user the shortest path and the time traveled it took to get from start to end
	return "<p>Shortest path from " + start + " to " + end + "</p>" + path +
	    "<p> Total travel time: " + totalTime + " seconds.</p>";
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a text input field with the id="from", for the start location
     * - a button labelled "Furthest Destination From" to submit this request
     * Ensure that this text field is clearly labelled, so that the user
     * can understand how to use it.
     * @return an HTML string that contains input controls that the user can
     *         make use of to request a furthest destination calculation
     */
    @Override
    public String generateFurthestDestinationFromPromptHTML(){
	//gives the user a prompt to get the furthest destination from a starting point
	return "<form>"+
           "<label for ='from'>Start Location:</label>"+
            "<input type = 'text' id = 'from' name = 'from'>"+
            "<button type = 'submit'>Furthest Destination From</button>"+
           " </form>";
    }
    
    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a paragraph (p) that describes the starting point being searched from
     * - a paragraph (p) that describes the furthest destination found
     * - an ordered list (ol) of locations on the path between these locations
     * Or if there is no such destination, the HTML returned should instead 
     * indicate the kind of problem encountered.
     * @param start is the starting location to find the furthest dest from
     * @return an HTML string that describes the furthest destination from the
     *        specified start location
     */
    @Override
    public String generateFurthestDestinationFromResponseHTML(String start){
	try{
	//creates a String of furthest Destination from the backend
	//along with the locations on the shortest path from the backend
	String furthestDestination = backend.getFurthestDestinationFrom(start);
	List<String> path = backend.findLocationsOnShortestPath(start, furthestDestination);

	if(path.isEmpty()){
	    return "<p>No valid path found from " + start + " to furthest destination</p>";
        }

	//creates an ordered list for the path from the furthest destination
	String paths = generateString(path);

	return "<p>Starting from: " + start + "</p>" +
            "<p>Furthest reachable destination: " + furthestDestination + "</p>" +  paths;
	}catch(NoSuchElementException e){
	    throw new NoSuchElementException("Start location does not exist");
	}
    }
	
 
}
