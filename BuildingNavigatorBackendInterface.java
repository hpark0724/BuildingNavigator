import java.io.FileNotFoundException;
import java.util.List;

/*
 * This interface is a BackendInterface for the BuildingNavigatorBackend class
 */
public interface BuildingNavigatorBackendInterface {
    // public BuildingNavigatorBackendBD
    // (DijkstraInterface<BuildingInterface,Double> dijkstra, MapReaderInterface
    // reader)

    // load and store the data into the graph
    public void loadData(String filename) throws FileNotFoundException;

    // adds a building into the graph
    public boolean addBuilding(String building) throws NullPointerException;

    // checks if a building is in the graph
    public boolean containsBuilding(String building) throws NullPointerException;

    // returns the number of buildings in the graph
    public int getNumBuildings();

    // returns the cost of the shortest path
    public double getShortestCost(String startBuilding, String endBuilding, String... stops)
            throws NullPointerException, IllegalArgumentException;

    // adds a road between two buildings (going both ways) along with distance
    public boolean insertRoad(String startBuilding, String endBuilding, double distance)
            throws NullPointerException, IllegalArgumentException;

    // checks if a road directly connecting two buildings is in the graph
    public boolean containsRoad(String startBuilding, String endBuilding) throws NullPointerException;

    // returns the number of roads in the graph
    public int getNumRoads();

    // returns the shortest path between two buildings (as well as potential stops)
    public String getShortestPath(String startBuilding, String endBuilding, String... stops)
            throws NullPointerException, IllegalArgumentException;

    // returns the list of the buildings in the graph
    public List<String> listBuilding() throws NullPointerException;

}
