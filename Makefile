startServer:
	javac -cp .:../junit5.jar WebApp.java Backend.java Frontend.java DijkstraGraph.java HashtableMap.java
	java -cp .:../junit5.jar WebApp 3000

runAllTests:
	javac -cp .:../junit5.jar WebApp.java Backend.java Frontend.java BackendTests.java FrontendTests.java DijkstraGraph.java HashtableMap.java
	java -jar ../junit5.jar -cp . -c BackendTests
	java -jar ../junit5.jar -cp. -c FrontendTests

clean:
	rm -f *.class
