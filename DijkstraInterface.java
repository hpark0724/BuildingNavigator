import java.util.List;

/**
 *
 */
public interface DijkstraInterface<NodeType, EdgeType extends Number>
    extends GraphADT<NodeType, EdgeType> {
  // public void DijkstraInterface();
  public List<NodeType> shortestPathStopsData(NodeType start, NodeType end, NodeType... stops);

  public double shortestPathStopsCost(NodeType start, NodeType end, NodeType... stops);

  public List<NodeType> shortestPathConstrainRoadsData(NodeType start, NodeType end, NodeType edgeStart,
      NodeType edgeEnd);

  public double shortestPathConstrainRoadsCost(NodeType start, NodeType end, NodeType edgeStart,
      NodeType edgeEnd);

  public NodeType getNode(NodeType searchNode);
}

