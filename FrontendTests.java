import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
   This class tests the methods from the Frontend class to make sure they work as expected
 **/
public class FrontendTests{

    /**
       This tests the genereateShortestPathPromptHTML() method from the frontend class and makes sure the expected
       output is given
     **/

    @Test
    public void roleTest1(){
	//creates a backend and frontend object
	BackendInterface backend = new Backend_Placeholder(new Graph_Placeholder());
	Frontend frontend = new Frontend(backend);

	//contains the output from the method
	String actualHTML = frontend.generateShortestPathPromptHTML();

	//makes sure the String contains the correct output
	assertTrue(actualHTML.contains("<label for ='start'>Start Location:</label>"));
	assertTrue(actualHTML.contains("<input type = 'text' id ='start' name = 'start'>"));
        assertTrue(actualHTML.contains("<label for ='end'> End location:</label>"));
	assertTrue(actualHTML.contains("<input type = 'text' id ='end' name = 'end'>"));
	assertTrue(actualHTML.contains("<button type = 'submit'>Find shortest path</button>"));
    }

    /**
       This tests the generateShortestPathResponseHTML method from the frontend class and makes sure the expected
       output is given
     **/
    @Test
    public void roleTest2(){
	//creates backend and frontend objects
	BackendInterface backend = new Backend_Placeholder(new Graph_Placeholder());
        Frontend frontend = new Frontend(backend);

	//contains the output from the method
	String actualHTML = frontend.generateShortestPathResponseHTML("Start", "End");

	//When there is no path output should be this
	if(actualHTML.contains("No path found")){
		assertTrue(actualHTML.contains("<p>No path found between Start and End."));

	//expected output when there is a path	
	}else{
		assertTrue(actualHTML.contains("<ol>"));
		assertTrue(actualHTML.contains("</ol>"));
		assertTrue(actualHTML.contains("<p>"));
		assertTrue(actualHTML.contains("</p>"));
		assertTrue(actualHTML.contains("Total travel time: "));
	    }
    }	    

    @Test
    /**
       Tests the genereateFurthestDetintaionFromPromptHTML method from the frontend class and makes sure the
       expected output is given
     **/
    public void roleTest3(){
	//creates backend and frontend objects
	BackendInterface backend = new Backend_Placeholder(new Graph_Placeholder());
        Frontend frontend = new Frontend(backend);

	//contains the output from the method
        String actualHTML = frontend.generateFurthestDestinationFromPromptHTML();

	//makes sure the output given is what's expected from the method
        assertTrue(actualHTML.contains("<label for ='from'>Start Location:</label>"));
        assertTrue(actualHTML.contains("<input type = 'text' id = 'from' name = 'from'>"));
	assertTrue(actualHTML.contains("<button type = 'submit'>Furthest Destination From</button>"));

    }

    @Test
    /**
       Tests the generateFurthestDestinationFromResponseHTML method from the frontend class and makes sure the 
       expected output is given
     **/
    public void roleTest4(){
	//creates a backend and frontend object
	BackendInterface backend = new Backend_Placeholder(new Graph_Placeholder());
        Frontend frontend = new Frontend(backend);

	//contains output from the method
	String actualHTML = frontend.generateFurthestDestinationFromResponseHTML("Start");

	//tests that when there is nothing in the path  the correct output is given
	if(actualHTML.contains("No valid path found")){
		assertTrue(actualHTML.contains("<p>No valid path found from Start to furthest destination</p>"));
       	//when there is a valid path this should be the output given
	}else{
		assertTrue(actualHTML.contains("<ol>"));
		assertTrue(actualHTML.contains("</ol>"));
		assertTrue(actualHTML.contains("<p>"));
		assertTrue(actualHTML.contains("</p>"));
		assertTrue(actualHTML.contains("Furthest reachable destination: "));
	}
    }

    /**
       This is testsing the getListOfALlLocations() method is the backend in checking that there is an output given by the backend method
     **/
    @Test
    public void IntegrationTest1(){
	//creates the two objects
	Backend backend = new Backend(new DijkstraGraph<>());
	Frontend frontend = new Frontend(backend);

	//the List of locations
	List<String> locations = backend.getListOfAllLocations();

	//checks that there are locations in the list and that is not empty
	assertNotNull(locations);
	assertFalse(locations.isEmpty(), "Graph should not be empty");
    }

    /**
       This tester is checking that the frontend generate shortest path response is outputing the correct
       response along with checking that the backend is generating the correct shortest path 
     **/
    @Test
    public void IntegrationTest2(){
	//creates the two obejects
	Backend backend = new Backend(new DijkstraGraph<>());
        Frontend frontend = new Frontend(backend);

	
	String start = "Memorial Union";
	String end = "Science Hall";

	try{

	    List<Double> time = backend.findTimesOnShortestPath(start, end);

	    //checks the list contains tehe correct information                                                                 
	    assertEquals(1, time.size(), "Expected on time between the two locations");
	    assertEquals(105.8, time.get(0), 0.001, "Expected walking time incorrect");
	    //gets the output from frontend
	    String output = frontend.generateShortestPathResponseHTML(start,end);

	    //checks the output is what is expected
	    assertFalse(output.contains("No path found"), "Valid path should be found");
	    assertTrue(output.contains("<ol>"), "Output should have an ordered list");
	    assertTrue(output.contains("<p>"), "Output should have a paragraph");
	}catch(NoSuchElementException e){
	    //should not be thrown as locations do exist
	    fail("Start or end location do not exist in graph");
	}
    }

    /**
       This test is checking that the generateFurtehstDestinationFromResponseHTML is producing the correct output and that the backend is producing the expected furthest 
     **/
    @Test
    public void IntegrationTest3(){
	//creates teh objects
	Backend backend = new Backend(new DijkstraGraph<>());
        Frontend frontend = new Frontend(backend);

	String start = "Memorial Union";

	try{
	    //creates the output from frontend
	    String output = frontend.generateFurthestDestinationFromResponseHTML(start);

	    //checks the output is what is expected
	    assertTrue(output.contains("Starting from: " + start), "Should show starting location");
	    assertTrue(output.contains("<ol>"), "Output should included ordered list");
	}catch(NoSuchElementException e){
	    //should not be thrown as the location does exist
	    fail("Start location does not exist in the graph");
	}

	//should be thrown as this location does not exist
        assertThrows(NoSuchElementException.class, () -> {
		frontend.generateFurthestDestinationFromResponseHTML("Nolocation");
	    },"Should throw error as this location does not exist");
    }

    /**
       Checks that the getFurthestDestinationFrom Method is working as expected
     **/
    @Test
    public void IntegrationTest4(){
	//creates objects
	Backend backend = new Backend(new DijkstraGraph<>());
        Frontend frontend = new Frontend(backend);

	//the two locations we want
	String start = "Memorial Union";	
	try{
	    //gets the backend information 
	    String location = backend.getFurthestDestinationFrom(start);	    
	    assertEquals("Smith Residence Hall", location, "Smith Residence Hall Expected as the furthest"); 
	}catch(NoSuchElementException e){
		fail("Start location does not exist in graph");
	    }
    }   
}
