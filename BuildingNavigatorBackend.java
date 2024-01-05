import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

public class BuildingNavigatorBackend implements BuildingNavigatorBackendInterface {

    private DijkstraGraph<BuildingInterface, Double> dijkstraGraph; // DijstraGraph object
    private MapReaderInterface buildingData; // MapReaderInterface object

    /**
     * The constructor for BuildingNavigatorBackend class in which the nodeType is
     * the DijkstraGraphBD, and the EdgeType is Double
     *
     * @param dijkstraGraph The DijstraGraph object
     * @param buildingData  the MapReaderInterface object
     */
    public BuildingNavigatorBackend(DijkstraGraph<BuildingInterface, Double> dijkstraGraph,
            MapReaderInterface buildingData) {
        this.dijkstraGraph = dijkstraGraph;
        this.buildingData = buildingData;
    }

    /**
     * read the filename and load the data into the graph
     *
     * @param filename the filename to load
     * @throws FileNotFoundException when the file does not exist
     */
    public void loadData(String filename) throws FileNotFoundException {
        dijkstraGraph = buildingData.read(filename);

    }

    /**
     * adds a building into the graph
     * 
     * @param building the name of the building
     * @throws NullPointerException when the building name (input) is null
     */
    @Override
    // adds a building into the graph
    public boolean addBuilding(String building) throws NullPointerException {

        if (building == null || building == "") {
            throw new NullPointerException("building cannot be empty");
        }
        // initialize the buildingInterface with the name of the building
        BuildingInterface toAdd = new Building(building);

        // add the building to the graph
        return dijkstraGraph.insertNode(toAdd);
    }

    /**
     * inserts the road into the graph
     * 
     * @throws NullPointerException     when the building name (input) is null
     * @throws IllegalArgumentException when the distance is not a positive number
     */
    @Override
    // adds a road between two buildings (going both ways) along with distance
    public boolean insertRoad(String startBuilding, String endBuilding, double distance)
            throws NullPointerException, IllegalArgumentException {
        if (startBuilding == null || endBuilding == null || startBuilding == "" || endBuilding == "") {
            throw new NullPointerException("Start or end building cannot be empty");
        }
        // initialize the buildingInterface with the name of the building
        BuildingInterface predecessor = new Building(startBuilding);
        BuildingInterface successor = new Building(endBuilding);
        BuildingInterface searchPred = dijkstraGraph.getNode(predecessor);
        BuildingInterface searchSucc = dijkstraGraph.getNode(successor);
        if (searchPred == null || searchSucc == null) {
            throw new NullPointerException("the building doesn't exist in the map");
        }
        // add the road to the graph
        return dijkstraGraph.insertEdge(searchPred, searchSucc, distance);

    }

    /**
     * checks if a building is in the graph
     * 
     * @param building the name of the building
     * @throws NullPointerException when the building name (input) is null
     */
    public boolean containsBuilding(String building) throws NullPointerException {
        if (building == null || building == "") {
            throw new NullPointerException("building cannot be empty");
        }
        // initialize the buildingInterface with the name of the building
        BuildingInterface toAdd = new Building(building);
        BuildingInterface searchNode = dijkstraGraph.getNode(toAdd);
        if (searchNode == null) {
            return false;
        }
        return true;

    }

    /**
     * returns the number of buildings in the graph
     * 
     */
    public int getNumBuildings() {
        return dijkstraGraph.getNodeCount();
    }

    /**
     * 
     * returns the shortest path between two buildings (as well as potential stops),
     * along with the distance to travel
     * 
     * @throws NullPointerException     when the building name (input) is null
     * @throws IllegalArgumentException when the distance is not a positive number
     * 
     */
    public double getShortestCost(String startBuilding, String endBuilding, String... stops)
            throws NullPointerException, IllegalArgumentException {
        // throw exception when the buildings (input) is null
        if (startBuilding == null || endBuilding == null || startBuilding == "" || endBuilding == "") {
            throw new NullPointerException("Start or end building cannot be empty");
        }

        // return the buildings in which the building name matches the inputted building
        // name
        BuildingInterface predecessor = new Building(startBuilding);
        BuildingInterface successor = new Building(endBuilding);
        BuildingInterface searchPred = dijkstraGraph.getNode(predecessor);
        BuildingInterface searchSucc = dijkstraGraph.getNode(successor);
        BuildingInterface[] stopsArray = new Building[stops.length];

        if (searchPred == null || searchSucc == null) {
            throw new NullPointerException("the building doesn't exist in the map");
        }

        if (stops.length == 0) {
            // when the stop is empty, calculate the shortestpath cost with two parameters
            return dijkstraGraph.shortestPathStopsCost(searchPred, searchSucc);
        } else {
            // else store the stops in the BuildingInterface array
            for (int i = 0; i < stops.length; i++) {
                BuildingInterface stop = dijkstraGraph.getNode(new Building(stops[i]));
                stopsArray[i] = stop;
            }
        }
        // calculate the shortestpath cost with the shortestPathStopsCost that allows
        // any number of stops
        return dijkstraGraph.shortestPathStopsCost(searchPred, searchSucc, stopsArray);
    }

