public interface BuildingNavigatorFrontendInterface {

    public void runCommandLoop(); // run the command loop
    public char mainMenuPrompt(); // This will be show the menu
    public void addBuilding(); // Can add building
    public void insertRoad(); // Can insert road
    public void getNumBuildings(); // Gets the number of buildings
    public void getNumRoads(); // Gets the number of road
    public void checkBuilding(); // Checks if there is a building
    public void buildingsList(); // Gets the list of buildings
    public void costOfPath(String startBuilding, String endBuilding); // It will return the cost of the path between two buildings
    public void costOfPath(String startBuilding, String endBuilding, String... stops); // It will return the cost of the path between more than two buildings
    public void shortestPath(String startBuilding, String endBuilding); // finds the shortest path between two buildings. ( It will return the shortest path ex) A → B → C )
    public void shortestPath(String startBuilding, String endBuilding, String... stops); // finds the shortest path between more than two buildings. ( It will return the shortest path ex) A → B → C → D → E )
    
    }
    
