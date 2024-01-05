import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * This is University of Wisconsin maidon's map project Frontend file.
 * It will nevigate the shortest path between buildings.
 * 
 */
public class BuildingNavigatorFrontend implements BuildingNavigatorFrontendInterface {
    private Scanner in; // to get user's input
    private BuildingNavigatorBackendInterface backend; // to use methods from backend
    private String startBuilding; // starting building
    private String endBuilding; // ending building
    private String stops; // stopping building

    /**
     * Constructor to initialize instance variables
     * 
     * @param userInput - to read user input/ files
     * @param backend   - backend implementation
     */
    public BuildingNavigatorFrontend(Scanner in, BuildingNavigatorBackendInterface backend) {
        this.in = in;
        this.backend = backend;
    }

    /**
     * Interactive user interface, loops until user quits by inputting "Q"/"q"
     */
    @Override
    public void runCommandLoop() {
        System.out.println("================================================**");
        System.out.println("This is University of Wisconsin Madison's buliding map");
        System.out.println("&&================================================");
        
        try {
            backend.loadData("cs400project3.dot");
            System.out.println("data load!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } // load data from file → It will directly return the wisconsin map

        char command = '\0';
        while (command != 'Q') { // main loop continues until user chooses to quit
            command = this.mainMenuPrompt();
            switch (command) {
                case '+': // Add[+] buliding with buliding, endbuilding and cost
                    addBuilding();
                    break;
                case 'I': // [I] nsert some path between two buildings
                    insertRoad();
                    break;
                case 'B': // Get the number of [B] uildings
                    getNumBuildings();
                    break;
                case 'R': // Get the number of [R] oads
                    getNumRoads();
                    break;
                case 'C': // [C] heck whether or not the building is in the map
                    checkBuilding();
                    break;
                case 'L': // [L] ist of the buildings in the map
                    buildingsList();
                    break;
                case 'D' : // [D] istance of two buildings in the map
                    costOfPath(startBuilding, endBuilding);
                    break;
                case 'E' : // Distanc [E] of more than two buildings in the map
                    costOfPath(startBuilding, endBuilding, stops);
                    break;
                case 'S': // It will get the [S] hortest path between two buildings
                    shortestPath(startBuilding, endBuilding);
                    break;
                case 'P': // It will get the shortest [P] ath between more than two buildings
                    shortestPath(startBuilding, endBuilding, stops);
                    break;
                case 'Q': // [Q]uit
                    System.out.println("Thanks for using University of Wisconsin Madison's buliding map!");
                    break;
                default:
                    System.out.println(
                            "Unrecognizable command. Pick a command by selecting one of the letters within []s.");
                    break;
            }
        }
    }

    /**
     * Main prompt for the user to interact with.
     */
    @Override
    public char mainMenuPrompt() {
        // prints prompt
        System.out.println("Pick a command from the list below!\n"
                + "    Add Bulidings [+]\n" + "    [I]nsert roads\n" 
                + "    Get the number of [B]uildings\n" + "    Get the number of [R]oads\n"
                + "    [C]heck whether or not the building is in the map\n" + "    [L]ist of the buildings in the map\n" 
                + "    [D]istance of two buildings in the map\n" + "    Distanc[E] of more than two buildings in the map\n"
                + "    [S]hortest path between two buildings\n" + "    shortest [P] ath between more than two buildings\n" 
                + "    [Q]uit\n");
        System.out.print("Enter command: ");

        String input = in.nextLine().trim();
        if (input.length() == 0) // if user's choice is blank, return null character
            return '\0';
        // otherwise, return an uppercase version of the first character in input
        return Character.toUpperCase(input.charAt(0));
    }


    /**
     * 
     * Prompts user to input the building to be added based on the name.
     * errors will be thrown if empty string is entered.
     * 
     */
    public void addBuilding() {
        System.out.println("Write the building name, which you want to add in the map : ");
        String input = in.nextLine().trim();
        try {
            boolean rst = backend.addBuilding(input);

            if (rst == true) {
                System.out.println("Map added "+ input + ".");
            } else {
                System.out.println("This building cannot be added in the map.");
            }
        } catch (Exception e) {
            System.out.println("This building cannot be added in the map.");
        }
    }


    /**
     * 
     * It will insert the road into the map with [startBuilding/endBuilding/cost] this format.
     * If the length is not three, empty string is entered or doesn't follow format, it will return the error message
     * 
     */
    public void insertRoad() {
        System.out.println("Add your road in this format [startBuilding/endBuilding/cost], press [Q] to exit:");
        String input = in.nextLine().trim();
        String[] details = input.split("/"); // splits String into it's respective categories

        if (details[0].toUpperCase().equals("Q") && details.length == 1) { // check if user wants to quit
            return;
        }

        while (details.length != 3) { // checks if 3 details are included
            System.out.println("Invalid data/format entered! Try again.");
            System.out
                    .println("Add your road in this format [startBuilding/endBuilding/cost], press [Q] to exit:");
            input = in.nextLine().trim();
            details = input.split("/");

            if (details[0].toUpperCase().equals("Q") && details.length == 1) {
                return;
            }
        }

        try {
            double distance = Double.parseDouble(details[2].trim());
            backend.insertRoad(details[0].trim(), details[1].trim(), distance);
            System.out.println("Road successfully added!");
        } catch (NullPointerException npe) { // missing/empty parts
            System.out
                    .println("Missing data! Make sure the road is in this format [startBuilding/endBuilding/cost]."
                            + "\nTry again.");
        } catch (IllegalArgumentException e) { // duplicate road
            System.out.println("Road already exists! Try again.");
        }
    }

    /**
     * 
     * This method will get the number of buildings in the map
     * 
     */
    @Override
    public void getNumBuildings() {
        try {
            int numberOfBuildings = backend.getNumBuildings();
            System.out.println("This map contains " + numberOfBuildings + " buildings.");
        } catch (Exception e) {
            System.out.println("This map does not exist.");
        }
    }

    /**
     * 
     * This method will get the number of roads in the map
     * 
     */
    @Override
    public void getNumRoads() {
        try {
            int numberOfRoads = backend.getNumRoads();
            System.out.println("This map contains " + numberOfRoads + " roads.");
        } catch (Exception e) {
            System.out.println("This map does not exist.");
        }
    }


    /**
     * 
     * It will check whether or not the building is in the map
     * if empty string is entered or write something wrong word, it wil return error message
     * 
     */
    public void checkBuilding() {
        System.out.println("Enter the building's name: ");
        String input = in.nextLine().trim();
        try {
            boolean rst = backend.containsBuilding(input);

            if (rst == true) {
                System.out.println("Map contained "+ input + ".");
            } else {
                System.out.println("This building does not exist.");
            }
        } catch (Exception e) {
            System.out.println("This building does not exist.");
        }
    }

    /**
     * 
     * It method get the list of all the buildings in the map
     * This method will make user to know which building is in the map
     * It will make user easier to find the building
     * 
     */
    @Override
    public void buildingsList(){
        try {
            List<String> backList = backend.listBuilding();
            System.out.println("This is the buildings list in the University of Wisconsin Madison map : " + backList);
        } catch (Exception e) {
            System.out.println("There is no buildings in the map.");
        }
    }

    /**
     * 
     * This is for count the cost of between two buildings.
     * If user write startbuilding and the endbuilding, it will return the cost of the shortest path between two buildings.
     * If the length is not three, empty string is entered or doesn't follow format, it will return the error message
     * [/] is used to split
     * instead of [,] because some buildings may contain commas.
     * 
     */
    @Override
    public void costOfPath(String startBuilding, String endBuilding){
        System.out.println("Find the cost of the path in this format [startBuilding/endBuilding], press [Q] to exit:");
        String input = in.nextLine().trim();
        String[] details = input.split("/"); // splits String into it's respective categories

        if (details[0].toUpperCase().equals("Q") && details.length == 1) { // check if user wants to quit
            return;
        } 
        while (details.length != 2) { // checks if 2 details are included
            System.out.println("Invalid data/format entered! Try again.");
            System.out
                    .println("Find the cost of the path in this format [building/endBuilding], press [Q] to exit:");
            input = in.nextLine().trim();
            details = input.split("/");

            if (details[0].toUpperCase().equals("Q") && details.length == 1) {
                return;
            }
        }
        
        try {
            double backCost = backend.getShortestCost(details[0].trim(), details[1].trim());
            System.out.println("This is the cost of the path from " + details[0].trim() + " to " + details[1].trim() + " : " + backCost);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * This is for count the cost of between more than two buildings.
     * If user write startbuilding and the end building and the stop, it will return the cost of the shortest path between buildings.
     * If the length is not three, empty string is entered or doesn't follow format, it will return the error message
     * [/] is used to split
     * instead of [,] because some buildings may contain commas.
     * 
     */
    @Override
    public void costOfPath(String startBuilding, String endBuilding, String... stops){
        System.out.println("Find the cost of the path in this format [startBuilding/endBuilding/stops], press [Q] to exit:");
        String input = in.nextLine().trim();
        String[] details = input.split("/"); // splits String into it's respective categories

        if (details[0].toUpperCase().equals("Q") && details.length == 1) { // check if user wants to quit
            return;
        } 
        while (details.length < 3) { // checks if 2 details are included
            System.out.println("Invalid data/format entered! Try again.");
            System.out
                    .println("Find the cost of the path in this format [building/endBuilding/stops], press [Q] to exit:");
            input = in.nextLine().trim();
            details = input.split("/");

            if (details[0].toUpperCase().equals("Q") && details.length == 1) {
                return;
            }
        }

        String[] stopsList = new String[details.length - 2];
        for (int i = 2; i < details.length; i++) {
            stopsList[i-2] = details[i].trim();
        }
        
        try {
            String stopsString = String.join(", ", stopsList);
            double backCost = backend.getShortestCost(details[0].trim(), details[1].trim(), stopsList);
            System.out.println("This is the cost of the path from " + details[0].trim() + " to " + details[1].trim() + " (Stops building : " + stopsString + ") : " + backCost);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * This is the shortest path between two buildings.
     * User will enter the two buildings and it will return the shortest path between them.
     * If the length is not three, empty string is entered or doesn't follow format, it will return the error message
     * [/] is used to split
     * instead of [,] because some buildings may contain commas.
     * 
     */
    @Override
    public void shortestPath(String startBuilding, String endBuilding) {
        System.out.println("Find the shortest path of the path in this format [startBuilding/endBuilding], press [Q] to exit:");
        String input = in.nextLine().trim();
        String[] details = input.split("/"); // splits String into it's respective categories

        if (details[0].toUpperCase().equals("Q") && details.length == 1) { // check if user wants to quit
            return;
        } 
        while (details.length != 2) { // checks if 2 details are included
            System.out.println("Invalid data/format entered! Try again.");
            System.out
                    .println("Find the shortest path of the path in this format [building/endBuilding], press [Q] to exit:");
            input = in.nextLine().trim();
            details = input.split("/");

            if (details[0].toUpperCase().equals("Q") && details.length == 1) {
                return;
            }
        }
        
        try {
            String backPath = backend.getShortestPath(details[0].trim(), details[1].trim());
            System.out.println("This is the path from " + details[0].trim() + " to " + details[1].trim() + "\n" + backPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * This is the shortest path between more than two buildings.
     * User will enter more than two buildings and it will return the shortest path between them.
     * If the length is not three, empty string is entered or doesn't follow format, it will return the error message
     * [/] is used to split
     * instead of [,] because some buildings may contain commas.
     * 
     */
    @Override
    public void shortestPath(String startBuilding, String endBuilding, String... stops){
        System.out.println("Find the shortest path of the path in this format [startBuilding/endBuilding/stops], press [Q] to exit:");
        String input = in.nextLine().trim();
        String[] details = input.split("/"); // splits String into it's respective categories

        if (details[0].toUpperCase().equals("Q") && details.length == 1) { // check if user wants to quit
            return;
        } 
        while (details.length < 3) { // checks if 2 details are included
            System.out.println("Invalid data/format entered! Try again.");
            System.out
                    .println("Find the shortest path of the path in this format [building/endBuilding/stops], press [Q] to exit:");
            input = in.nextLine().trim();
            details = input.split("/");

            if (details[0].toUpperCase().equals("Q") && details.length == 1) {
                return;
            }
        }

        String[] stopsList = new String[details.length - 2];
        for (int i = 2; i < details.length; i++) {
            stopsList[i-2] = details[i].trim();
        }

        try {
            String stopsString = String.join(", ", stopsList);
            String backPaths = backend.getShortestPath(details[0].trim(), details[1].trim(), stopsList);
            System.out.println("This is the path from " + details[0].trim() + " to " + details[1].trim() + " (Stops building : " + stopsString + ") \n " + backPaths);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


  public static void main(String[] args) {
    DijkstraGraph<BuildingInterface, Double> rbt = new DijkstraGraph<BuildingInterface, Double>();
    BuildingNavigatorBackendInterface back = new BuildingNavigatorBackend(rbt, new MapReader());
    Scanner sc = new Scanner(System.in);
    BuildingNavigatorFrontendInterface front = new BuildingNavigatorFrontend(sc, back);
    
    front.runCommandLoop();
  }

}
