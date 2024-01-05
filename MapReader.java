import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is used to read dot files and convert them into a graph in the program
 * 
 */
public class MapReader implements MapReaderInterface {
  DijkstraGraph<BuildingInterface, Double> graph;    // graph resulting from the dot file

  public MapReader() {
    graph = new DijkstraGraph<BuildingInterface, Double>();
  }

  /**
   * Reads a file and translates them into a dijkstra-based map
   * 
   * @param filename - file to be read
   * @throws FileNotFoundException when the file doesn't exist
   */
  @Override
  public DijkstraGraph<BuildingInterface, Double> read(String filename) throws FileNotFoundException {
    File file = new File(filename); // stores file contents
    Scanner sc = new Scanner(file); // reads file contents

    List<Building> nodes = new ArrayList<Building>(); // stores each node

    // iterates each line until EOF
    while (sc.hasNextLine()) {
      String line = sc.nextLine().trim();

      // skips line unnecessary lines like comments/empty lines
      if (line.startsWith("#") || !line.contains("->"))
        continue;

      String[] parts = line.split("->"); // splits pred and succ nodes

      String predStr = parts[0].trim();
      String succStr = parts[1].trim().split("\\[")[0].trim(); // removes additional data from line

      double cost = Double.POSITIVE_INFINITY;
      if (parts[1].contains("label")) { // gets cost of edge
        String costStr = parts[1].split("label\" =")[1].split("]")[0].trim();
        cost = Double.parseDouble(costStr);
      }

      Building pred = new Building(predStr);
      Building succ = new Building(succStr);

      // adds them into graph and nodes if building doesn't exist yet
      // unable to use BaseGraph.containsNode as every building is assigned a different key even
      // if the name is the same
      if (!nodes.contains(pred)) {
        nodes.add(pred);
        graph.insertNode(pred);
      }
      if (!nodes.contains(succ)) {
        nodes.add(succ);
        graph.insertNode(succ);
      }

      // due to the issue above, pred & succ must use the specific building that was stored in the
      // graph which can be obtained from the nodes variable
      for (Building b : nodes) {
        if (b.equals(pred)) {
          pred = b;
        }
        if (b.equals(succ)) {
          succ = b;
        }
      }
      // edge is added once the building with similar key is obtained
      graph.insertEdge(pred, succ, cost);
    }

    sc.close();
    return graph;
  }
}
