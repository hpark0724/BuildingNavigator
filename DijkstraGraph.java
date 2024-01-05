import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * This class extends the BaseGraph data structure with additional methods for computing the total
 * cost and list of node data along the shortest path connecting a provided starting to ending
 * nodes. This class makes use of Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
    extends BaseGraph<NodeType, EdgeType> implements DijkstraInterface<NodeType, EdgeType> {

  /**
   * While searching for the shortest path between two nodes, a SearchNode contains data about one
   * specific path between the start node and another node in the graph. The final node in this path
   * is stored in it's node field. The total cost of this path is stored in its cost field. And the
   * predecessor SearchNode within this path is referened by the predecessor field (this field is
   * null within the SearchNode containing the starting node in it's node field).
   *
   * SearchNodes are Comparable and are sorted by cost so that the lowest cost SearchNode has the
   * highest priority within a java.util.PriorityQueue.
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
   * This helper method creates a network of SearchNodes while computing the shortest path between
   * the provided start and end locations. The SearchNode that is returned by this method is
   * represents the end of the shortest path that is found: it's cost is the cost of that shortest
   * path, and the nodes linked together through predecessor references represent all of the nodes
   * along that shortest path (ordered from end to start).
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return SearchNode for the final end node within the shortest path
   * @throws NoSuchElementException when no path from start to end is found or when either start or
   *                                end data do not correspond to a graph node
   */
  protected SearchNode computeShortestPath(NodeType start, NodeType end) {
    // check if start and end nodes exist in the graph
    if (!super.containsNode(start) || !this.containsNode(end)) {
      throw new NoSuchElementException("start or end does not exist in graph");
    }
    // check if start and end nodes are the same
    if (start == end) {
      return new SearchNode(nodes.get(start), 0.0, null);
    }
    // hashtable to keep track of visited nodes
    Hashtable<NodeType, Node> visited = new Hashtable<NodeType, Node>();
    // priority queue to store search nodes
    PriorityQueue<SearchNode> paths = new PriorityQueue<SearchNode>();
    Node startNode = nodes.get(start);
    SearchNode firstNode = new SearchNode(startNode, 0, null);
    paths.add(firstNode);
    while (!paths.isEmpty()) {
      SearchNode temp = paths.remove();
      // if the removed node has not been visited before add the node to the visited hashtable
      if (!visited.containsKey(temp.node.data)) {
        visited.put(temp.node.data, temp.node);
        // if the node is the end node, return the search node, search is done
        if (temp.node.data == end) {
          return temp;
        }
        // add all nodes with edges leaving the removed node to the pq
        for (Edge edge : temp.node.edgesLeaving) {
          SearchNode newNode =
              new SearchNode(edge.successor, temp.cost + edge.data.doubleValue(), temp);
          paths.add(newNode);
        }
      }
    }
    // if no path is found, throw an exception
    throw new NoSuchElementException("no path from start to end");
  }

  /**
   * Returns the list of data values from nodes along the shortest path from the node with the
   * provided start value through the node with the provided end value. This list of data values
   * starts with the start value, ends with the end value, and contains intermediary values in the
   * order they are encountered while traversing this shortest path. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return list of data item from node along this shortest path
   */
  public List<NodeType> shortestPathData(NodeType start, NodeType end) {
    // compute the shortest path using the computeShortestPath method
    SearchNode finalNode = computeShortestPath(start, end);
    LinkedList<NodeType> paths = new LinkedList<NodeType>();
    Node temp = finalNode.node;
    // continue adding previous nodes to the path list until the first node
    while (finalNode.predecessor != null) {
      paths.addFirst(temp.data);
      finalNode = finalNode.predecessor;
      temp = finalNode.node;
    }
    // add the start node data to the beginning of the linked list
    paths.addFirst(start);
    return paths;
  }

  /**
   * Returns the cost of the path (sum over edge weights) of the shortest path freom the node
   * containing the start data to the node containing the end data. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return the cost of the shortest path between these nodes
   */
  public double shortestPathCost(NodeType start, NodeType end) {
    // returns the cost of the path generated my the compute method
    return computeShortestPath(start, end).cost;
  }

  @Override
  public List<NodeType> shortestPathStopsData(NodeType start, NodeType end, NodeType... stops) {
    // no stops
    if (stops.length == 0) {
      return (LinkedList<NodeType>) shortestPathData(start, end);
    }

    // if more than 1 stop

    // add stops to a linkedlist for easier access
    LinkedList<NodeType> stopsList = new LinkedList<NodeType>();
    for (NodeType stop : stops) {
      stopsList.add(stop);
    }
    // more stops than nodes, throw error
    if (stopsList.size() + 2 > nodes.size()) {
      throw new NoSuchElementException("too many stops");
    }

    // final list for entire path through all the stops
    LinkedList<NodeType> combinedList = new LinkedList<NodeType>();
    // temp list to store each path between stops
    LinkedList<NodeType> tempList = new LinkedList<NodeType>();

    // first iteration from the start to the first stop
    tempList = (LinkedList<NodeType>) shortestPathData(start, stopsList.get(0));
    combinedList.addAll(tempList);
    // remove the last node to avoid duplicates on the next iteration
    combinedList.remove(combinedList.size() - 1);
    // compute all the individual paths from one stop to another, and add all those short paths to
    // the final path list
    for (int i = 0; i < stopsList.size() - 1; i++) {
      tempList.clear();
      tempList = (LinkedList<NodeType>) shortestPathData(stopsList.get(i), stopsList.get(i + 1));
      combinedList.addAll(tempList);
      combinedList.remove(combinedList.size() - 1);
    }
    // last iteration from final stop to end
    tempList.clear();
    tempList = (LinkedList<NodeType>) shortestPathData(stopsList.get(stopsList.size() - 1), end);
    combinedList.addAll(tempList);
    return combinedList;
  }

  @Override
  public double shortestPathStopsCost(NodeType start, NodeType end, NodeType... stops) {
    if (stops.length == 0) {
      return computeShortestPath(start, end).cost;
    }

    LinkedList<NodeType> stopsList = new LinkedList<NodeType>();
    for (NodeType stop : stops) {
      stopsList.add(stop);
    }
    double cost = computeShortestPath(start, stopsList.get(0)).cost;
    for (int i = 0; i < stopsList.size() - 1; i++) {
      cost += computeShortestPath(stopsList.get(i), stopsList.get(i + 1)).cost;
    }
    cost += computeShortestPath(stopsList.get(stopsList.size() - 1), end).cost;
    return cost;
  }

  @Override
  public List<NodeType> shortestPathConstrainRoadsData(NodeType start, NodeType end,
      NodeType edgeStart, NodeType edgeEnd) {
    // remove constrainRoad edge from graph
    EdgeType weight = this.getEdge(edgeStart, edgeEnd);
    this.removeEdge(edgeStart, edgeEnd);



    LinkedList<NodeType> paths = (LinkedList<NodeType>) shortestPathData(start, end);
    // insert constrainRoad edge back to graph
    this.insertEdge(edgeStart, edgeEnd, weight);
    return paths;
  }

  @Override
  public double shortestPathConstrainRoadsCost(NodeType start, NodeType end, NodeType edgeStart,
      NodeType edgeEnd) {
    // remove constrainRoad edge from graph
    EdgeType weight = this.getEdge(edgeStart, edgeEnd);
    this.removeEdge(edgeStart, edgeEnd);

    // returns the cost of the path generated my the compute method
    double cost = computeShortestPath(start, end).cost;
    // insert constrainRoad edge back to graph
    this.insertEdge(edgeStart, edgeEnd, weight);
    return cost;
  }

  @Override
  public NodeType getNode(NodeType searchNode) {
    Enumeration<NodeType> enumeration = nodes.keys();
    while (enumeration.hasMoreElements()) {
      NodeType key = enumeration.nextElement();
      if (key.equals(searchNode)) {
        return key;
      }
    }
    return null;
  }
}