    /**
     * checks if a road directly connecting two buildings is in the graph
     * 
     * @throws NullPointerException when the building name (input) is null
     */
    public boolean containsRoad(String startBuilding, String endBuilding) throws NullPointerException {
        if (startBuilding == null || endBuilding == null || startBuilding == "" || endBuilding == "") {
            throw new NullPointerException("Start or end building cannot be null");
        }
        // initialize the buildingInterface with the name of the building
        BuildingInterface predecessor = new Building(startBuilding);
        BuildingInterface successor = new Building(endBuilding);
        BuildingInterface searchPred = dijkstraGraph.getNode(predecessor);
        BuildingInterface searchSucc = dijkstraGraph.getNode(successor);

        if (searchPred == null || searchSucc == null) {
            return false;
        }

        return dijkstraGraph.containsEdge(searchPred, searchSucc);

    }

    /**
     * 
     * returns the number of roads in the graph
     */
    public int getNumRoads() {
        return dijkstraGraph.getEdgeCount();

    }

    /**
     * 
     * returns the shortest path between two buildings (as well as potential stops),
     * along with the distance to travel
     * 
     * @throws NullPointerException     when the building name (input) is null
     * @throws IllegalArgumentException when the distance is not a positive number
     * 
     */
    public String getShortestPath(String startBuilding, String endBuilding, String... stops)
            throws NullPointerException, IllegalArgumentException, NoSuchElementException {
        // throw exception when the buidlings (input) is null
        if (startBuilding == null || endBuilding == null || startBuilding == "" || endBuilding == "") {
            throw new NullPointerException("Start or end building cannot be empty");
        }

        // return the buildings in which the building name matches the inputted building
        // name
        BuildingInterface predecessor = new Building(startBuilding);
        BuildingInterface successor = new Building(endBuilding);
        BuildingInterface[] stopsArray = new Building[stops.length];
        BuildingInterface searchPred = dijkstraGraph.getNode(predecessor);
        BuildingInterface searchSucc = dijkstraGraph.getNode(successor);
        List<BuildingInterface> pathList = new ArrayList<BuildingInterface>();
        String pathstr = "";

        if (searchPred == null || searchSucc == null) {
            throw new NullPointerException("the building doesn't exist in the map");
        }

        if (stops.length == 0) {
            // when the stop is empty, calculate the shortestpath cost with two parameters
            pathList = dijkstraGraph.shortestPathStopsData(searchPred, searchSucc);

        } else {
            // else store the stops in the BuildingInterface array
            for (int i = 0; i < stops.length; i++) {
                BuildingInterface stop = dijkstraGraph.getNode(new Building(stops[i]));
                stopsArray[i] = stop;
            }
            pathList = dijkstraGraph.shortestPathStopsData(searchPred, searchSucc, stopsArray);
        }

        if (pathList.size() <= 1) {
            throw new IllegalArgumentException("there is no path");
        }
        // store the name as the string in the shortest path with the ->
        for (int i = 0; i < pathList.size() - 1; i++) {
            pathstr += pathList.get(i).getName() + " -> ";
        }
        pathstr += pathList.get(pathList.size() - 1).getName();

        // calculate the shortestpath cost with the shortestPathStopsCost that allows
        // any number of stops
        return pathstr;

    }

    /**
     * returns the list of the building names that are
     * store in the DijkstraGraph
     *
     */
    public List<String> listBuilding() throws NullPointerException {
        // store the building in the enumeration list
        Enumeration<BuildingInterface> buildingList = dijkstraGraph.nodes.keys();
        List<String> list = new ArrayList<String>();
        while (buildingList.hasMoreElements()) {
            // store the building name into the string list
            list.add(buildingList.nextElement().getName());
        }
        return list;

    }

}
